package org.master.task.scheduing.manager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.master.task.scheduing.manager.dao.impl.auth.InvalidateTokenRepository;
import org.master.task.scheduing.manager.entity.auth.InvalidateToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
    private final String secret = "6K+d6K+06YKj5peF6KGM6ICF6KGM6Iez6Iqx5p6c5bGx5rex5aSE77yM5L6/6KeB5b6X5LiA6YCP6Zyy552A5rex5riK5rCU5oGv55qE6YKq55+z77yM5q2j5b2T5peF6KGM6ICF6Zet5bmV5rKJ5oCd5LmL5Yi777yM5L6/6KeB5LiA5q+b6IS46Zu35YWs5Zi055qE5L+u6KGM6ICF77yM5omL5oyB5LiA5oqK6Zeq552A6YeR5YWJ55qE5Lik5bC65qON5a2Q77yM5a+5552A6L+Z5Liq55+z5aS054ug54ug5Zyw56C45LiL5Y6777yM6KeB5peF6KGM6ICF5LiN6Kej77yM5q2k5Lq66Kej6YeK5Yiw77yM5ZC+5LmD6b2Q5aSp5aSn5Zyj77yM5aaC5LuK5Zyo6Iqx5p6c5bGx57un57ut5L+u6KGM77yM5q2k5aaW5LmD5YWt6ICz54yV54y077yM5aaC5LuK5Zyo6Iqx5p6c5bGx5YaS5YWF5oiR5Y2x5a6z6IuN55Sf77yM5ZCO6YCD6Iez5LiA5Liq5Y+r5o+Q55Om54m555qE5Zyw5pa577yM5Lmg5b6X5rex5riK6YKq5pyv77yM5Y2x5a6z5o+Q55Om54m577yM5aaC5LuK77yM5a6D5LuT55qH6YCD5Zue6Iqx5p6c5bGx77yM5LyB5Zu+55So5rex5riK6YKq5pyv57un57ut5Y2x5a6z5LyX55Sf77yM6KeB5Yiw5L2g5oiR5LqM5Lq677yM5LyB5Zu+5YyW5Li65bGx55+z77yM6JKZ5re36L+H5YWz77yM55yL5oiR5LiA5qOS5Y+W5a6D54uX5ZG977yM5peF6KGM6ICF6ZqP5Y2z6Kej6YeK5Yiw77yM5ZC+5LmD5peF6KGM6ICF77yM5LuO5o+Q55Om54m56ICM5p2l77yM5oiR5bey57uP6KeB6K+B5Yiw5LqG5rex5riK6YKq5pyv5Li65a6z5o+Q55Om54m577yM6K+0572i5ZKM5aSn5Zyj55eb5omT5q2k55+z77yM5q2k55+z6ZqP5Y2z5YyW5b2i5Li65LiA5Y+q54y05a2Q77yM6KKr5LqM5Lq65b2T5Y2z55eb5omT6Ie05q27";

    @Autowired
    private InvalidateTokenRepository invalidateTokenRepository;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        log.info(subject);
        // 1 hour
        int jwtExpirationInMs = 3600000;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public long getTokenRemainingTime(String token) {
        // 获取token中的claims信息
        Claims claims = getClaimsFromToken(token);

        // 获取token的过期时间
        Date expirationDate = claims.getExpiration();

        // 计算当前时间与过期时间的差值，得到剩余时间（毫秒）
        long remainingTimeInMillis = expirationDate.getTime() - System.currentTimeMillis();

        // 如果剩余时间小于等于0，返回0
        return remainingTimeInMillis > 0 ? remainingTimeInMillis : 0;
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token) && !isTokenBlacklisted(token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    private boolean isTokenBlacklisted(String token) {
        Optional<InvalidateToken> invalidateTokenOpt = invalidateTokenRepository.findByToken(token);
        if (invalidateTokenOpt.isPresent()) {
            Date invalidateTime = invalidateTokenOpt.get().getInvalidateTime();
            return invalidateTime.after(new Date());
        }
        return false;
    }

    public void invalidateToken(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        InvalidateToken invalidateToken = new InvalidateToken(token, expiration);
        invalidateTokenRepository.save(invalidateToken);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
