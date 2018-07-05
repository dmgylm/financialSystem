package cn.financial.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import cn.financial.model.RoleResource;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.UserRoleService;
import cn.financial.service.UserService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;

/**
 * 登录认证
 * @author gs
 *
 */
@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    /**
     * 跳转登录页面
     * @param request
     * @param response
     * @return
     */
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
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String userName = request.getParameter("username");
        String passWord = request.getParameter("password");
        if(userName == null || userName.equals("")){
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.USERNAME_NULL_ERROR));
            return dataMap;
        }
        if(passWord == null || passWord.equals("")){
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.PASSWORD_NULL_ERROR));
            return dataMap;
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,passWord);   
        try {
            subject.login(token);
            //判断密码是否为Welcome1
            if(passWord.equals("Welcome1")){
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RESET_PWD));
                return dataMap;
            }
            User user = userService.getUserByName(userName);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String expreTime = sf.format(c.getTime());
            Date date = sf.parse(user.getExpreTime());
            if(sf.format(date).equals(expreTime)){//判断密码是否到期
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.PASSWORD_INVALID_ERROR));
                return dataMap;
            }
            List<JSONObject> jsonObject = roleResourceService.roleResourceList(userName);
            dataMap.put("roleResource", jsonObject);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
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
        } catch (ParseException e) {
            System.out.println( "其他错误：" + e.getMessage());
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_FAILURE));
        }
        return dataMap; 
    }
    /**
     * 登出
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)  
    @ResponseBody  
    public Map<String, Object> logout() {  
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Subject currentUser = SecurityUtils.getSubject();
        dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        currentUser.logout();  
        return dataMap;  
    } 
}