package org.master.task.scheduing.manager.controller;

import org.master.task.scheduing.manager.service.StatusInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class StatusManager {

    @Autowired
    private StatusInfoService statusInfoService;

    @GetMapping
    public Map<String,Object> getStatus(){
        Map<String,Object> response = new HashMap<>();
        response.put("code",200);
        Map<String, Map<String, Object>> info =new HashMap<>();
        Map<String, Object> serverInfo=statusInfoService.getServerInfo();
        info.put("server",serverInfo);
        response.put("info",info);
        return response;
    }
    @GetMapping(value = "/storage")
    public Map<String,Object> getStorageStatus(){
        Map<String,Object> response = new HashMap<>();
        response.put("code",200);
        Map<String, Map<String, Object>> info =new HashMap<>();
        Map<String, Object> serverInfo=statusInfoService.getServerInfo();
        info.put("server",serverInfo);
        response.put("info",info);
        return response;
    }
}
