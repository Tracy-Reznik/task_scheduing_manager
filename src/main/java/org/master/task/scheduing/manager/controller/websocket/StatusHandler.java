package org.master.task.scheduing.manager.controller.websocket;

import org.master.task.scheduing.manager.service.StatusInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class StatusHandler extends TextWebSocketHandler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Autowired
    private StatusInfoService statusInfoService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String statusData = getStatusData(); // 获取状态数据
                session.sendMessage(new TextMessage(statusData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS); // 每5秒发送一次数据
    }

    private String getStatusData() {
        Map<String, Object> liveStatusInfo = statusInfoService.getLiveStatus();
        return liveStatusInfo.toString(); // 返回状态数据，可以使用JSON序列化
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 可以在这里处理客户端发来的消息
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // 清理资源
        scheduler.shutdown();
    }
}
