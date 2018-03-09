package cn.financial.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
public class LoginInterceptor implements HandlerInterceptor {

	 private static final String[] IGNORE_URI = {"/login.jsp","/login", "/sysError.htm", ".js", ".css",".jpg",".png",".ttf",".woff",".swf"};
	 
	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	        boolean flag = false;
	        String url = request.getRequestURL().toString();
	        System.out.println(">>>: " + url);
	        for (String s : IGNORE_URI) {
	            if (url.contains(s)) {
	                flag = true;
	                break;
	            }
	        }
	        if (!flag) {
	           Object user=request.getSession().getAttribute("userName");
	            if (user != null) {
	            	flag = true;
	            }else{
	            	response.sendRedirect(request.getContextPath()+"/login");
	            }
	        }
			return flag;
	    }
	 
	    @Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	    }

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
				Exception ex) throws Exception {
			
		}
	
}
