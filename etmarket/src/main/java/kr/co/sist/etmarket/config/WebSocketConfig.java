//package kr.co.sist.etmarket.config;
//
//import kr.co.sist.etmarket.handler.WebSocketHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.config.annotation.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.HashMap;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final WebSocketHandler webSocketHandler;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(webSocketHandler, "/chating/{chatroomId}"); // 채팅방 구분
//    }
//}
