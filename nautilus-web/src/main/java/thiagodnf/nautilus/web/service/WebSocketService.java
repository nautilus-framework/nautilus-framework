package thiagodnf.nautilus.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);
	
	@Autowired
	private SimpMessageSendingOperations messaging;

	public void sendTitle(String sessionId, String text) {
		LOGGER.info(text);
		messaging.convertAndSend("/topic/optimize/title." + sessionId, text);
	}

	public void sendProgress(String sessionId, double progress) {
		messaging.convertAndSend("/topic/optimize/progress." + sessionId, progress);
	}

	public void sendDone(String sessionId, String id) {
		LOGGER.info("Done");
		messaging.convertAndSend("/topic/optimize/done." + sessionId, id);
	}

	public void sendException(String sessionId, String message) {
		LOGGER.info(message);
		messaging.convertAndSend("/topic/optimize/exception." + sessionId, message);
	}
}
