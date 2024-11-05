package kr.co.sist.etmarket.config;

import kr.co.sist.etmarket.handler.WebSocketExHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketExConfig implements WebSocketConfigurer {

    private final WebSocketExHandler webSocketExHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketExHandler, "/chatingEx/{userName}");
    }
}
