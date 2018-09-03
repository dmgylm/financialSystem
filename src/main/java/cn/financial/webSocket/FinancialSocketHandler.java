package cn.financial.webSocket;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class FinancialSocketHandler implements WebSocketHandler {
	
	
	private static final Logger logger;
	
	private static final ArrayList<WebSocketSession> users;
	
	static {
        users = new ArrayList<WebSocketSession>();
        logger = Logger.getLogger(FinancialSocketHandler.class);
    }
	
	/**
	 * 链接成功时触发
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("connect to the websocket success......");
		users.add(session);
		System.out.println("wenSocketSession"+session);
		
	}
	
	/**
	 * 关闭链接时触发
	 */
	public void afterConnectionClosed(WebSocketSession session,CloseStatus closeStatus) throws Exception {
		logger.info("websocket connection closed......");
		users.remove(session);
	}
	
	/**
	 * js调用websocket.send时候，会调用该方法
	 */
	public void handleMessage(WebSocketSession session,WebSocketMessage<?> socketMessage) throws Exception {
		logger.info("进入send实现类中，传回数据");
	}
	
	public void handleTransportError(WebSocketSession session,Throwable throwable) throws Exception {
		logger.info("websocket connection closed......");
		users.remove(session);
	}
	
	public boolean supportsPartialMessages() {
		return false;
	}
	
	/**
	 * 给某个用户发送消息
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userName,TextMessage message,String unread) {
		for(WebSocketSession user : users) {
			if(user.toString().substring(user.toString().lastIndexOf("/")+1).equals(userName)) {
				try {
					if(user.isOpen()) {
//						user.getUri().toString().substring(user.getUri().toString().lastIndexOf("/")+1).substring(0,user.getUri().toString().substring(user.getUri().toString().lastIndexOf("/")+1).indexOf(";")).equals(userName)
						System.out.println(user.toString());
//						System.out.println(user.toString().substring(user.toString().lastIndexOf("=")+1).substring(0,user.toString().substring(user.toString().lastIndexOf("=")+1).indexOf("]")));
						synchronized (user) {
							user.sendMessage(message);
							System.out.println("发送消息成功"+message+unread);
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 给所有在线用户发送消息
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
		
		for(WebSocketSession user : users) {
			try {
				if(user.isOpen()) {
					user.sendMessage(message);
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}