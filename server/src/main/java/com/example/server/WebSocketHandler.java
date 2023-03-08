package com.example.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler{
    Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
	@Autowired
	TaskController taskController;
    
    @Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		logger.info("Message received:" + message.getPayload());
		long id = Long.valueOf(message.getPayload());
		session.sendMessage(new TextMessage("Task progress: "+ taskController.getProgress(id)));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
	}
}
