package com.nikolaev.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebrtcWebsocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    WebSocketSession thirdPeer;
    String thirdPeerX;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        HashMap map = objectMapper.readValue(payload, HashMap.class);
        String event = (String) map.get("event");
        String x = (String) map.get("x");
        System.out.println("X = " + x);
        System.out.println("event = " + event);
        if (event.equals("offer") && x != null) {
            thirdPeer = session;
            thirdPeerX = x;
        }

        if ((event.equals("answer") || event.equals("candidate")) && x != null && x.equals(thirdPeerX)) {
            System.out.println("Sending message to " + thirdPeer.getId());
            System.out.println("x = " + x);
            thirdPeer.sendMessage(message);
        } else {
            System.out.println("Broadcasting message");
            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                    webSocketSession.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}
