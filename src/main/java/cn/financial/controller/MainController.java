package cn.financial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;




/**
 * 控制页面跳转
 * @author gs
 *
 */
@Controller
public class MainController {
    /**
     * login
     * @param request
     * @param respons
     * @return
     */
    @RequestMapping(value="/login")
    public String login(HttpServletRequest request,HttpServletResponse respons ,Model model){
        //String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
        String error = null;
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(request.getParameter("name"),request.getParameter("pwd"));
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            System.out.println( "用户名/密码错误");
            error = "用户名/密码错误";  
        } catch (IncorrectCredentialsException e) { 
            System.out.println( "用户名/密码错误");
            error = "用户名/密码错误";  
        } catch (ExcessiveAttemptsException e) {  
            System.out.println("登录失败多次，账户锁定10分钟");
            error = "登录失败多次，账户锁定10分钟";  
        } catch (AuthenticationException e) {  
            System.out.println( "其他错误：" + e.getMessage());
            // 其他错误，比如锁定，如果想单独处理请单独catch处理  
            error = "其他错误：" + e.getMessage();  
        } 
        if (error != null) {// 出错了，返回登录页面  
            request.setAttribute("error", error);  
            return "/login";  
        } else {// 登录成功  
            return "/index";  
        } 
    }
    /**
     * index
     * @param request
     * @param respons
     * @return
     */
	@RequestMapping(value="/index")
    public ModelAndView index(HttpServletRequest request,HttpServletResponse respons){
    	ModelAndView andView=new ModelAndView();
    	andView.setViewName("index");
    	return andView;
    }
}
