package com.example.air.webSocket;

import com.example.air.entity.Frame1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Frame1WebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void notifyClients(Frame1 frame) {
        try {
            String jsonData = objectMapper.writeValueAsString(frame);
            TextMessage message = new TextMessage(jsonData);

            for (WebSocketSession session : sessions) {
                session.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

