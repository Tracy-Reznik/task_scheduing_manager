package org.master.task.scheduing.manager.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CaptchaTestService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void testCaptchaOperationsSet() {
        String testKey = "captchaTestKey";
        String testValue = "captchaTestValue";

        stringRedisTemplate.opsForValue().set(testKey, testValue);
    }
    public String testCaptchaOperationsGet() {
        String testKey = "captchaTestKey";
        String retrievedValue = stringRedisTemplate.opsForValue().get(testKey);

        System.out.println("Retrieved value: " + retrievedValue); // Should print "captchaTestValue"
        return retrievedValue;
    }
}
