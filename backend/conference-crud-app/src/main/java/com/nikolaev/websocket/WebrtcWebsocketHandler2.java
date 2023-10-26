package com.nikolaev.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class WebrtcWebsocketHandler2 extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, WebSocketSession> activePeers = new HashMap<>();
    Map<String, WebSocketSession> sessionsWithId = new HashMap<>();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        Map data = objectMapper.readValue(payload, HashMap.class);

        String id = (String) data.get("id");

//        for (WebSocketSession webSocketSession : sessions) {
//            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
//                webSocketSession.sendMessage(message);
//            }
//        }

        ConcurrentWebSocketSessionDecorator decorator = new ConcurrentWebSocketSessionDecorator(session, 10, 1024 * 20);

        handleIdMessage(id, decorator);
        handleOfferMessage(data, message, session.getId());
        handleAnswerMessage(data, message, session.getId());
        handleCandidateMessage(data, message, session.getId());
    }

    private void handleIdMessage(String id, WebSocketSession session) {
        if (id != null) {
            log.info("New session connected with id = {}", id);
            sessionsWithId.put(id, session);
            activePeers.put(id, session);
//            if (activePeers.isEmpty()) {
//                activePeers.put(id, session);
//            }

        }
    }

    private void handleOfferMessage(Map data, TextMessage message, String sessionId) throws IOException {
        String event = (String) data.get("event");
        if (event != null && event.equals("offer")) {
            log.info("Handling offer message");

//            for (WebSocketSession webSocketSession : sessions) {
//                if (webSocketSession.isOpen() && !sessionId.equals(webSocketSession.getId())) {
//                    webSocketSession.sendMessage(message);
//                }
//            }

            if (activePeers.isEmpty()) {
                return;
            }

            for (Map.Entry<String, WebSocketSession> entry : activePeers.entrySet()) {
                if (entry.getValue().getId().equals(sessionId))
                    continue;
                if (!entry.getValue().isOpen()) {
                    continue;
                }
                log.info("Broadcasting offer message");
                entry.getValue().sendMessage(message);
            }
        }
    }

    private void handleAnswerMessage(Map data, TextMessage message, String sessionId) throws IOException {
        String event = (String) data.get("event");
        if (event != null && event.equals("answer")) {
            log.info("Handling answer message");

//            for (WebSocketSession webSocketSession : sessions) {
//                if (webSocketSession.isOpen() && !sessionId.equals(webSocketSession.getId())) {
//                    webSocketSession.sendMessage(message);
//                }
//            }

            String wsId = (String) data.get("wsId");
            log.info("Sending answer message to = {}", wsId);
            WebSocketSession origin = sessionsWithId.get(wsId);
            origin.sendMessage(message);
        }
    }

    private void handleCandidateMessage(Map data, TextMessage message, String sessionId) throws IOException {
        String event = (String) data.get("event");
        if (event != null && event.equals("candidate")) {

//            for (WebSocketSession webSocketSession : sessions) {
//                if (webSocketSession.isOpen() && !sessionId.equals(webSocketSession.getId())) {
//                    webSocketSession.sendMessage(message);
//                }
//            }

//            for (Map.Entry<String, WebSocketSession> entry : activePeers.entrySet()) {
//                if (entry.getValue().getId().equals(sessionId))
//                    continue;
//                if (!entry.getValue().isOpen()) {
//                    continue;
//                }
//                log.info("Broadcasting candidate message");
//                entry.getValue().sendMessage(message);
//            }

            String wsId = (String) data.get("wsId");
            WebSocketSession origin = sessionsWithId.get(wsId);
            origin.sendMessage(message);
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}
