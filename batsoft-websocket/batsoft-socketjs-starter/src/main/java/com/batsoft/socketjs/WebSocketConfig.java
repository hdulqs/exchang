package com.batsoft.socketjs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * WebSocket配置
 * 
 * @author simon
 */
@EnableWebSocketMessageBroker
@Configuration("socketJsConfig")
@EnableConfigurationProperties({WebSocketProperties.class})
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
    @Autowired
    private WebSocketProperties webSocketProperties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(webSocketProperties.getBroker());
        config.setApplicationDestinationPrefixes(webSocketProperties.getAppDestinationPrefixes());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketProperties.getPoint()).setAllowedOrigins("*").withSockJS();
    }
    
}