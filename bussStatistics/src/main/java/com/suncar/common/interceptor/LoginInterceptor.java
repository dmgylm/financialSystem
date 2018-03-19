package com.suncar.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.suncar.common.util.FileUtil;

public class LoginInterceptor implements HandlerInterceptor {

	private static final String[] IGNORE_URI = { "/login.jsp", "/login",
			"/sysError.htm", ".js", ".css", ".jpg", ".png", ".ttf", ".woff",
			".swf" };

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean flag = false;
		String url = request.getRequestURL().toString();
		System.out.println(">>>: " + url);
		if (url.contains("/voice/getShowMessage") || url.contains("/main/sendSpeech") || url.contains("/user/screenControl")|| url.contains("/largescreen/largesc")|| url.contains("/voice/voiceList")) {
			flag = true;
		}else{
			for (String s : IGNORE_URI) {
				if (url.contains(s)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				Object user = request.getSession().getAttribute("username");
				if (user != null) {
					flag = true;
				} else {
					if (request.getHeader("x-requested-with") != null
							&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
						response.setHeader("sessionstatus", "timeout");
						response.setContentType("text/html; charset=UTF-8");
						PrintWriter out = response.getWriter();
						out.println();
					}
					response.sendRedirect(request.getContextPath() + "/user/login");
				}
			}
		}
		return flag;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
