package cn.financial.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class FinancialSocketHandler implements WebSocketHandler {
	
	
	private static final Logger logger;
	
	private static  ArrayList<WebSocketSession> users;
	
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
	public void afterConnectionClosed(String session,CloseStatus closeStatus) throws Exception {
		 
	        Iterator<WebSocketSession> iterator = users.iterator();
	        while(iterator.hasNext()){
	        	WebSocketSession user = iterator.next();
	        	if(user.toString().substring(user.toString().lastIndexOf("/")+1).substring(0,user.toString().substring(user.toString().lastIndexOf("/")+1).lastIndexOf(";")).equals(session)) {
	        		 iterator.remove();   //注意这个地方
					System.out.println("removeWebSocket:"+user);
				}
        }
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
			System.out.println("sub:  "+user.toString().substring(user.toString().lastIndexOf("/")+1).substring(0,user.toString().substring(user.toString().lastIndexOf("/")+1).lastIndexOf(";")));
			if(user.toString().substring(user.toString().lastIndexOf(";")+1).equals(userName)) {
				System.out.println("websocket userName"+userName);
				try {
					if(user.isOpen()) {
						synchronized (user) {
							user.sendMessage(message);
							System.out.println("发送消息成功"+message+unread);
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(user.toString().substring(user.toString().lastIndexOf("/")+1).substring(0,user.toString().substring(user.toString().lastIndexOf("/")+1).lastIndexOf(";")).equals(userName)) {
				try {
					if(user.isOpen()) {
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

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub
		
	}

}