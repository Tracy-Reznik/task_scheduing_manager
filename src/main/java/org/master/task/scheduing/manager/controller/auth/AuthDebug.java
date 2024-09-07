package org.master.task.scheduing.manager.controller.auth;

import org.master.task.scheduing.manager.service.auth.CaptchaTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = "/authtest")
public class AuthDebug {
    @Autowired
    private CaptchaTestService captchaTestService;
    @GetMapping("/get")
    public String get(){

        return captchaTestService.testCaptchaOperationsGet();
    }
    @GetMapping("/set")
    public String set(){
        captchaTestService.testCaptchaOperationsSet();
        return "debug";
    }
}
