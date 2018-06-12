package cn.financial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.UserRole;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserRoleService;
import cn.financial.service.UserService;
import cn.financial.util.UuidUtil;
import cn.financial.util.shiro.PasswordHelper;

/**
 * 用户(用户角色关联表)(用户组织结构关联表)
 * @author gs
 * 2018/3/7
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserOrganizationService userOrganizationService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PasswordHelper passwordHelper;
    
    /*密码校验规则：
     * 由6-15位字符组成，组成内容必须包含（但不仅限于）：
     * 至少6个字符（最多15个字符）、大写与小写字母、至少一个数字，支持特殊符号，但不支持空格
     * */
    final String regEx = "(?!(^[A-Za-z]*$))(?!(^[0-9]*$))(?=(^.*[\\d].*$))(?=(^.*[a-z].*$))(?=(^.*[A-Z].*$))(?!(^.*[\\s].*$))^[0-9A-Za-z\\x21-\\x7e]{6,15}$";

    protected Logger logger = LoggerFactory.getLogger(UserController.class);
    
    /**
     * 修改密码
     * @param request
     * @param response
     */
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/passWord", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUserPwd(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String oldPwd = null, newPwd = null, userId = null;
        
        User newuser = (User) request.getAttribute("user");
        User users = userService.getUserById(newuser.getId());//查询当前登录用户密码,salt
        
        try{
            if(null!=request.getParameter("oldPwd") && !"".equals(request.getParameter("oldPwd"))){
                User userPwd = new User();
                userPwd.setPwd(request.getParameter("oldPwd"));
                userPwd.setSalt(users.getSalt());
                passwordHelper.encryptPassword(userPwd);
                oldPwd = userPwd.getPwd();//旧密码(页面传入)
            }
            if(null!=request.getParameter("newPwd") && !"".equals(request.getParameter("newPwd"))){
                newPwd = request.getParameter("newPwd");//新密码
            }
            if(null!=request.getParameter("userId") && !"".equals(request.getParameter("userId"))){
                userId = request.getParameter("userId");//用户id
            }
            if(newPwd.matches(regEx)){//密码规则校验
                if(oldPwd.equals(users.getPwd())) {//判断旧密码与原密码是否相等
                    if(oldPwd.equals(newPwd)){
                        dataMap.put("resultCode", 400);
                        dataMap.put("resultDesc", "新旧密码一致");
                    }else{
                        User user = new User();
                        user.setId(userId);
                        user.setPwd(newPwd);
                        user.setSalt(UuidUtil.getUUID());
                        Integer userList = userService.updateUser(user);
                        if(userList>0){
                            dataMap.put("resultCode", 200);
                            dataMap.put("resultDesc", "修改成功");
                        }else{
                            dataMap.put("resultCode", 400);
                            dataMap.put("resultDesc", "修改失败");
                        }
                    }
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "原密码输入错误");
                }
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "密码输入格式错误");
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    
    /**
     * 查询所有用户/多条件查询用户列表
     * @param request
     * @param response
     */
    //@RequiresRoles("超级管理员")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
    	    Map<Object, Object> map = new HashMap<>();
            if(null!=request.getParameter("name") && !"".equals(request.getParameter("name"))){
                map.put("name",  new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8"));//用户名
            }
            if(null!=request.getParameter("realName") && !"".equals(request.getParameter("realName"))){
                map.put("realName", new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8"));//真实姓名
            }
            if(null!=request.getParameter("userId") && !"".equals(request.getParameter("userId"))){
                map.put("id", request.getParameter("userId"));//用户id
            }
            /*if(null!=request.getParameter("pwd") && !"".equals(request.getParameter("pwd"))){
                map.put("pwd", request.getParameter("pwd"));//密码
            }*/
            if(null!=request.getParameter("jobNumber") && !"".equals(request.getParameter("jobNumber"))){
                map.put("jobNumber", request.getParameter("jobNumber"));//工号
            }
            if(null!=request.getParameter("status") && !"".equals(request.getParameter("status"))){
                map.put("status", request.getParameter("status"));//状态
            }
            if (null!=request.getParameter("createTime") && !"".equals(request.getParameter("createTime"))) {
                map.put("createTime", request.getParameter("createTime"));//创建时间
            }
            if (null!=request.getParameter("updateTime") && !"".equals(request.getParameter("updateTime"))) {
                map.put("updateTime", request.getParameter("updateTime"));//修改时间
            }
            Integer pageSize=0;
            if(request.getParameter("pageSize")!=null && !request.getParameter("pageSize").equals("")){
                pageSize=Integer.parseInt(request.getParameter("pageSize"));
                map.put("pageSize",pageSize);//条数
            }
            Integer start=0;
            if(request.getParameter("page")!=null && !request.getParameter("page").equals("")){
                start=pageSize * (Integer.parseInt(request.getParameter("page")) - 1);
                map.put("start",start);//页码
            }
            List<User> user = userService.listUser(map);//查询全部map为空
            dataMap.put("userList", user);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
    	return dataMap;
    }
    /**
     * 根据id查询
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping(value = "/userById", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUserById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String userId = null;
            if(null!=request.getParameter("userId") && !"".equals(request.getParameter("userId"))){
                userId = request.getParameter("userId");//用户id
            }
            User user = userService.getUserById(userId);
            dataMap.put("userById", user);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            e.printStackTrace();
        }
        return dataMap;
    }
    /**
     * 新增用户
     * @param request
     * @param response
     * @param name
     * @param pwd
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = null, realName = null, jobNumber = null;
            if(null!=request.getParameter("name") && !"".equals(request.getParameter("name"))){
                name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");//用户名
            }
            if(null!=request.getParameter("realName") && !"".equals(request.getParameter("realName"))){
                realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");//真实姓名
            }
            if(null!=request.getParameter("jobNumber") && !"".equals(request.getParameter("jobNumber"))){
                jobNumber = request.getParameter("jobNumber");//工号
            }
            Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
            if(flag>0){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名已存在,请重新命名");
            }else{
                User user = new User();
                user.setId(UuidUtil.getUUID());
                user.setSalt(UuidUtil.getUUID());
                user.setName(name);
                user.setRealName(realName);
                user.setPwd("Welcome1");//用户新增默认密码为Welcome1
                user.setJobNumber(jobNumber);
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
        return dataMap;
    }
    /**
     * 修改用户
     * @param request
     * @param response
     * @param name
     * @param pwd
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String userId = null, name = null, realName = null, pwd = null, jobNumber = null;
        try {
            if(null!=request.getParameter("name") && !"".equals(request.getParameter("name"))){
                name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");//用户名
            }
            if(null!=request.getParameter("realName") && !"".equals(request.getParameter("realName"))){
                realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");//真实姓名
            }
            if(null!=request.getParameter("userId") && !"".equals(request.getParameter("userId"))){
                userId = request.getParameter("userId");//用户id
            }
            if(null!=request.getParameter("pwd") && !"".equals(request.getParameter("pwd"))){
                pwd = request.getParameter("pwd");//密码
                if(!pwd.matches(regEx)){//密码规则校验
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "密码输入格式错误");
                    return dataMap;
                }
            }
            if(null!=request.getParameter("jobNumber") && !"".equals(request.getParameter("jobNumber"))){
                jobNumber = request.getParameter("jobNumber");//工号
            }
            User user = new User();
            user.setId(userId);
            user.setSalt(UuidUtil.getUUID());
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(pwd);
            user.setJobNumber(jobNumber);
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
        return dataMap;
    }
    /**
     * 删除用户
     * @param request
     * @param response
     * @param userId
     */
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String userId = null;
            if(null!=request.getParameter("userId") && !"".equals(request.getParameter("userId"))){
                userId = request.getParameter("userId");//用户id
            }
            Integer flag = userService.deleteUser(userId);//逻辑删除根据status状态判断0表示离职1表示在职
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
        return dataMap;
    }
    
    /**
     * 查询所有(用户组织结构关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"organization:view","permission:view"})
    @RequestMapping(value = "/userOrganizationIndex", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String uId = null;
            if(null!=request.getParameter("uId") && !"".equals(request.getParameter("uId"))){
                uId = request.getParameter("uId");//用户id
            }
            List<UserOrganization> userOrganization = userOrganizationService.listUserOrganization(uId);
            dataMap.put("userOrganizationList", userOrganization);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功"); 
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 新增(用户组织结构关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"organization:create","permission:create"})
    @RequestMapping(value = "/userOrganizationInsert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String orgId = null, uId = null;
            if(null!=request.getParameter("uId") && !"".equals(request.getParameter("uId"))){
                uId = request.getParameter("uId");//用户id
            }
            if (null!=request.getParameter("orgId") && !"".equals(request.getParameter("orgId"))) {
                orgId = request.getParameter("orgId");//组织结构id ,json格式数据
            }
            JSONArray sArray = JSON.parseArray(orgId);
            int userOrganizationList = 0;
            UserOrganization userOrganization = null;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String orgIdStr =(String)object.get("orgId");//获取key-orgId键值
                System.out.println("organizationId:==="+orgIdStr);
                if(orgIdStr!=null && !"".equals(orgIdStr)){
                    userOrganization = new UserOrganization();
                    userOrganization.setId(UuidUtil.getUUID());
                    userOrganization.setoId(orgIdStr);
                    userOrganization.setuId(uId);
                    userOrganizationList = userOrganizationService.insertUserOrganization(userOrganization);
                }
            }
            if(userOrganizationList>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "新增失败");
            } 
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 修改（先删除用户关联的组织架构信息，再重新添加该用户的组织架构信息）（根据用户id修改用户组织架构关联信息）
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions({"organization:update","permission:update"})
    @RequestMapping(value = "/userOrganizationUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String orgId = null, uId = null;
            if(null!=request.getParameter("uId") && !"".equals(request.getParameter("uId"))){
                uId = request.getParameter("uId");//用户id
            }
            if (null!=request.getParameter("orgId") && !"".equals(request.getParameter("orgId"))) {
                orgId = request.getParameter("orgId");//组织结构id ,json格式数据
            }
            int userOrgDelete = userOrganizationService.deleteUserOrganization(uId);//删除
            if(userOrgDelete > 0){
                JSONArray sArray = JSON.parseArray(orgId);
                int userOrganizationList = 0;
                UserOrganization userOrganization = null;
                for (int i = 0; i < sArray.size(); i++) {
                    JSONObject object = (JSONObject) sArray.get(i);
                    String orgIdStr =(String)object.get("orgId");//获取key-orgId键值
                    System.out.println("organizationId:==="+orgIdStr);
                    if(orgIdStr!=null && !"".equals(orgIdStr)){
                        userOrganization = new UserOrganization();
                        userOrganization.setId(UuidUtil.getUUID());
                        userOrganization.setoId(orgIdStr);
                        userOrganization.setuId(uId);
                        userOrganizationList = userOrganizationService.updateUserOrganization(userOrganization);//修改（新增数据）
                    }
                }
                if(userOrganizationList>0){
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "修改成功");
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "修改失败");
                }
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "修改失败");
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    
    /**
     * 查询所有(用户角色关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userRoleIndex", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = null;//用户名
            if (null!=request.getParameter("name") && !"".equals(request.getParameter("name"))) {
                name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            }
            List<UserRole> userRole = userRoleService.listUserRole(name);
            dataMap.put("userRoleList", userRole);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功");
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 新增(用户角色关联表)
     * @param request
     * @param response
     * @param uid
     * @param rid
     */
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/userRoleInsert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = null, uId = null;//roleId前台传入的数据是JSON格式
            if (null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))) {
                roleId = request.getParameter("roleId");//角色id
            }
            if(null!=request.getParameter("uId") && !"".equals(request.getParameter("uId"))){
                uId = request.getParameter("uId");//用户id
            }
            JSONArray sArray = JSON.parseArray(roleId);
            int userRoleList = 0;
            UserRole userRole = null; 
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String roleStr =(String)object.get("roleId");//获取key-roleId键值
                System.out.println("roleId:==="+roleStr);
                if(roleStr!=null && !"".equals(roleStr)){
                    userRole = new UserRole();
                    userRole.setId(UuidUtil.getUUID());
                    userRole.setrId(roleStr);
                    userRole.setuId(uId);
                    userRoleList = userRoleService.insertUserRole(userRole);
                }
            }  
            if(userRoleList>0){
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增成功");
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "新增失败");
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 修改(用户角色关联表)先删除用户关联的角色信息，再重新添加该用户的角色信息（根据用户id修改用户角色关联信息）
     * @param request
     * @param response
     * @param uid
     * @param rid
     * @param updateTime
     */
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/userRoleUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = null, uId = null;//roleId前台传入的数据是JSON格式
            if (null!=request.getParameter("roleId") && !"".equals(request.getParameter("roleId"))) {
                roleId = request.getParameter("roleId");//角色id
            }
            if(null!=request.getParameter("uId") && !"".equals(request.getParameter("uId"))){
                uId = request.getParameter("uId");//用户id
            }
            int userRoleDelete = userRoleService.deleteUserRole(uId);//删除
            if(userRoleDelete > 0){
                JSONArray sArray = JSON.parseArray(roleId);
                int userRoleList = 0;
                UserRole userRole = null; 
                for (int i = 0; i < sArray.size(); i++) {
                    JSONObject object = (JSONObject) sArray.get(i);
                    String roleStr =(String)object.get("roleId");//获取key-roleId键值
                    System.out.println("roleId:==="+roleStr);
                    if(roleStr!=null && !"".equals(roleStr)){
                        userRole = new UserRole();
                        userRole.setId(UuidUtil.getUUID());
                        userRole.setrId(roleStr);
                        userRole.setuId(uId);
                        userRoleList = userRoleService.updateUserRole(userRole);//修改（新增数据）
                    }
                }  
                if(userRoleList>0){
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "修改成功");
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "修改失败");
                }
            }else{
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "修改失败");
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
