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

import cn.financial.model.Organization;
import cn.financial.model.Role;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.UserRole;
import cn.financial.service.OrganizationService;
import cn.financial.service.RoleService;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserRoleService;
import cn.financial.service.UserService;
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
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserOrganizationService userOrganizationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    
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
        String userId = request.getParameter("userId");
        
        User newuser = (User) request.getAttribute("user");
        String pwd = userService.getUserById(newuser.getId()).getPwd();
        
        try{
            if(oldPwd.equals(pwd)) {//判断旧密码与原密码是否相等
            	if(oldPwd == null || "".equals(oldPwd)){
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "旧密码为空");
                }else if(newPwd == null || "".equals(newPwd)){
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "新密码为空");
                }else if(userId == null || "".equals(userId)){
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "用户id为空");
                }else{
                    if(oldPwd.equals(newPwd)){
                        dataMap.put("resultCode", 400);
                        dataMap.put("resultDesc", "新旧密码一致");
                    }else{
                        User user = new User();
                        user.setId(userId);
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
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public Map<String, Object> listUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    	    String name = new String(request.getParameter("name"));
            String realName = new String(request.getParameter("realName"));
            String userId = request.getParameter("userId");
            String pwd = request.getParameter("pwd");
            String jobNumber = request.getParameter("jobNumber");
            String createTime = request.getParameter("createTime");
            String updateTime = request.getParameter("updateTime");
            Date createTimeOfDate = null;
            Date updateTimeOfDate = null;
            if(name == null || "".equals(name)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名为空");
            }else if(realName == null || "".equals(realName)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "真实姓名为空");
            }else if(userId == null || "".equals(userId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空");
            }else if(pwd == null || "".equals(pwd)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户密码为空");
            }else if(jobNumber == null || "".equals(jobNumber)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "工号为空");
            }else if (createTime == null && "".equals(createTime)) {
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "创建时间为空");
            }else if (updateTime == null && "".equals(updateTime)) {
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "修改时间为空");
            }else{
                name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
                realName = new String(realName.getBytes("ISO-8859-1"), "UTF-8");
                createTimeOfDate = dateFormat.parse(createTime);
                updateTimeOfDate = dateFormat.parse(updateTime);
                Map<Object, Object> map = new HashMap<>();
                map.put("id", userId);
                map.put("name", name);
                map.put("realName", realName);
                map.put("pwd", pwd);
                map.put("jobNumber", jobNumber);
                map.put("createTime", createTimeOfDate);
                map.put("updateTime", updateTimeOfDate);
                List<User> user = userService.listUser(map);
                dataMap.put("userList", user);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功");
            }
            
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
    public Map<String, Object> getUserById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String userId = request.getParameter("userId");
            if(userId == null || "".equals(userId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空");
            }else{
                User user = userService.getUserById(userId);
                dataMap.put("userById", user);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功");
            }
            
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
    @RequestMapping(value = "/insert")
    public Map<String, Object> insertUser(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = new String(request.getParameter("name"));
            String realName = new String(request.getParameter("realName"));
            String jobNumber = request.getParameter("jobNumber");
            if(name == null || "".equals(name)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名为空");
            }else if(realName == null || "".equals(realName)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "真实姓名为空");
            }else if(jobNumber == null || "".equals(jobNumber)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "工号为空");
            }else{
                Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
                if(flag>0){
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "用户名已存在");
                }else{
                    name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
                    realName = new String(realName.getBytes("ISO-8859-1"), "UTF-8");
                    User user = new User();
                    user.setId(UuidUtil.getUUID());
                    user.setName(name);
                    user.setRealName(realName);
                    user.setPwd(name);//用户新增默认密码为用户名
                    user.setJobNumber(jobNumber);
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
            String name = new String(request.getParameter("name"));
            String realName = new String(request.getParameter("realName"));
            String pwd = request.getParameter("pwd");
            String jobNumber = request.getParameter("jobNumber");
            if(name == null || "".equals(name)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名为空");
            }else if(realName == null || "".equals(realName)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "真实姓名为空");
            }else if(userId == null || "".equals(userId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空");
            }else if(pwd == null || "".equals(pwd)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户密码为空");
            }else if(jobNumber == null || "".equals(jobNumber)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "工号为空");
            }else{
                name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
                realName = new String(realName.getBytes("ISO-8859-1"), "UTF-8");
                User user = new User();
                user.setId(userId);
                user.setName(name);
                user.setRealName(realName);
                user.setPwd(pwd);
                user.setJobNumber(jobNumber);
                user.setUpdateTime(new Date());
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
            String userId = request.getParameter("userId");
            if(userId == null || "".equals(userId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空");
            }else{
                Integer flag = userService.deleteUser(userId);
                if(flag>0){
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "删除成功");
                }else{
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "删除失败");
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
     * 查询所有(用户组织结构关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/userOrganizationIndex", method = RequestMethod.POST)
    public Map<String, Object> listUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String uId = request.getParameter("uId");
            if(uId == null || "".equals(uId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空"); 
            }else{
                List<UserOrganization> userOrganization = userOrganizationService.listUserOrganization(uId);
                dataMap.put("userOrganizationList", userOrganization);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功"); 
            }
            
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
    @RequiresPermissions("organization:create")
    @RequestMapping(value = "/userOrganizationInsert", method = RequestMethod.POST)
    public Map<String, Object> insertUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String orgName = request.getParameter("orgName");
            String uId = request.getParameter("uId");
            if(uId == null || "".equals(uId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空"); 
            }else if (orgName == null || "".equals(orgName)) {
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "组织结构名为空"); 
            }else{
                orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
                Map<Object, Object> map = new HashMap<>();
                map.put("orgName", orgName);// 根据组织架构名查询id
                List<Organization> list = organizationService.listOrganizationBy(map);
                if(list.size()>0){
                    UserOrganization userOrganization = new UserOrganization();
                    userOrganization.setId(UuidUtil.getUUID());
                    for(Organization organization:list){
                        userOrganization.setoId(organization.getId());
                    }
                    userOrganization.setuId(uId);
                    userOrganization.setCreateTime(new Date());
                    int userOrganizationList = userOrganizationService.insertUserOrganization(userOrganization);
                    if(userOrganizationList>0){
                        dataMap.put("resultCode", 200);
                        dataMap.put("resultDesc", "新增成功");
                    }else{
                        dataMap.put("resultCode", 400);
                        dataMap.put("resultDesc", "新增失败");
                    }
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
     * 查询所有(用户角色关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userRoleIndex", method = RequestMethod.POST)
    public Map<String, Object> listUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String name = request.getParameter("name");//用户名
            if (name == null || "".equals(name)) {
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户名为空"); 
            }else{
                name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
                List<UserRole> userRole = userRoleService.listUserRole(name);
                dataMap.put("userRoleList", userRole);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功");
            }
            
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
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/userRoleInsert", method = RequestMethod.POST)
    public Map<String, Object> insertUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = request.getParameter("roleName");
            String uId = request.getParameter("uId");
            if (roleName == null || "".equals(roleName)) {
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "角色名为空"); 
            }else if(uId == null || "".equals(uId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "用户id为空"); 
            }else{
                roleName = new String(roleName.getBytes("ISO-8859-1"), "UTF-8");
                List<Role> roleList= roleService.listRole(roleName);//根据roleName查询角色id
                if(roleList.size()>0){
                    UserRole userRole = new UserRole();
                    userRole.setId(UuidUtil.getUUID());
                    for(Role list:roleList){
                        userRole.setrId(list.getId());
                    }
                    userRole.setuId(uId);
                    userRole.setCreateTime(new Date());
                    int userRoleList = userRoleService.insertUserRole(userRole);
                    if(userRoleList>0){
                        dataMap.put("resultCode", 200);
                        dataMap.put("resultDesc", "新增成功");
                    }else{
                        dataMap.put("resultCode", 400);
                        dataMap.put("resultDesc", "新增失败");
                    }
                }
            }
            
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
