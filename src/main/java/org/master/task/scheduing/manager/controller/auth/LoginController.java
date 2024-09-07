package org.master.task.scheduing.manager.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.master.task.scheduing.manager.entity.Response;
import org.master.task.scheduing.manager.service.TokenCleanupService;
import org.master.task.scheduing.manager.service.auth.CaptchaService;
import org.master.task.scheduing.manager.service.auth.UserDetailsService;
import org.master.task.scheduing.manager.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    TokenCleanupService tokenCleanupService;



    @GetMapping(value = "/captcha", produces = "text/plain;charset=UTF-8")
    public String getCaptcha(HttpServletResponse response, @RequestHeader(value = "Captcha-Token") String captchaToken) throws IOException {
        Map<String, Object> captcha = captchaService.generateCaptcha(captchaToken);
        response.addHeader("Captcha-Token", (String) captcha.get("token"));
        return java.util.Base64.getEncoder().encodeToString(((ByteArrayOutputStream) captcha.get("image")).toByteArray());
    }

    @PostMapping("/login")
    public Response login(String username, String password, String captcha, @RequestHeader(value = "Captcha-Token") String captchaToken) {
        // 验证验证码
        Response loginResponse = new Response();
        if (!captchaService.validateCaptchaToken(captchaToken)){
            loginResponse.setCode(HttpStatus.FORBIDDEN.value());
            loginResponse.setType("CAPTCHA_ERROR");
            loginResponse.setMessage("验证码已过期");
            return loginResponse;
        }
        if (!captchaService.validateCaptcha(captcha, captchaToken)) {
            loginResponse.setCode(HttpStatus.FORBIDDEN.value());
            loginResponse.setType("CAPTCHA_ERROR");
            loginResponse.setMessage("验证码有误");
            return loginResponse;
        }


        try {
            UsernamePasswordAuthenticationToken tokenObject = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(tokenObject);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 此处可以生成JWT令牌并返回
            String token = jwtTokenUtil.generateToken(username);
            captchaService.deleteCaptcha(captchaToken);
            return new Response(200, "登录成功", "LOGIN_SUCCESS", token);
        } catch (AuthenticationException e) {
            System.out.println(e.toString());
            return new Response(401, "用户名或密码错误", "LOGIN_ERROR", null);
        }
    }

    @GetMapping("/gettoken")

    Response generalToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new Response(400, "请求无效", "LOGOUT_ERROR", null);
        }
        String token = authHeader.substring(7);
        String new_token=token;
        String message = "token剩余时间较长，不需要更新";
        if (jwtTokenUtil.getTokenRemainingTime(token)<=20 * 60 * 1000){
            new_token=jwtTokenUtil.generateToken(jwtTokenUtil.getUsernameFromToken(token));
            jwtTokenUtil.invalidateToken(token);
            message = "token已更新";

        }
        return new Response(200, message, "TOKEN_SUCCESS", new_token);

    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 获取JWT令牌
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(400, "请求无效", "LOGOUT_ERROR", null));
        }

        String token = authHeader.substring(7);
        tokenCleanupService.cleanUpExpiredTokens();

        // 将令牌列入黑名单使其失效
        jwtTokenUtil.invalidateToken(token);

        // 清除SecurityContext中的认证信息
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new Response(200, "注销成功", "LOGOUT_SUCCESS", null));
    }

}
