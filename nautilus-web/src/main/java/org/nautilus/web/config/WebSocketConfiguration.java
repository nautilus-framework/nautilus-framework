package org.nautilus.web.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfiguration.class);
    
    @Bean
    public Map<String, List<String>> loggedUsers() {
        return new ConcurrentHashMap<>();
    }
    
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/user");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		// Not implemented
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
	    // Not implemented
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
	    // Not implemented
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	    // Not implemented
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
	    // Not implemented
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
	 // Not implemented
		return false;
	}
	
    @Configuration
    public class WebSocketOnConnected implements ApplicationListener<SessionConnectEvent> {

        @Autowired
        private Map<String, List<String>> loggedUsers;

        public void onApplicationEvent(SessionConnectEvent event) {

            StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

            String key = sha.getUser().getName();

            if (!loggedUsers.containsKey(key)) {
                loggedUsers.put(key, new CopyOnWriteArrayList<>());
            }

            loggedUsers.get(key).add(sha.getSessionId());

            LOGGER.debug("User {} Connected", sha.getSessionId());
        }
    }
	
    @Configuration
    public class WebSocketOnDisconnected implements ApplicationListener<SessionDisconnectEvent> {

        @Autowired
        private Map<String, List<String>> loggedUsers;
        
        @Override
        public void onApplicationEvent(SessionDisconnectEvent event) {

            StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
            
            String key = sha.getUser().getName();

            if (loggedUsers.containsKey(key)) {
                loggedUsers.get(key).remove(sha.getSessionId());
            }

            LOGGER.debug("User {} Disconnected", sha.getSessionId());
        }
    }
}