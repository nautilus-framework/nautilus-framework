package thiagodnf.nautilus.web.websocket;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
public class WebSocketOnDisconnected implements ApplicationListener<SessionDisconnectEvent> {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketOnDisconnected.class);

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

		String sessionId = sha.getSessionId();

		logger.info("User {} Disconnected", sessionId);

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

		for (Thread t : threadSet) {
			if (t.getName().equalsIgnoreCase("optimizing-" + sessionId)) {
				t.interrupt();
			}

		}
	}
}
