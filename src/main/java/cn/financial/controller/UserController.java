package cn.financial.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.UuidUtil;

/**
 * 用户
 * @author gs
 * 2018/3/7
 */
@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;
    
    protected Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * 查询所有用户
     * @param request
     * @param respons
     */
    @RequestMapping(value = "/user/index")
    Map<String, Object> ListUser(HttpServletRequest request,HttpServletResponse respons){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<User> user = userService.listUser();
            dataMap.put("userList", user);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    	return dataMap;
    }
    /**
     * 新增用户
     * @param request
     * @param respons
     * @param name
     * @param pwd
     * @param privilege
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequestMapping(value = "/user/insert")
    Map<String, Object> insertUser(HttpServletRequest request,HttpServletResponse respons){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        Integer privilege = Integer.parseInt(request.getParameter("privilege"));
        String cTime = request.getParameter("createTime");
        String uTime = request.getParameter("updateTime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date createTime = formatter.parse(cTime);
            Date updateTime = formatter.parse(uTime);
            String oId = request.getParameter("oId");
            int flag = userService.listUserById(name);//查询用户名是否存在
            if(flag>0){
                System.out.println("用户名已存在");
            }else{
                int user = userService.insertUser(name, pwd, privilege, createTime, updateTime, oId);
                if(user>0){
                    dataMap.put("resultCode", 200);
                    dataMap.put("insertUser", "新增成功");
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("insertUser", "新增失败");
                }
            }
            
        } catch (ParseException e) {
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 修改用户
     * @param request
     * @param respons
     * @param name
     * @param pwd
     * @param privilege
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequestMapping(value = "/user/update")
    Map<String, Object> updateUser(HttpServletRequest request,HttpServletResponse respons){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String userId = request.getParameter("userId");
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        Integer privilege = Integer.parseInt(request.getParameter("privilege"));
        String cTime = request.getParameter("createTime");
        String uTime = request.getParameter("updateTime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date createTime = formatter.parse(cTime);
            Date updateTime = formatter.parse(uTime);
            String oId = request.getParameter("oId");
            int user = userService.updateUser(userId, name, pwd, privilege, createTime, updateTime, oId);
            if(user>0){
                dataMap.put("resultCode", 200);
                dataMap.put("updateUser", "修改成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("updateUser", "修改失败");
            }
        } catch (ParseException e) {
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 删除用户
     * @param request
     * @param respons
     * @param userId
     */
    @RequestMapping(value = "/user/delete")
    Map<String, Object> deleteUser(HttpServletRequest request,HttpServletResponse respons){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String userId = request.getParameter("userId");
        try {
            int user = userService.deleteUser(userId);
            if(user>0){
                dataMap.put("resultCode", 200);
                dataMap.put("deleteUser", "删除成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("deleteUser", "删除失败");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }  
        return dataMap;
    }
}
