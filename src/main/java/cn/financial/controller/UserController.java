package cn.financial.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;

import cn.financial.model.User;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.FileUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

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
     * 登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public void getUserName(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            String pwd = request.getParameter("pwd");
            Integer flag = userService.countUserName(name, pwd);
            if(flag>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功");
                request.getSession().setAttribute("username", name);
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名或密码不匹配");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 查询所有用户
     * @param request
     * @param response
     */
    @RequestMapping(value = "/user/index", method = RequestMethod.POST)
    public void listUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<User> user = userService.listUser();
            dataMap.put("userList", user);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
    	FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 根据id查询
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/userById", method = RequestMethod.POST)
    public void getUserById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            User user = userService.getUserById(request.getParameter("userId"));
            dataMap.put("userById", user);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            e.printStackTrace();
        }
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 新增用户
     * @param request
     * @param response
     * @param name
     * @param pwd
     * @param privilege
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequestMapping(value = "/user/insert", method = RequestMethod.POST)
    public void insertUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            String realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");
            String oId = request.getParameter("oId");
            Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
            if(flag>0){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名已存在");
            }else{
                User user = new User();
                user.setId(UuidUtil.getUUID());
                user.setName(name);
                user.setRealName(realName);
                user.setPwd(request.getParameter("pwd"));
                user.setJobNumber(request.getParameter("jobNumber"));
                user.setCreateTime(new Date());
                user.setoId(oId);
                int userList = userService.insertUser(user);
                if(userList>0){
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "新增成功");
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "新增失败");
                }
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 修改用户
     * @param request
     * @param response
     * @param name
     * @param pwd
     * @param privilege
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public void updateUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String userId = request.getParameter("userId");
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            String realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");
            String oId = request.getParameter("oId");
            User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(request.getParameter("pwd"));
            user.setJobNumber(request.getParameter("jobNumber"));
            user.setUpdateTime(new Date());
            user.setoId(oId);
            Integer userList = userService.updateUser(user);
            if(userList>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "修改成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "修改失败");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
    /**
     * 删除用户
     * @param request
     * @param response
     * @param userId
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public void deleteUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Integer flag = userService.deleteUser(request.getParameter("userId"));
            if(flag>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "删除失败");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }  
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
}
