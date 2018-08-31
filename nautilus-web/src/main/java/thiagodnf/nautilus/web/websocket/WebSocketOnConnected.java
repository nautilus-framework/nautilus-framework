package thiagodnf.nautilus.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Configuration
public class WebSocketOnConnected implements ApplicationListener<SessionConnectEvent> {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketOnConnected.class);

	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

		logger.info("User {} Connected", sha.getSessionId());
	}

}
