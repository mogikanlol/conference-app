package com.nikolaev.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@EnableWebSocket
@Configuration
public class WebsocketConfiguration implements WebSocketConfigurer {


    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebsocketHandler(objectMapper), "/websocket").setAllowedOrigins("*");

        registry.addHandler(new WebrtcWebsocketHandler(), "/webrtc").setAllowedOrigins("*");
        registry.addHandler(new WebrtcWebsocketHandler2(), "/webrtc2").setAllowedOrigins("*");
    }
}
