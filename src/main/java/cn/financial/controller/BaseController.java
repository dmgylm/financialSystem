package cn.financial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseController {
    /**
     * 登录认证异常
     */
    /*@ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public String authenticationException(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("url*********"+req.getRequestURL());
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        // 设置响应编码
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.sendError(402,"登录认证异常,请重新登录!");
        return null;
    }*/
    
    /**
     * 权限异常
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    @ResponseBody
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("url*********"+req.getRequestURL());
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        // 设置响应编码
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.sendError(403,"您没有访问此方法的权限!");
        return null;
    }
}
