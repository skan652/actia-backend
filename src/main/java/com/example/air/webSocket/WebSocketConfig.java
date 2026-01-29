package com.example.air.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private Frame1WebSocketHandler frame1WebSocketHandler;

    @Autowired
    private Frame2WebSocketHandler frame2WebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(frame1WebSocketHandler, "/websocket-frame1")
                .setAllowedOrigins("*"); // Allow all origins, modify as needed

        registry.addHandler(frame2WebSocketHandler, "/websocket-frame2")
                .setAllowedOrigins("*"); // Allow all origins, modify as needed
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}


