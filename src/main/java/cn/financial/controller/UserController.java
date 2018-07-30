package cn.financial.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Organization;
import cn.financial.model.Role;
import cn.financial.model.RoleResource;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.UserRole;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.UserListReslult;
import cn.financial.model.response.UserOrganizationResult;
import cn.financial.model.response.UserResetPwd;
import cn.financial.model.response.UserResetPwdInfo;
import cn.financial.model.response.UserRoleResult;
import cn.financial.service.OrganizationService;
import cn.financial.service.RoleService;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserRoleService;
import cn.financial.service.UserService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;
import cn.financial.util.shiro.PasswordHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 用户(用户角色关联表)(用户组织结构关联表)
 * @author gs
 * 2018/3/7
 */
@Controller
@Api(value="用户controller",tags={"用户操作接口"})
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;
    @Autowired
    private UserOrganizationService userOrganizationService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    @Qualifier("apiRedisTemplate")
    private RedisTemplate redis;

    protected Logger logger = LoggerFactory.getLogger(UserController.class);
    
    /**
     * 新增用户，对应的角色以及对应的权限
     * @param roleName			角色名称
     * @param userName			用户名称
     * @param realName			真实姓名
     * @param jobNumber			工号
     * @param roleId			角色id
     * @param orgId				组织结构id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions({"role:create","permission:create","organization:create"})
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增用户，对应的角色以及对应的权限",notes="新增用户，对应的角色以及对应的权限", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="用户名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="roleId",value="角色id,传入json格式,例如：[{\"roleId\":\"0603027a3a0948729c47a9f279ca3b34\"}]", paramType = "query", required = true),
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式,例如：[{\"orgId\":\"fef092ad443546aca122c0616f069089\"}]", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 222, message = "用户名为空"),
        @ApiResponse(code = 224, message = "工号为空"),@ApiResponse(code = 209, message = "用户名已存在"),
        @ApiResponse(code = 212, message = "工号已存在"),@ApiResponse(code = 223, message = "真实姓名为空"),
        @ApiResponse(code = 228, message = "角色id为空"),@ApiResponse(code = 254, message = "请先新增组织架构配置"),
        @ApiResponse(code = 253, message = "请先新增角色设置")})
    @ResponseBody
    public ResultUtils insert(String name, String realName, String jobNumber,String roleId,String orgId) {
    	ResultUtils result = new ResultUtils();
    	try {
    		//判断传值是否为空
			if (name == null || name.equals("")) {
				ElementXMLUtils.returnValue(ElementConfig.USER_NAME_NULL, result);
				return result;
			}
			if (jobNumber == null || jobNumber.equals("")) {
				ElementXMLUtils.returnValue(ElementConfig.USER_JOBNUMBER_NULL, result);
				return result;
			}
			if (realName == null || realName.equals("")) {
				ElementXMLUtils.returnValue(ElementConfig.USER_REALNAME_NULL, result);
				return result;
			}
			if(roleId == null || roleId.equals("")){//roleId前台传入的数据是JSON格式
				ElementXMLUtils.returnValue(ElementConfig.USER_ROLEID_ISNULL, result);
	            return result;
	        }
			if(orgId == null || orgId.equals("")){//组织结构id
				ElementXMLUtils.returnValue(ElementConfig.USER_ORGID_ISNULL, result);
	            return result;
	        }
            //判断用户名和工号是否存在
            Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
            Integer jobNumberFlag = userService.countUserName("", jobNumber);//查询工号是否存在
            if(flag > 0){
                ElementXMLUtils.returnValue(ElementConfig.USERNAME_EXISTENCE, result);
                return result;
            }
            if(jobNumberFlag > 0){
                ElementXMLUtils.returnValue(ElementConfig.JOBNUMBER_EXISTENCE, result);
                return result;
            }
            //新增用户
            User user = new User();
            user.setId(UuidUtil.getUUID());
            user.setSalt(UuidUtil.getUUID());
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(User.INITIALCIPHER);//用户新增默认密码为Welcome1
            user.setJobNumber(jobNumber);
            Integer userList = userService.insertUser(user);
            if(userList > 0){
                //新增用户角色关联信息
                UserRole userRole =  new UserRole();
                userRole.setId(UuidUtil.getUUID());
                userRole.setrId(roleId);
                userRole.setuId(user.getId());
                int userRoleList = userRoleService.insertUserRole(userRole);
                if (userRoleList > 0){
                	//新增用户组织结构关联表
                    UserOrganization userOrganization = new UserOrganization();
                    userOrganization.setuId(user.getId());
                    userOrganization.setoId(orgId);
                    Integer userOrganizationList = userOrganizationService.insertUserOrganization(userOrganization);
                    if (userOrganizationList > 0){
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                    }else{
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }else{
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
		} catch (Exception e) {
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.logger.error(e.getMessage(), e);
		}
		return result;
    }
    
    /**
     * 超级管理员修改用户,对应的角色以及对应的权限信息
     * @param userId		用户id
     * @param name			用户名称
     * @param realName		真实姓名
     * @param pwd			密码
     * @param jobNumber		工号
     * @param roleId		角色id
     * @param orgId			组织结构id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions({"role:update","permission:update","organization:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改用户，对应的角色以及对应的权限信息",notes="超级管理员修改用户，对应的角色以及对应的权限信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="pwd",value="密码",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="roleId",value = "角色id,传入json格式,例如：[{\"roleId\":\"0603027a3a0948729c47a9f279ca3b34\"}]", paramType = "query"),
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式,例如：[{\"orgId\":\"fef092ad443546aca122c0616f069089\"}]", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空"),
        @ApiResponse(code = 208, message = "密码由6～15位数字、大小写字母组成"),@ApiResponse(code = 217, message = "用户组织架构关联信息不存在"),
        @ApiResponse(code = 218, message = "用户角色关联系信息不存在")})
    @ResponseBody
    public ResultUtils update(String userId, String name, String realName, String pwd, String jobNumber,String roleId,String orgId) {
    	ResultUtils result = new ResultUtils();
    	
    	try {
            //修改用户
    		User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(pwd);
            user.setJobNumber(jobNumber);
            Integer userList = userService.updateUser(user);
            //修改用户角色关联信息,先删除用户关联的角色信息，再重新添加该用户的角色信息
            Integer userRoleList = 0;
            if(roleId != null && !"".equals(roleId)) {
            	UserRole userRole = new UserRole();
                userRole.setId(UuidUtil.getUUID());
                userRole.setrId(roleId);
                userRole.setuId(user.getId());
                userRoleList = userRoleService.updateUserRole(userRole);
            }
            //修改用户组织架构关联信息，先删除用户关联的组织架构信息，再重新添加该用户的组织架构信息
            Integer userOrganizationList = 0;
            if(orgId != null && !"".equals(orgId)) {
            	UserOrganization userOrganization = null;
                userOrganization = new UserOrganization();
                userOrganization.setId(UuidUtil.getUUID());
                userOrganization.setoId(orgId);
                userOrganization.setuId(user.getId());
                userOrganizationList = userOrganizationService.updateUserOrganization(userOrganization);
            }
            
            if(userList == -1){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);//用户id为空
            }else if(userList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_PWDFORMAT_ERROR, result);//密码输入格式错误
            }else if(userRoleList == -3){
                ElementXMLUtils.returnValue(ElementConfig.USER_ROLE, result);//用户角色关联系信息不存在 
            }else if(userOrganizationList == -3){
                ElementXMLUtils.returnValue(ElementConfig.USER_ORGANIZATION, result);//用户组织架构关联信息不存在
            }else if(userList > 0 || userRoleList > 0 || userOrganizationList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
           
		} catch (Exception e) {
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            this.logger.error(e.getMessage(), e);
		}
    	
    	return result;
    }

    /**
     * 查询所有用户/多条件查询用户列表
     * @param request
     * @param response
     */
    //@RequiresRoles("超级管理员")
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    @ApiOperation(value="查询用户信息",notes="查询所有用户/多条件查询用户列表",response = UserListReslult.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="status",value="状态0表示离职1表示在职",dataType="int", paramType = "query"),
        @ApiImplicitParam(name="createTime",value="创建时间（如2018-07-10 13:21:10）",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="updateTime",value="更新时间(如2018-07-10)",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="orgName",value="组织架构名称",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="pageSize",value="条数（如每页显示10条数据）",dataType="int", paramType = "query", required = true),
        @ApiImplicitParam(name="page",value="页码（第一页开始）page=1",dataType="int", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误")})
    @ResponseBody
    public Map<String, Object> listUser(HttpServletRequest request, String name, String realName, String jobNumber,String userId,
            String createTime, String updateTime, Integer status,String orgName, Integer pageSize, Integer page){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        User currentUser = (User) request.getAttribute("user");
    	try {
    	    //long start = System.currentTimeMillis();
    	    Map<Object, Object> map = new HashMap<>();
	        map.put("name", name);//用户名
            map.put("realName", realName);//真实姓名
    	    map.put("id", userId);//用户id
    	    map.put("jobNumber", jobNumber);//工号
    	    map.put("status", status);//状态
    	    map.put("createTime", createTime);//创建时间
    	    map.put("updateTime", updateTime);//修改时间
    	    if(pageSize==null || pageSize.equals("")){
                map.put("pageSize",10);//条数
            }else{
                map.put("pageSize",pageSize);
            }
            if(page==null || page.equals("")){
                map.put("start",0);//页码
            }else{
                map.put("start",pageSize * (page- 1));
            }
            List<User> user = new ArrayList<>();
            List<UserOrganization> userOrgPermission = new ArrayList<>();
            Integer userList = 0;
            //判断当前登录用户数据权限范围
            if(currentUser.getId()!=null && !currentUser.getId().equals("")){
                //根据当前登录用户id查询组织节点信息
                userOrgPermission = userOrganizationService.listUserOrganization(currentUser.getId());
            }
            List<Organization> userOrg = new ArrayList<>();
            List<Organization> userOrganization = new ArrayList<>();
            Map<String, Organization> removalOrg = new HashMap<>();
            if(!CollectionUtils.isEmpty(userOrgPermission)){
                for(UserOrganization userOrgId : userOrgPermission){
                    //根据当前节点id查询子节点信息
                    userOrg.addAll(organizationService.listTreeByIdForSon(userOrgId.getoId()));
                }
            }else{
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.USER_ORGANIZATION));
                return dataMapList;
            }
            List<String> org = new ArrayList<>();
            if(!CollectionUtils.isEmpty(userOrg)){
                for(Organization orgId : userOrg){
                    removalOrg.put(orgId.getId(), orgId);
                }
                //判断去重之后的orgName
                for(Organization orgRemoval : removalOrg.values()){
                    if(orgName!=null && !orgName.equals("")){
                        if(orgRemoval.getOrgName().contains(orgName.toUpperCase())){
                            userOrganization.add(orgRemoval);
                        }
                    }else{
                        userOrganization.add(orgRemoval);
                    }
                }
                if(!CollectionUtils.isEmpty(userOrganization)){
                    for(Organization orgId : userOrganization){
                        org.add(orgId.getId());//添加筛选之后的oId
                    }
                    map.put("oId", org);
                    user.addAll(userService.listUserOrgOId(map));//查询全部map为空
                    userList = userService.listUserOrgNameCount(map);
                }     
            }
            if(!CollectionUtils.isEmpty(user)){
                for(User item : user){
                    List<JSONObject> jsonOrg = userOrganizationService.userOrganizationList(item.getId());
                    //List<JSONObject> jsonRole = roleResourceService.roleResourceList(item.getName());//查询角色功能权限关联信息
                    List<RoleResource> jsonRole = new ArrayList<>();
                    List<UserRole> userRole = userRoleService.listUserRole(name);//根据用户名查询用户角色关联信息是否存在
                    if(!CollectionUtils.isEmpty(userRole)){
                        for(UserRole uRole : userRole){
                            jsonRole = roleResourceService.listRoleResource(uRole.getrId()); 
                        }
                    }
                    if(!CollectionUtils.isEmpty(jsonOrg)){
                        item.setOrgFlag(User.MATCH);
                    }else{
                        item.setOrgFlag(User.UNMATCHED);
                    }
                    if(!CollectionUtils.isEmpty(jsonRole)){
                        item.setRoleFlag(User.SETUP);
                    }else{
                        item.setRoleFlag(User.NOTSET);
                    }
                }
            }
            //long end = System.currentTimeMillis();
            //System.out.println("花费时间:" + (end - start));
            dataMap.put("userList", user);
            dataMap.put("total", userList);
            dataMapList.put("data", dataMap);
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
    	return dataMapList;
    }
    /**
     * 根据id查询
     * @param request
     * @param response
     * @param id
     * @return
     */
    /*@RequiresPermissions("permission:view")
    @RequestMapping(value = "/userById", method = RequestMethod.POST)
    @ApiOperation(value="根据id查询用户信息",notes="根据id查询用户信息",response = UserInfoResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空")})
    @ResponseBody
    public UserInfoResult getUserById(String userId){
        UserInfoResult result = new UserInfoResult();
        UserInfo resultInfo = new UserInfo();
        try {
            User user = userService.getUserById(userId);
            if(user == null){
                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL,result);
                return result;
            }
            resultInfo.setUserById(user);
            result.setData(resultInfo);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            e.printStackTrace();
        }
        return result;
    }*/
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
//    @RequiresPermissions("permission:create")
//    @RequestMapping(value = "/insert", method = RequestMethod.POST)
//    @ApiOperation(value="新增用户信息",notes="新增用户信息", response = ResultUtils.class)
//    @ApiImplicitParams({
//        @ApiImplicitParam(name="name",value="用户名称",dataType="string", paramType = "query", required = true),
//        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query", required = true),
//        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query", required = true)})
//    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
//        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 222, message = "用户名为空"),
//        @ApiResponse(code = 224, message = "工号为空"),@ApiResponse(code = 209, message = "用户名已存在"),
//        @ApiResponse(code = 212, message = "工号已存在"),@ApiResponse(code = 223, message = "真实姓名为空")})
//    @ResponseBody
//    public ResultUtils insertUser(String name, String realName, String jobNumber){
//        ResultUtils result = new ResultUtils();
//        try {
//            if(name == null || name.equals("")){
//                ElementXMLUtils.returnValue(ElementConfig.USER_NAME_NULL, result);
//                return result;
//            }
//            if(jobNumber == null || jobNumber.equals("")){
//                ElementXMLUtils.returnValue(ElementConfig.USER_JOBNUMBER_NULL, result);
//                return result;
//            }
//            if(realName == null || realName.equals("")){
//                ElementXMLUtils.returnValue(ElementConfig.USER_REALNAME_NULL, result);
//                return result;
//            }
//            Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
//            Integer jobNumberFlag = userService.countUserName("", jobNumber);//查询工号是否存在
//            if(flag > 0){
//                ElementXMLUtils.returnValue(ElementConfig.USERNAME_EXISTENCE, result);
//                return result;
//            }
//            if(jobNumberFlag > 0){
//                ElementXMLUtils.returnValue(ElementConfig.JOBNUMBER_EXISTENCE, result);
//                return result;
//            }
//            User user = new User();
//            user.setId(UuidUtil.getUUID());
//            user.setSalt(UuidUtil.getUUID());
//            user.setName(name);
//            user.setRealName(realName);
//            user.setPwd(User.INITIALCIPHER);//用户新增默认密码为Welcome1
//            user.setJobNumber(jobNumber);
//            int userList = userService.insertUser(user);
//            if(userList > 0){
//                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
//            }else{
//                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
//            }
//            
//        } catch (Exception e) {
//            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
//            this.logger.error(e.getMessage(), e);
//        }
//        return result;
//    }
    
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
//    @RequiresPermissions("permission:update")
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @ApiOperation(value="修改用户信息",notes="超级管理员修改用户信息", response = ResultUtils.class)
//    @ApiImplicitParams({
//        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true),
//        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
//        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
//        @ApiImplicitParam(name="pwd",value="密码",dataType="string", paramType = "query"),
//        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query")})
//    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
//        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空"),
//        @ApiResponse(code = 208, message = "密码由6～15位数字、大小写字母组成")})
//    @ResponseBody
//    public ResultUtils updateUser(String userId, String name, String realName, String pwd, String jobNumber){
//        ResultUtils result = new ResultUtils();
//        try {
//            User user = new User();
//            user.setId(userId);
//            user.setName(name);
//            user.setRealName(realName);
//            user.setPwd(pwd);
//            user.setJobNumber(jobNumber);
//            Integer userList = userService.updateUser(user);
//            if(userList == -1){
//                ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL, result);
//            }else if(userList == -2){
//                ElementXMLUtils.returnValue(ElementConfig.USER_PWDFORMAT_ERROR, result);
//            }else if(userList > 0){
//                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
//            }else{
//                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
//            } 
//            
//        } catch (Exception e) {
//            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
//            this.logger.error(e.getMessage(), e);
//        }
//        return result;
//    }
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
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空")})
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
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 226, message = "旧密码为空"),
        @ApiResponse(code = 225, message = "新密码为空"),@ApiResponse(code = 206, message = "新旧密码一致"),
        @ApiResponse(code = 207, message = "密码输入错误，请重新输入"),@ApiResponse(code = 208, message = "密码由6～15位数字、大小写字母组成")})
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
                return result;
            }
            if(newPwd == null || newPwd.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_NEWPWD_NULL, result);
                return result;
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
    @ApiOperation(value="管理员重置密码",notes="管理员重置密码(解锁用户)", response = UserResetPwdInfo.class)
    @ApiImplicitParams({@ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空"),
        @ApiResponse(code = 208, message = "密码由6～15位数字、大小写字母组成")})
    @ResponseBody
    public UserResetPwdInfo resetUser(String userId){
        UserResetPwdInfo result = new UserResetPwdInfo();
        UserResetPwd resultInfo = new UserResetPwd();
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
            if(userList == -2){
                ElementXMLUtils.returnValue(ElementConfig.USER_PWDFORMAT_ERROR, result);
            }else if(userList > 0){
                System.out.println("重置密码："+resetPwd);
                resultInfo.setResetPwd(resetPwd);
                result.setData(resultInfo);
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
     * 根据用户id查询组织结构关联信息(用户组织结构关联表)/获取当前登录人组织架构关联信息
     * @param request
     * @param response
     */
    @RequiresPermissions({"organization:view","permission:view"})
    @RequestMapping(value = "/userOrganizationIndex", method = RequestMethod.POST)
    @ApiOperation(value="获取当前登录人组织架构关联信息/根据用户id查询用户组织结构关联信息",notes="根据用户id查询用户组织结构关联信息(用户组织结构关联表)", response = UserOrganizationResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 500, message = "系统错误")})
    @ResponseBody
    public Map<String, Object> listUserOrganization(HttpServletRequest request, String uId){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            User newuser = (User) request.getAttribute("user");//获取当前登录id
            List<JSONObject> jsonUserOrg = userOrganizationService.userOrganizationList(newuser.getId());
            if(uId == null || uId.equals("")){
                dataMap.put("userOrganizationList", jsonUserOrg);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                List<JSONObject> jsonUserOrgList = userOrganizationService.userOrganizationList(uId);
                if(!CollectionUtils.isEmpty(jsonUserOrgList)){
                    UserServiceImpl.addItem(jsonUserOrg, jsonUserOrgList);
                }
                dataMap.put("userOrganizationList", jsonUserOrg);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMapList;
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
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式,例如：[{\"orgId\":\"fef092ad443546aca122c0616f069089\"}]", paramType = "query", required = true)})
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
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式,例如：[{\"orgId\":\"fef092ad443546aca122c0616f069089\"}]", paramType = "query", required = true)})
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
     * name为空查全部角色信息及关联的功能权限信息,根据用户名查询用户角色关联信息及关联的功能权限信息
     * @param request
     * @param response
     */
    @RequiresPermissions({"permission:view","role:view","jurisdiction:view"})
    @RequestMapping(value = "/userRoleIndex", method = RequestMethod.POST)
    @ApiOperation(value="name为空查全部角色信息及关联的功能权限信息,根据用户名查询用户角色关联信息及关联的功能权限信息",notes="查询所有(用户角色关联表)", response = UserRoleResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query")})
    @ResponseBody
    public Map<String, Object> listUserRole(String name){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            //name为空查全部角色信息及关联的功能权限信息
            List<Role> roleList = roleService.listRole("");//查询全部参数为空
            for(Role uRole : roleList){
                List<RoleResource> roleResource = roleResourceService.listRoleResource(uRole.getId());
                List<TreeNode<RoleResource>> nodes = new ArrayList<>();
                JSONObject jsonObject = new JSONObject();
                if(!CollectionUtils.isEmpty(roleResource)){
                    for (RoleResource rss : roleResource) {
                        TreeNode<RoleResource> node = new TreeNode<>();
                        node.setId(rss.getCode().toString());//当前code
                        String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                        node.setParentId(b);//父id
                        node.setName(rss.getName());//功能权限名称
                        node.setPid(rss.getsId());//当前权限id
                        nodes.add(node);
                    }
                    jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
                    uRole.setJsonRoleResource(jsonObject);//角色功能能权限关联信息
                }
            }
            if(name == null || name.equals("")){
                dataMap.put("userRoleList", roleList);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                //不为空根据name查询关联角色信息
                List<UserRole> userRole = userRoleService.listUserRole(name);
                if(!CollectionUtils.isEmpty(userRole)){
                    for(Role role : roleList){
                        role.setMathc("N");
                        for(UserRole uRole : userRole){
                            if(role.getId().contains(uRole.getrId())){
                                role.setMathc("Y");
                            }
                        }
                    }
                }
                dataMap.put("userRoleList", roleList);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }
            
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMapList;
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
        @ApiImplicitParam(name="roleId",value="角色id,传入json格式,例如：[{\"roleId\":\"0603027a3a0948729c47a9f279ca3b34\"}]", paramType = "query", required = true),
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
        @ApiImplicitParam(name="roleId",value = "角色id,传入json格式,例如：[{\"roleId\":\"0603027a3a0948729c47a9f279ca3b34\"}]", paramType = "query", required = true),
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
