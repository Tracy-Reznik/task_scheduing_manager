package org.master.task.scheduing.manager.service.auth;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CaptchaService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${captcha.time}")
    private String captchaTime;

    private static final Logger log = LoggerFactory.getLogger(CaptchaService.class);

    public long getCaptchaTime() {
        // 提取数字和单位
        Pattern pattern = Pattern.compile("(\\d+)([a-z]+)");
        Matcher matcher = pattern.matcher(captchaTime);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Invalid captcha time format: " + captchaTime);
        }
    }

    public TimeUnit getCaptchaTimeUnit() {
        Pattern pattern = Pattern.compile("(\\d+)([a-z]+)");
        Matcher matcher = pattern.matcher(captchaTime);
        if (matcher.matches()) {
            String unit = matcher.group(2);

            // 返回对应的 TimeUnit
            return switch (unit) {
                case "min" -> TimeUnit.MINUTES;
                case "h" -> TimeUnit.HOURS;
                case "s" -> TimeUnit.SECONDS;
                default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
            };
        } else {
            throw new IllegalArgumentException("Invalid captcha time format: " + captchaTime);
        }
    }


    public Map<String,Object> generateCaptcha(String captchaToken){
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(128, 32);

        if (captchaToken.isEmpty()){
            UUID uuid = UUID.randomUUID();
            captchaToken = uuid.toString();
        }
        log.info(lineCaptcha.getCode());
        stringRedisTemplate.opsForValue().set(captchaToken,lineCaptcha.getCode(),getCaptchaTime(),getCaptchaTimeUnit());
        Map<String,Object> map = new HashMap<>();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        lineCaptcha.write(bytes);
        map.put("token",captchaToken);
        map.put("image",bytes);
        return map;
    }

    public boolean validateCaptchaToken(String captchaToken){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(captchaToken));
    }

    public boolean validateCaptcha(String captcha, String captchaToken) {
        boolean isCaptcha=captcha.equals(stringRedisTemplate.opsForValue().get(captchaToken));
        if(!isCaptcha){
            deleteCaptcha(captchaToken);
        }
        return isCaptcha;
    }
    public void deleteCaptcha(String captchaToken){
        stringRedisTemplate.delete(captchaToken);
    }
}
