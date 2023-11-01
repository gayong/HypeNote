package com.surf.editor.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override // 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //해당 주소를 구독하는 클라이언트에게 메시지를 보낸다. 즉, 인자에는 구독 요청의 prefix를 넣고,
        // 클라이언트에서 1번 채널을 구독하고자 할 때는 sub/1 형식과 같은 규칙을 따라야 한다.
        registry.enableSimpleBroker("/sub");

        //메시지 발행 요청의 prefix를 넣는다. 즉, /pub로 시작하는 메시지만 해당 Broker에서 받아서 처리한다.
        registry.setApplicationDestinationPrefixes("/pub");

    }

    @Override
    // 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정한다.
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/api/editor/ws") // ex) ws://localhost:8080/api/editor/stomp
                .setAllowedOriginPatterns("*")
                .withSockJS(); //테스트할때는 주석처리, 테스트할때 ws대신 http사용
    }
}
