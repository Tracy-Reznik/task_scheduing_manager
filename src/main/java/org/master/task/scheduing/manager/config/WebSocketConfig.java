package org.master.task.scheduing.manager.config;

import org.master.task.scheduing.manager.controller.websocket.StatusHandler;
import org.master.task.scheduing.manager.util.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtTokenUtil jwtTokenUtil;

    public WebSocketConfig(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil=jwtTokenUtil;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new StatusHandler(), "/status")
                .addInterceptors(new JwtHandshakeInterceptor(jwtTokenUtil))
                .setAllowedOrigins("*");
    }
}
