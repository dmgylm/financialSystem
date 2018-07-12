package cn.financial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.UserRole;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.UserInfo;
import cn.financial.model.response.UserOrganizationResult;
import cn.financial.model.response.UserResetPwd;
import cn.financial.model.response.UserResult;
import cn.financial.model.response.UserRoleResult;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserRoleService;
import cn.financial.service.UserService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.UuidUtil;
import cn.financial.util.shiro.PasswordHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 用户(用户角色关联表)(用户组织结构关联表)
 * @author gs
 * 2018/3/7
 */
@Controller
@Api(value="用户controller",tags={"用户操作接口"})
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
    @Autowired
    @Qualifier("apiRedisTemplate")
    private RedisTemplate redis;

    protected Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 查询所有用户/多条件查询用户列表
     * @param request
     * @param response
     */
    //@RequiresRoles("超级管理员")
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ApiOperation(value="查询用户信息",notes="查询所有用户/多条件查询用户列表",response = UserResult.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="status",value="状态",dataType="Integer", paramType = "query"),
        @ApiImplicitParam(name="createTime",value="创建时间",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="updateTime",value="更新时间",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="pageSize",value="条数",dataType="integer", paramType = "query", required = true),
        @ApiImplicitParam(name="page",value="页码",dataType="integer", paramType = "query", required = true)})
    @ApiResponse(code = 200, message = "成功")
    @ResponseBody
    public UserResult listUser(HttpServletRequest request, String name, String realName, String jobNumber,String userId,
            String createTime, String updateTime, Integer status, Integer pageSize, Integer page){
        UserResult result = new UserResult();
    	try {
    	    Map<Object, Object> map = new HashMap<>();
    	    map.put("name", name);//用户名
    	    map.put("realName", realName);//真实姓名
    	    map.put("id", userId);//用户id
    	    map.put("jobNumber", jobNumber);//工号
    	    map.put("status", request.getParameter("status"));//状态
    	    map.put("createTime", createTime);//创建时间
    	    map.put("updateTime", request.getParameter("updateTime"));//修改时间
    	    map.put("pageSize",pageSize);//条数
    	    map.put("start", page);//页码
            List<User> user = userService.listUser(map);//查询全部map为空
            List<User> userList = userService.listUserCount(map);//查询总条数
            result.setUserList(user);
            result.setUserListTotal(userList.size());
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
            this.logger.error(e.getMessage(), e);
        }
    	return result;
    }
    /**
     * 根据id查询
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userById", method = RequestMethod.POST)
    @ApiOperation(value="根据id查询用户信息",notes="根据id查询用户信息",response = UserInfo.class)
    @ApiImplicitParams({@ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public UserInfo getUserById(HttpServletRequest request, String userId){
        UserInfo result = new UserInfo();
        try {
            User user = userService.getUserById(userId);
            if(user == null){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL,result);
                return result;
            }
            result.setUserById(user);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            e.printStackTrace();
        }
        return result;
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
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增用户信息",notes="新增用户信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="用户名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertUser(HttpServletRequest request,String name, String realName, String jobNumber){
        ResultUtils result = new ResultUtils();
        try {
            Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
            Integer jobNumberFlag = userService.countUserName("", jobNumber);//查询工号是否存在
            if(flag == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_NAME_NULL, result);
                return result;
            }
            if(flag == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_JOBNUMBER_NULL, result);
                return result;
            }
            if(flag > 0){
                ElementXMLUtils.returnValue(ElementConfig.USERNAME_EXISTENCE, result);
                return result;
            }
            if(jobNumberFlag > 0){
                ElementXMLUtils.returnValue(ElementConfig.JOBNUMBER_EXISTENCE, result);
                return result;
            }
            User user = new User();
            user.setId(UuidUtil.getUUID());
            user.setSalt(UuidUtil.getUUID());
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(User.INITIALCIPHER);//用户新增默认密码为Welcome1
            user.setJobNumber(jobNumber);
            int userList = userService.insertUser(user);
            if(userList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_REALNAME_NULL, result);
            }else if(userList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    
    /**
     * 超级管理员修改用户信息
     * @param request
     * @param response
     * @param name
     * @param pwd
     * @param createTime
     * @param updateTime
     * @param oId
     */
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改用户信息",notes="超级管理员修改用户信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="pwd",value="密码",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query")})
    @ResponseBody
    public ResultUtils updateUser(String userId, String name, String realName, String pwd, String jobNumber){
        ResultUtils result = new ResultUtils();
        try {
            User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(pwd);
            user.setJobNumber(jobNumber);
            Integer userList = userService.updateUser(user);
            if(userList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(userList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_PWDFORMAT_ERROR, result);
            }else if(userList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            } 
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 管理员删除用户(停用)
     * @param request
     * @param response
     * @param userId
     */
    @RequiresPermissions("permission:stop")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value="用户信息删除",notes="管理员删除用户(停用)", response = ResultUtils.class)
    @ApiImplicitParams({@ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils deleteUser(String userId){
        ResultUtils result = new ResultUtils();
        try {
            Integer flag = userService.deleteUser(userId);//逻辑删除根据status状态判断0表示离职1表示在职
            if(flag == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(flag > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }  
        return result;
    }
    
    /**
     * 修改（当前登录用户）密码
     * @param request
     * @param response
     */
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/passWord", method = RequestMethod.POST)
    @ApiOperation(value="修改密码",notes="修改当前登录用户密码", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="oldPwd",value="旧密码",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="newPwd",value="新密码",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils getUserPwd(HttpServletRequest request, String oldPwd, String newPwd){
        ResultUtils result = new ResultUtils();
        
        User newuser = (User) request.getAttribute("user");//获取当前登录用户密码,salt
        
        try{
            if(oldPwd!=null && !oldPwd.equals("")){
                User userPwd = new User();
                userPwd.setPwd(oldPwd);
                userPwd.setSalt(newuser.getSalt());
                passwordHelper.encryptPassword(userPwd);
                oldPwd = userPwd.getPwd();//旧密码加密(页面传入)
            }else{
                ElementXMLUtils.returnValue(ElementConfig.USER_OLDPWD_NULL, result);
            }
            if(newPwd == null || newPwd.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_NEWPWD_NULL, result);
            }
            
            if(newPwd.matches(User.REGEX)){//密码规则校验
                if(oldPwd.equals(newuser.getPwd())) {//判断旧密码与原密码是否相等
                    if(oldPwd.equals(newPwd)){
                        ElementXMLUtils.returnValue(ElementConfig.USER_OLDPWD, result);
                    }else{
                        User user = new User();
                        user.setId(newuser.getId());
                        user.setPwd(newPwd);
                        Integer userList = userService.updateUser(user);
                        if(userList>0){
                            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                        }else{
                            ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                        }
                    }
                }else{
                    ElementXMLUtils.returnValue(ElementConfig.USER_OLDPWD_ERROR, result);
                }
            }else{
                ElementXMLUtils.returnValue(ElementConfig.USER_PWDFORMAT_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 管理员重置密码(解锁用户)
     * @param request
     * @param response
     * @param userId
     * @param pwd
     */
    @RequiresPermissions("permission:reset")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ApiOperation(value="管理员重置密码",notes="管理员重置密码(解锁用户)", response = UserResetPwd.class)
    @ApiImplicitParams({@ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public UserResetPwd resetUser(String userId){
        UserResetPwd result = new UserResetPwd();
        try {
            User users = userService.getUserById(userId);
            if(users == null){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
                return result;
            }
            if(users.getName()!=null && !users.getName().equals("")){
                Object locking=redis.opsForValue().get("financialSystem"+"_cache_"+users.getName()+"_status");//获取是否锁定
                if(locking != null){
                    redis.delete(users.getName());
                    redis.delete("financialSystem"+"_cache_"+users.getName()+"_status");//清除key
                }
            }
            String resetPwd = UuidUtil.getRandomPassword(6);//生成随机重置密码
            User user = new User();
            user.setId(userId);
            user.setPwd(resetPwd);
            Integer userList = userService.updateUser(user);
            if(userList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(userList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_PWDFORMAT_ERROR, result);
            }else if(userList > 0){
                System.out.println("重置密码："+resetPwd);
                result.setResetPwd(resetPwd);
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            } 
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        
        return result;
    }
    /**
     * 根据用户id查询组织结构关联信息(用户组织结构关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"organization:view","permission:view"})
    @RequestMapping(value = "/userOrganizationIndex", method = RequestMethod.POST)
    @ApiOperation(value="根据用户id查询用户组织结构关联信息",notes="根据用户id查询用户组织结构关联信息(用户组织结构关联表)", response = UserOrganizationResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public UserOrganizationResult listUserOrganization(String uId){
        UserOrganizationResult result = new UserOrganizationResult();
        try {
            List<JSONObject> jsonUserOrg = userOrganizationService.userOrganizationList(uId);
            if(jsonUserOrg == null){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
                return result;
            }
            result.setUserOrganizationList(jsonUserOrg);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 新增(用户组织结构关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"organization:create","permission:create"})
    @RequestMapping(value = "/userOrganizationInsert", method = RequestMethod.POST)
    @ApiOperation(value="新增用户组织结构关联信息",notes="新增(用户组织结构关联表)", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertUserOrganization(String uId, String orgId){
        ResultUtils result = new ResultUtils();
        try {
            UserOrganization userOrganization = new UserOrganization();
            userOrganization.setuId(uId);
            userOrganization.setoId(orgId);
            int userOrganizationList = userOrganizationService.insertUserOrganization(userOrganization);
            if(userOrganizationList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(userOrganizationList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_ORGID_NULL, result);
            }else if(userOrganizationList>0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            } 
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 修改（先删除用户关联的组织架构信息，再重新添加该用户的组织架构信息）（根据用户id修改用户组织架构关联信息）
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions({"organization:update","permission:update"})
    @RequestMapping(value = "/userOrganizationUpdate", method = RequestMethod.POST)
    @ApiOperation(value="修改用户组织结构关联信息",notes="先删除用户关联的组织架构信息，再重新添加该用户的组织架构信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils updateUserOrganization(String uId, String orgId){
        ResultUtils result = new ResultUtils();
        try {
            UserOrganization userOrganization = null;
            userOrganization = new UserOrganization();
            userOrganization.setId(UuidUtil.getUUID());
            userOrganization.setoId(orgId);
            userOrganization.setuId(uId);
            int userOrganizationList = userOrganizationService.updateUserOrganization(userOrganization);//修改（新增数据）
            if(userOrganizationList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(userOrganizationList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_ORGID_NULL, result);
            }else if(userOrganizationList == -3){
                ElementXMLUtils.returnValue(ElementConfig.USER_ORGANIZATION, result);
            }else if(userOrganizationList>0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    
    /**
     * 根据用户名查询用户角色关联信息(用户角色关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"permission:view","role:view"})
    @RequestMapping(value = "/userRoleIndex", method = RequestMethod.POST)
    @ApiOperation(value="根据用户名查询用户角色关联信息",notes="查询所有(用户角色关联表)", response = UserRoleResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public UserRoleResult listUserRole(String name){
        UserRoleResult result = new UserRoleResult();
        try {
            List<UserRole> userRole = userRoleService.listUserRole(name);
            if(userRole == null){
                ElementXMLUtils.returnValue(ElementConfig.USER_NAME_NULL, result);
                return result;
            }
            result.setUserRoleList(userRole);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 新增用户角色关联信息
     * @param request
     * @param response
     * @param uid
     * @param rid
     */
    @RequiresPermissions({"permission:create","role:create"})
    @RequestMapping(value = "/userRoleInsert", method = RequestMethod.POST)
    @ApiOperation(value="新增用户角色关联信息",notes="新增(用户角色关联表)", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value="角色id,传入json格式", paramType = "query", required = true),
        @ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils insertUserRole(String roleId, String uId){
        ResultUtils result = new ResultUtils();
        try {
            UserRole userRole =  new UserRole();
            userRole.setId(UuidUtil.getUUID());
            userRole.setrId(roleId);
            userRole.setuId(uId);
            int userRoleList = userRoleService.insertUserRole(userRole);
            if(userRoleList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
            }else if(userRoleList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(userRoleList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 修改(用户角色关联表)先删除用户关联的角色信息，再重新添加该用户的角色信息（根据用户id修改用户角色关联信息）
     * @param request
     * @param response
     * @param uid
     * @param rid
     * @param updateTime
     */
    @RequiresPermissions({"permission:update","role:update"})
    @RequestMapping(value = "/userRoleUpdate", method = RequestMethod.POST)
    @ApiOperation(value="修改用户角色关联信息",notes="修改(用户角色关联表)先删除用户关联的角色信息，再重新添加该用户的角色信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="roleId",value = "角色id,传入json格式", paramType = "query", required = true),
        @ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ResponseBody
    public ResultUtils updateUserRole(String roleId, String uId){
        ResultUtils result = new ResultUtils();
        try {
            UserRole userRole = new UserRole();
            userRole.setId(UuidUtil.getUUID());
            userRole.setrId(roleId);
            userRole.setuId(uId);
            int userRoleList = userRoleService.updateUserRole(userRole);//修改（新增数据）
            if(userRoleList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_NULL, result);
            }else if(userRoleList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
            }else if(userRoleList == -3){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLE, result);
            }else if(userRoleList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
}
