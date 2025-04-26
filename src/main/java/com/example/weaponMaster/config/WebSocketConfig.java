package com.example.weaponMaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 프론트에서 구독할 주소 prefix
        config.setApplicationDestinationPrefixes("/app");      // 서버가 메시지 받을 때 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")    // WebSocket 연결할 주소
                .setAllowedOriginPatterns("*") // CORS 허용 (Svelte 프론트랑 통신)
                .withSockJS();                 // SockJS fallback (브라우저 호환성용)
    }
}

