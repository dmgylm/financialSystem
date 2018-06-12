package cn.financial.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.financial.model.RoleResource;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.RoleResourceService;
import cn.financial.service.UserRoleService;

/**
 * 控制页面跳转
 * @author gs
 *
 */
@Controller
public class MainController {
    
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleResourceService roleResourceService;
    
    @RequestMapping(value="/subLogin", produces = "application/json;charset=utf-8")
    public String getexcel(HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }
    /**
     * login验证
     * @param request
     * @param respons
     * @return
     */
    @RequestMapping(value="/login")
    public String login(HttpServletRequest request,HttpServletResponse respons ,Model model){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(request.getParameter("username"),request.getParameter("password"));
        try {
            subject.login(token);
        }catch (UnknownAccountException e) {
            System.out.println( "帐号不存在");
            model.addAttribute("msg","帐号不存在");
        }catch (IncorrectCredentialsException e) { 
            System.out.println( "用户名/密码错误");
            model.addAttribute("msg","用户名/密码错误"); 
        } catch (ExcessiveAttemptsException e) {  
            System.out.println("登录失败多次，账户锁定10分钟");
            model.addAttribute("msg","登录失败多次，账户锁定10分钟");
        } catch (AuthenticationException e) {  
            System.out.println( "其他错误：" + e.getMessage());
            // 其他错误，比如锁定，如果想单独处理请单独catch处理  
            model.addAttribute("msg","其他错误：" + e.getMessage());
        } 
        return "login"; 
    }
    /**
     * index(根据角色显示对应的功能权限)
     * @param request
     * @param respons
     * @return
     */
	@RequestMapping(value="/")
    public String index(HttpServletRequest request,HttpServletResponse respons,Model model){
	    User user = (User) request.getAttribute("user");
	    String userName = user.getName();
	    if(userName!=null && !"".equals(userName)){
	        List<UserRole> userRole = userRoleService.listUserRole(userName);//根据用户名查询对应角色信息
	        List<RoleResource> roleResource = new ArrayList<RoleResource>();
	        if(userRole.size()>0){ //CollectionUtils.isEmpty(userRole)
	            for(UserRole list:userRole){
	                roleResource = roleResourceService.listRoleResource(list.getrId());//根据角色id查询对应功能权限信息
	            }  
	        }
	        model.addAttribute("roleResource",roleResource);
	    }
    	return "index";
    }
}
