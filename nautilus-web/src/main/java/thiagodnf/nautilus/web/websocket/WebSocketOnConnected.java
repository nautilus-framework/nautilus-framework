package thiagodnf.nautilus.web.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Configuration
public class WebSocketOnConnected implements ApplicationListener<SessionConnectEvent>{
	
	public void onApplicationEvent(SessionConnectEvent event) {
	    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

	    
	    System.out.println("Connect event [sessionId: " + sha.getSessionId());
	}

}
