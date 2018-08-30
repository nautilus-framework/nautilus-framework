package thiagodnf.nautilus.web.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
public class WebSocketOnDisconnected implements ApplicationListener<SessionDisconnectEvent> {

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

		System.out.println("Disconnected event [sessionId: " + sha.getSessionId());

	}
}
