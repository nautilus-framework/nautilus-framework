package thiagodnf.nautilus.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
	
	@Autowired
	private SimpMessageSendingOperations messaging;

	public void sendTitle(String sessionId, String text) {
		messaging.convertAndSend("/topic/optimize/title." + sessionId, text);
	}

	public void sendProgress(String sessionId, double progress) {
		messaging.convertAndSend("/topic/optimize/progress." + sessionId, progress);
	}

	public void sendDone(String sessionId, String id) {
		messaging.convertAndSend("/topic/optimize/done." + sessionId, id);
	}

	public void sendException(String sessionId, String message) {
		messaging.convertAndSend("/topic/optimize/exception." + sessionId, message);
	}
}
