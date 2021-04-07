package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp
     * The WebSocket protocol defines two types of messages (text and binary), but their content is undefined.
     * Stomp基于websocket之上的消息传递协议,对传递的文本定义了读取/写入的格式
     * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-fallback-sockjs-overview
     * SockJS是一种兼容的方案,当游览器或网络环境不支持websocket协议时,通过http模拟websocket传输
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //认证端点
        registry.addEndpoint("/ws/ep")
                .setAllowedOrigins("http://localhost:8080")
                //交互方式
                .withSockJS();
    }

/*    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setTimeToFirstMessage()
    }*/

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //启用一个简单的消息代理，并配置一个或多个前缀来过滤以该代理为目标的目的地(例如，以“/topic”为前缀的目的地)
        //Enable a simple message broker and configure one or more prefixes to filter destinations targeting the broker
        //(e.g. destinations prefixed with "/topic").
        //.setHeartbeatValue().setTaskScheduler().setSelectorHeaderName()
        //所有destinations prefixed with  '/queue'的消息处理,都将遵循如上配置,可以为null,存在
        //默认值
        registry.enableSimpleBroker("/queue");
    }
}
