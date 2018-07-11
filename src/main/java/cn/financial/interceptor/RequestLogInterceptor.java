package cn.financial.interceptor;

import java.util.Enumeration;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
public class RequestLogInterceptor implements HandlerInterceptor {
	
		private Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	        StringBuffer url = request.getRequestURL();
	    	@SuppressWarnings("unchecked")
			Enumeration<String> names = request.getParameterNames();
	    	StringBuffer params = new StringBuffer("?");
	    	while(names.hasMoreElements()){
	    		String name = names.nextElement();
	    		String value = request.getParameter(name);
	    		params.append(name).append("=").append(value).append("&");
	    	}
	    	logger.info("前端请求URL: " + url);
	    	logger.info("前端GET/POST请求参数: " + params.toString());
	    	String streamString = getInputStream(request);
	    	logger.info("前端InputStream请求参数: " + streamString);
	    	
	    	
	    	addCors(response);
			return true;
	    }
	    
	    private String getInputStream(HttpServletRequest request) {
			try {
				ServletInputStream is = request.getInputStream();
				StringBuilder content = new StringBuilder();
			    byte[] b = new byte[1024];
			    int lens = -1;
			    while ((lens = is.read(b)) > 0) {
			        content.append(new String(b, 0, lens,"UTF-8"));
			    }
			    return content.toString();
			} catch (Exception e) {
				logger.error("receive KHT Result ERROR", e);
				return null;
			}
		}
	    
		private void addCors(HttpServletResponse response) {
			response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	        response.setHeader("Access-Control-Max-Age", "3600");
	        response.setHeader("Access-Control-Allow-Headers"," Origin, X-Requested-With, Content-Type, Accept");
		}




		@Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	    }

		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
				Exception ex) throws Exception {
			
		}
	
}
