package cn.financial.util.shiro;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionManager extends DefaultWebSessionManager{
	
	private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
	
	private String authorization = "JSESSIONID";  

	@Override
	protected Serializable getSessionId(ServletRequest request,
			ServletResponse response) {
		return getReferencedSessionId(request, response);
	}
	
	private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response) {

        String id = getSessionIdCookieValue(request, response);
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
        } else {
            //not in a cookie, or cookie is disabled - try the request URI as a fallback (i.e. due to URL rewriting):

            //try the URI path segment parameters first:
            id = getUriPathSegmentParamValue(request, ShiroHttpSession.DEFAULT_SESSION_ID_NAME);

            if (id == null) {
            	 id = WebUtils.toHttp(request).getHeader(this.authorization);  
            	if(id == null) {
            		//not a URI path segment parameter, try the query parameters:
            		String name = getSessionIdName();
            		id = request.getParameter(name);
            		if (id == null) {
            			//try lowercase:
            			id = request.getParameter(name.toLowerCase());
            		}
            	}
            }
            if (id != null) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                        ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            }
        }
        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            //automatically mark it valid here.  If it is invalid, the
            //onUnknownSession method below will be invoked and we'll remove the attribute at that time.
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return id;
    }
	
	private String getSessionIdCookieValue(ServletRequest request, ServletResponse response) {
        if (!isSessionIdCookieEnabled()) {
            log.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        }
        if (!(request instanceof HttpServletRequest)) {
            log.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
    }
	
	 //SHIRO-351
    //also see http://cdivilly.wordpress.com/2011/04/22/java-servlets-uri-parameters/
    //since 1.2.2
    private String getUriPathSegmentParamValue(ServletRequest servletRequest, String paramName) {

        if (!(servletRequest instanceof HttpServletRequest)) {
            return null;
        }
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String uri = request.getRequestURI();
        if (uri == null) {
            return null;
        }

        int queryStartIndex = uri.indexOf('?');
        if (queryStartIndex >= 0) { //get rid of the query string
            uri = uri.substring(0, queryStartIndex);
        }

        int index = uri.indexOf(';'); //now check for path segment parameters:
        if (index < 0) {
            //no path segment params - return:
            return null;
        }

        //there are path segment params, let's get the last one that may exist:

        final String TOKEN = paramName + "=";

        uri = uri.substring(index+1); //uri now contains only the path segment params

        //we only care about the last JSESSIONID param:
        index = uri.lastIndexOf(TOKEN);
        if (index < 0) {
            //no segment param:
            return null;
        }

        uri = uri.substring(index + TOKEN.length());

        index = uri.indexOf(';'); //strip off any remaining segment params:
        if(index >= 0) {
            uri = uri.substring(0, index);
        }

        return uri; //what remains is the value
    }
    
  //since 1.2.1
    private String getSessionIdName() {
        String name = this.getSessionIdCookie() != null ? this.getSessionIdCookie().getName() : null;
        if (name == null) {
            name = ShiroHttpSession.DEFAULT_SESSION_ID_NAME;
        }
        return name;
    }

}
