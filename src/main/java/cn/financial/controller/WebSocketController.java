package cn.financial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;

import cn.financial.webSocket.FinancialSocketHandler;

@Controller
@RequestMapping("/meSocket")
public class WebSocketController {
	
	@Bean
	public FinancialSocketHandler financialWebSocketHandler() {
		
		return new FinancialSocketHandler();
		
	}
    @RequestMapping(value = "/sendMesss")
    public void sendSocketInfo(String unread) {
    	
    	JSONObject jsonObject = new JSONObject();
        jsonObject.put("unread",unread);
        financialWebSocketHandler().sendMessageToUser("MessageSocketServerInfo", new TextMessage(jsonObject.toString()));
    	
    }
    
    @RequestMapping(value = "/send")
	public ModelAndView send(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView andView = new ModelAndView();
		andView.setViewName("send");
		return andView;
	}

}
