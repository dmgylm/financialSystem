package cn.financial.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.User;
import cn.financial.model.response.LoginResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 登录认证
 * @author gs
 *
 */
@Controller
@Api(value="登录/登出controller",tags={"登录/登出操作接口"})
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    @Autowired
    private UserOrganizationService userOrganizationService;
    
    @RequestMapping(value="/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String userName = "aa";
        String passWord = "12345aA";
        
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,passWord);   
        try {
            subject.login(token);
            return "redirect:/docTest";
        }catch (UnknownAccountException e) {
            System.out.println( "该用户不存在");
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_NO_USER));
        }catch (IncorrectCredentialsException e) { 
            System.out.println( "密码或账户错误，请重新输入");
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_USERNAME_ERROR));
        } catch (ExcessiveAttemptsException e) {  
            System.out.println("账户已锁，请联系管理员");
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_USER_LOCKOUT));
        } catch (AuthenticationException e) {  
            System.out.println( "其他错误：" + e.getMessage());
            // 其他错误，比如锁定，如果想单独处理请单独catch处理  
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_FAILURE));
        }
        return "redirect:/test"; 
    }
    
    @RequestMapping(value="/docTest", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getTest(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/doc.html";
    }
    
    /**
     * 跳转登录页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/subLogin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getexcel(HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }
    /**
     * login验证
     * @param request
     * @param respons
     * @return
     */
    public static String key;
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ApiOperation(value="登录认证",notes="返回用户关联功能权限信息", response = LoginResult.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="username",value="用户名",dataType="string", paramType = "query",  required = true),
        @ApiImplicitParam(name="password",value="密码",dataType="string", paramType = "query",  required = true)})
    @ResponseBody
    public LoginResult login(String username, String password){
        LoginResult result = new LoginResult();
        try {
            if(username == null || username.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USERNAME_NULL_ERROR, result);
                return result;
            }
            if(password == null || password.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.PASSWORD_NULL_ERROR, result);
                return result;
            }
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);   
            subject.login(token);
            if(password.equals(User.INITIALCIPHER)){//判断密码是否为Welcome1
                ElementXMLUtils.returnValue(ElementConfig.RESET_PWD, result);
                return result;
            }
            User user = userService.getUserByName(username);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String expreTime = sf.format(c.getTime())+" 00:00:00";
            if(expreTime.equals(user.getExpreTime())){//判断密码是否到期
                ElementXMLUtils.returnValue(ElementConfig.PASSWORD_INVALID_ERROR, result);
                return result;
            }
            System.out.println("~~~session:"+subject.getSession().getId());
            List<JSONObject> jsonObject = roleResourceService.roleResourceList(username);
            List<JSONObject> jsonOrg = userOrganizationService.userOrganizationList(user.getId());
            result.setRoleResource(jsonObject);
            result.setUserOrganization(jsonOrg);
            result.setSessionId(subject.getSession().getId()+"");
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
        }catch (UnknownAccountException e) {
            System.out.println( "该用户不存在");
            ElementXMLUtils.returnValue(ElementConfig.LOGIN_NO_USER, result);
        }catch (IncorrectCredentialsException e) { 
            System.out.println( "密码或账户错误，请重新输入");
            ElementXMLUtils.returnValue(ElementConfig.LOGIN_USERNAME_ERROR, result);
        } catch (ExcessiveAttemptsException e) {  
            System.out.println("账户已锁，请联系管理员");
            ElementXMLUtils.returnValue(ElementConfig.LOGIN_USER_LOCKOUT, result);
        } catch (Exception e) {  
            System.out.println( "其他错误：" + e.getMessage());
            // 其他错误，比如锁定，如果想单独处理请单独catch处理  
            ElementXMLUtils.returnValue(ElementConfig.LOGIN_FAILURE, result);
        }
        return result; 
    }
    /**
     * 登出
     * @return
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})  
    @ApiOperation(value="登出",notes="登出", response = ResultUtils.class)
    @ResponseBody  
    public ResultUtils logout() {  
        ResultUtils result = new ResultUtils();
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout(); 
        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
        return result;  
    } 

}