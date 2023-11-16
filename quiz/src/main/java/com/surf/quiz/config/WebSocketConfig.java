package com.surf.quiz.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker //웹 소켓 메시지를 다룰 수 있게 허용
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/quiz/stomp/ws")  // 서버의 웹소켓 엔드 포인트 등록
                .setAllowedOriginPatterns("*") // 모든 오리진 허용
                .withSockJS();      // 만약 WebSocket을 사용할 수 없는 브라우저라면 다른 방식을 사용하도록 설정
//                 테스트 환경에선 주석 처리
//                .setAllowedOrigins("https://k9e101.p.ssafy.io", "http://k9e101.p.ssafy.io");  // 커넥션을 맺는 경로 설정

//
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지를 가공하고 처리할 필요가 있을 때
        config.setApplicationDestinationPrefixes("/pub"); // 발행자가 "/pub"의 경로로 메시지를 주면 가공을 해서 구독자들에게 전달
        config.enableSimpleBroker("/sub"); // 구독자는 "/sub"의 경로로 구독하여 메세지를 받음
    }




}
