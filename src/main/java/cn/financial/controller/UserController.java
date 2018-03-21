package cn.financial.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.UserRole;
import cn.financial.service.impl.UserOrganizationServiceImpl;
import cn.financial.service.impl.UserRoleServiceImpl;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.UuidUtil;

/**
 * 用户(用户角色关联表)(用户组织结构关联表)
 * @author gs
 * 2018/3/7
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    UserOrganizationServiceImpl userOrganizationService;
    @Autowired
    UserRoleServiceImpl userRoleService;
    
    protected Logger logger = LoggerFactory.getLogger(UserController.class);
    
    /**
     * 修改密码
     * @param request
     * @param response
     */
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/passWord", method = RequestMethod.POST)
    public Map<String, Object> getUserPwd(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        try{
            if(oldPwd.equals(newPwd)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "新旧密码一致");
            }else{
                User user = new User();
                user.setId(request.getParameter("userId"));
                user.setPwd(newPwd);
                Integer userList = userService.updateUser(user);
                if(userList>0){
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "修改成功");
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "修改失败");
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
     * 查询所有用户/多条件查询用户列表
     * @param request
     * @param response
     */
    @RequiresPermissions("user:view")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public Map<String, Object> listUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    	    String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            String realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");
            String createTime = request.getParameter("createTime");
            String updateTime = request.getParameter("updateTime");
            Date createTimeOfDate = null;
            Date updateTimeOfDate = null;
            if (createTime != null && !"".equals(createTime)) {
                createTimeOfDate = dateFormat.parse(createTime);
            }
            if (updateTime != null && !"".equals(updateTime)) {
                updateTimeOfDate = dateFormat.parse(updateTime);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("id", request.getParameter("userId"));
            map.put("name", name);
            map.put("realName", realName);
            map.put("pwd",request.getParameter("pwd"));
            map.put("jobNumber", request.getParameter("jobNumber"));
            map.put("createTime", createTimeOfDate);
            map.put("updateTime", updateTimeOfDate);
            List<User> user = userService.listUser(map);
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
    @RequiresPermissions("user:view")
    @RequestMapping(value = "/userById", method = RequestMethod.POST)
    public Map<String, Object> getUserById(HttpServletRequest request,HttpServletResponse response){
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
    @RequiresPermissions("user:create")
    @RequestMapping(value = "/insert")
    public Map<String, Object> insertUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            String realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");
            Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
            if(flag>0){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名已存在");
            }else{
                User user = new User();
                user.setId(UuidUtil.getUUID());
                user.setName(name);
                user.setRealName(realName);
                user.setPwd(name);//用户新增默认密码为用户名
                user.setJobNumber(request.getParameter("jobNumber"));
                user.setCreateTime(new Date());
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
    public Map<String, Object> updateUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String userId = request.getParameter("userId");
            String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
            String realName = new String(request.getParameter("realName").getBytes("ISO-8859-1"), "UTF-8");
            User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(request.getParameter("pwd"));
            user.setJobNumber(request.getParameter("jobNumber"));
            user.setUpdateTime(new Date());
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
    public Map<String, Object> deleteUser(HttpServletRequest request,HttpServletResponse response){
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
        return dataMap;
    }
    
    /**
     * 查询所有(用户组织结构关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"user:view","organization:view"})
    @RequestMapping(value = "/userOrganization/index", method = RequestMethod.POST)
    public Map<String, Object> listUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<UserOrganization> userOrganization = userOrganizationService.listUserOrganization("uId");
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
    @RequiresPermissions({"user:create","organization:create"})
    @RequestMapping(value = "/userOrganization/insert", method = RequestMethod.POST)
    public Map<String, Object> insertUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            UserOrganization userOrganization = new UserOrganization();
            userOrganization.setId(UuidUtil.getUUID());
            userOrganization.setoId(request.getParameter("oId"));
            userOrganization.setuId(request.getParameter("uId"));
            userOrganization.setCreateTime(new Date());
            int userOrganizationList = userOrganizationService.insertUserOrganization(userOrganization);
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
     * 查询所有(用户角色关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"user:view","role:view"})
    @RequestMapping(value = "/userRole/index", method = RequestMethod.POST)
    public Map<String, Object> listUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<UserRole> userRole = userRoleService.listUserRole(request.getParameter("name"));
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
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions({"user:create","role:create"})
    @RequestMapping(value = "/userRole/insert", method = RequestMethod.POST)
    public Map<String, Object> insertUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            UserRole userRole = new UserRole();
            userRole.setId(UuidUtil.getUUID());
            userRole.setuId(request.getParameter("uId"));
            userRole.setrId(request.getParameter("rId"));
            userRole.setCreateTime(new Date());
            int userRoleList = userRoleService.insertUserRole(userRole);
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
}
