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

import com.alibaba.fastjson.JSONArray;
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
     * 管理员新增用户，对应的角色，组织结构信息
     * @param roleName			角色名称
     * @param userName			用户名称
     * @param realName			真实姓名
     * @param jobNumber			工号
     * @param roleId			角色id
     * @param orgId				组织结构id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="管理员新增用户，对应的角色，组织结构信息",notes="新增用户，对应的角色以及对应的权限", response = ResultUtils.class)
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
     * 管理员修改用户,对应的角色，组织结构信息
     * @param userId		用户id
     * @param name			用户名称
     * @param realName		真实姓名
     * @param status		状态
     * @param jobNumber		工号
     * @param roleId		角色id
     * @param orgId			组织结构id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="管理员修改用户,对应的角色，组织结构信息",notes="超级管理员修改用户，对应的角色以及对应的权限信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
        //@ApiImplicitParam(name="pwd",value="密码",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="status",value="员工状态0表示离职1表示在职",dataType="int", paramType = "query"),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="roleId",value = "角色id,传入json格式,例如：[{\"roleId\":\"0603027a3a0948729c47a9f279ca3b34\"}]", paramType = "query"),
        @ApiImplicitParam(name="orgId",value="组织结构id,传入json格式,例如：[{\"orgId\":\"fef092ad443546aca122c0616f069089\"}]", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空"),
        @ApiResponse(code = 208, message = "密码由6～15位数字、大小写字母组成"),@ApiResponse(code = 217, message = "用户组织架构关联信息不存在"),
        @ApiResponse(code = 218, message = "用户角色关联系信息不存在")})
    @ResponseBody
    public ResultUtils update(HttpServletRequest request, String userId, String name, String realName, Integer status, String jobNumber,String roleId,String orgId) {
    	ResultUtils result = new ResultUtils();
    	
    	try {
    	    User currentUser = (User) request.getAttribute("user");
            List<UserOrganization> userOrgPermission = new ArrayList<>();
            List<Organization> listOrg = new ArrayList<>();
            if(currentUser.getId()!=null && !currentUser.getId().equals("")){
                //根据当前登录用户id查询组织节点信息
                userOrgPermission.addAll(userOrganizationService.listUserOrganization(currentUser.getId(),null));
            }
            if(!CollectionUtils.isEmpty(userOrgPermission)){
                for(UserOrganization orgCode : userOrgPermission){
                    //根据code查询组织架构信息(查询当前登录人orgId信息)
                    listOrg.addAll(organizationService.listOrganizationBy(null,null,null,null,orgCode.getCode(),null,null,null,null));
                }
            }
            //查询需要修改的用户orgId信息
            List<UserOrganization> userOrgPermissions = new ArrayList<>();
            List<Organization> listOrgs = new ArrayList<>();
            userOrgPermissions.addAll(userOrganizationService.listUserOrganization(userId,null));
            if(!CollectionUtils.isEmpty(userOrgPermissions)){
                for(UserOrganization orgCode : userOrgPermissions){
                    //根据code查询组织架构信息(查询需要修改的用户orgId信息)
                    listOrgs.addAll(organizationService.listOrganizationBy(null,null,null,null,orgCode.getCode(),null,null,null,null));
                }
            }
            //当前登录人的orgId
            List<String> org = new ArrayList<>();//需要修改的orgId
            if(!CollectionUtils.isEmpty(listOrg)){
                for(Organization orgCode : listOrg){
                    org.add(orgCode.getId());
                }
            }
            //需要修改的orgId
            List<String> orgs = new ArrayList<>();//需要修改的orgId
            if(!CollectionUtils.isEmpty(listOrgs)){
                for(Organization orgCode : listOrgs){
                    orgs.add(orgCode.getId());
                }
            }
            //循环检索需要修改的orgId是否包括在当前登录人的orgId之内
            if(!org.containsAll(orgs)){
                ElementXMLUtils.returnValue(ElementConfig.USER_ORGANIZATION_RESULT, result);
                return result;
            } 
    	    if(name!=null && !name.equals("")){
    	        //判断用户名是否存在
                Integer flag = userService.countUserName(name,"");//查询用户名是否存在(真实姓名可以重复)
                if(flag > 0){
                    ElementXMLUtils.returnValue(ElementConfig.USERNAME_EXISTENCE, result);
                    return result;
                }
    	    }
    	    if(jobNumber!=null && !jobNumber.equals("")){
    	        //判断工号是否存在
                Integer jobNumberFlag = userService.countUserName("", jobNumber);//查询工号是否存在
                if(jobNumberFlag > 0){
                    ElementXMLUtils.returnValue(ElementConfig.JOBNUMBER_EXISTENCE, result);
                    return result;
                }
    	    }
            //修改用户
    		User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setRealName(realName);
            user.setStatus(status);
            //user.setPwd(pwd);
            user.setJobNumber(jobNumber);
            Integer userList = userService.updateUser(user);
            //修改用户角色关联信息,先删除用户关联的角色信息，再重新添加该用户的角色信息
            Integer userRoleList = 0;
            if(roleId != null && !"".equals(roleId)) {
            	UserRole userRole = new UserRole();
                userRole.setId(UuidUtil.getUUID());
                userRole.setrId(roleId);
                userRole.setuId(userId);
                userRoleList = userRoleService.updateUserRole(userRole);
            }
            //修改用户组织架构关联信息，先删除用户关联的组织架构信息，再重新添加该用户的组织架构信息
            Integer userOrganizationList = 0;
            if(orgId != null && !"".equals(orgId)) {
            	UserOrganization userOrganization = null;
                userOrganization = new UserOrganization();
                userOrganization.setId(UuidUtil.getUUID());
                userOrganization.setoId(orgId);
                userOrganization.setuId(userId);
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
                if(status!=null && !status.equals("")){
                    if(currentUser.getId().equals(userId)){//修改自己的状态为离职
                        if(status.equals(0)){
                            ElementXMLUtils.returnValue(ElementConfig.USER_QUIT, result);
                        }
                    }
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
    public Map<String, Object> listUser(HttpServletRequest request, String name, String realName, String jobNumber,
            String createTime, String updateTime, Integer status,String orgName, Integer pageSize, Integer page){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        User currentUser = (User) request.getAttribute("user");
    	try {
    	    //long start = System.currentTimeMillis();
    	    Map<Object, Object> map = new HashMap<>();
	        map.put("name", name);//用户名
            map.put("realName", realName);//真实姓名
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
            List<Role> role = new ArrayList<>();
            List<UserRole> userRoleId = new ArrayList<>();
            List<String> uId = new ArrayList<>();//超级管理员用户id
            List<String> rIdList = new ArrayList<>();//超级管理员角色id
            boolean superAdmin = true;//当前登录用户是否是超级管理员
            Integer userList = 0;
            //判断当前登录用户数据权限范围
            if(currentUser.getId()!=null && !currentUser.getId().equals("")){
                //根据当前登录用户id查询组织节点信息
                userOrgPermission = userOrganizationService.listUserOrganization(currentUser.getId(),null);
                //根据当前登录用户名查询用户关联的角色id信息
                List<UserRole> userRole = userRoleService.listUserRole(currentUser.getName(), null);
                if(!CollectionUtils.isEmpty(userRole)){
                    for(UserRole uRole : userRole){
                        Role roleName = roleService.getRoleById(uRole.getrId());
                        role.add(roleName);
                        if(!CollectionUtils.isEmpty(role)){
                            for(Role roles : role){
                                //查询当前登录用户是不是超级管理员
                                if(roles.getRoleName().equals("超级管理员")){
                                    superAdmin = false;//是超级管理员
                                }
                            }
                        }
                    }
                }
            }
            if(superAdmin){//不是超级管理员就把超级管理员的uId作为条件查询
                //根据超级管理员查询角色id
                List<Role> rolelists = roleService.listRole("超级管理员", null);
                if(!CollectionUtils.isEmpty(rolelists)){
                    for(Role rid : rolelists){
                        rIdList.add(rid.getId());
                    }
                }
                if(!CollectionUtils.isEmpty(rIdList)){
                    for(String r : rIdList){
                        //根据超级管理员角色id查询关联的用户id
                        userRoleId.addAll(userRoleService.listUserRole(null, r));
                    }
                    if(!CollectionUtils.isEmpty(userRoleId)){
                        for(int i=0;i<userRoleId.size();i++){
                            //把超级管理员的uId作为条件查询
                            uId.add(userRoleId.get(i).getuId());
                        }
                    }
                } 
            }
            List<String> org = new ArrayList<>();
            if(!CollectionUtils.isEmpty(userOrgPermission)){
                for(UserOrganization orgId : userOrgPermission){
                    org.add(orgId.getCode());//关联的code
                }
                map.put("orgName", orgName);//组织架构名称
                map.put("uId", uId);//用户id
                map.put("code", org);//组织架构code
                user.addAll(userService.listUserOrgOId(map));//查询全部map为空
                userList = userService.listUserOrgNameCount(map);
            }else{
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.USER_ORGANIZATION));
                return dataMapList;
            }     
            if(!CollectionUtils.isEmpty(user)){
                for(User item : user){
                    item.setOrgFlag(User.MATCH);
                    item.setRoleFlag(User.SETUP);
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
    /*@RequiresPermissions("permission:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value="新增用户信息",notes="新增用户信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="name",value="用户名称",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 222, message = "用户名为空"),
        @ApiResponse(code = 224, message = "工号为空"),@ApiResponse(code = 209, message = "用户名已存在"),
        @ApiResponse(code = 212, message = "工号已存在"),@ApiResponse(code = 223, message = "真实姓名为空")})
    @ResponseBody
    public ResultUtils insertUser(String name, String realName, String jobNumber){
        ResultUtils result = new ResultUtils();
        try {
            if(name == null || name.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_NAME_NULL, result);
                return result;
            }
            if(jobNumber == null || jobNumber.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_JOBNUMBER_NULL, result);
                return result;
            }
            if(realName == null || realName.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.USER_REALNAME_NULL, result);
                return result;
            }
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
            User user = new User();
            user.setId(UuidUtil.getUUID());
            user.setSalt(UuidUtil.getUUID());
            user.setName(name);
            user.setRealName(realName);
            user.setPwd(User.INITIALCIPHER);//用户新增默认密码为Welcome1
            user.setJobNumber(jobNumber);
            int userList = userService.insertUser(user);
            if(userList > 0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
            }
            
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }*/
    
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
/*    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value="修改用户信息",notes="超级管理员修改用户信息", response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="userId",value="用户id",dataType="string", paramType = "query", required = true),
        @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="realName",value="真实姓名",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="pwd",value="密码",dataType="string", paramType = "query"),
        @ApiImplicitParam(name="jobNumber",value="工号",dataType="string", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 221, message = "用户id为空"),
        @ApiResponse(code = 208, message = "密码由6～15位数字、大小写字母组成")})
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
    }*/
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
    //@RequiresPermissions("user:update")
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
                Object locking=redis.opsForValue().get("financialSystem"+"_cache_"+users.getName().toLowerCase()+"_status");//获取是否锁定
                if(locking != null){
                    redis.delete("financialSystem"+"_cache_"+users.getName().toLowerCase()+"_count");
                    redis.delete("financialSystem"+"_cache_"+users.getName().toLowerCase()+"_status");//清除key
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
     * 根据用户id查询用户组织结构关联信息(用户组织结构关联表)(详情用)
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userOrganizationById", method = RequestMethod.POST)
    @ApiOperation(value="根据用户id查询用户组织结构关联信息",notes="根据用户id查询用户组织结构关联信息(用户组织结构关联表)", response = UserOrganizationResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="uId",value="用户id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 221, message = "用户id为空")})
    @ResponseBody
    public Map<String, Object> listUserOrganization(String uId){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            if(uId == null || uId.equals("")){
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.USER_ID_NULL));
                return dataMapList;
            }
            List<JSONObject> jsonUserOrg = userOrganizationService.userOrganizationList(uId);
            dataMap.put("userOrganizationList", jsonUserOrg);
            dataMapList.put("data", dataMap);
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        } catch (Exception e) {
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMapList;
    }
    /**
     * 根据用户id查询组织结构关联信息(用户组织结构关联表)/获取当前登录人组织架构关联信息(修改用)
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
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
    /*@RequiresPermissions("permission:create")
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
    }*/
    /**
     * 修改（先删除用户关联的组织架构信息，再重新添加该用户的组织架构信息）（根据用户id修改用户组织架构关联信息）
     * @param request
     * @param response
     * @return
     */
    /*@RequiresPermissions("permission:update")
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
    }*/
    
    /**
     * 根据用户名查询用户关联的角色信息和功能权限信息(详情用)
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userRoleById", method = RequestMethod.POST)
    @ApiOperation(value="根据用户名查询用户关联的角色信息和功能权限信息",notes="根据用户名查询用户关联的角色信息和功能权限信息", response = UserRoleResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query")})
    @ResponseBody
    public Map<String, Object> listUserRole(String name){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if(name == null || name.equals("")){
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.USER_NAME_NULL));
            return dataMapList;
        }
        List<Role> roleList = new ArrayList<>();
        List<RoleResource> roleResource = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
       
        //根据用户名查询用户角色关联信息
        List<UserRole> userRole = userRoleService.listUserRole(name, null);
        for(UserRole uRole : userRole){//循环获取角色名
            roleList.addAll(roleService.listRole(uRole.getRoleName(), null));
        }
        if(!CollectionUtils.isEmpty(roleList)){
            for(Role role : roleList){
                //根据角色id查询角色关联功能权限信息
                roleResource = roleResourceService.listRoleResource(role.getId());
                JSONArray  jsonArray = new JSONArray();
                List<TreeNode<RoleResource>> nodes = new ArrayList<>();
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
                    if(!CollectionUtils.isEmpty(nodes)){
                        jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
                    }
                    if(jsonObject.getString("id").equals("-1")){
                        jsonArray = jsonObject.getJSONArray("children");
                    }else{
                        jsonArray.add(jsonObject);
                    }
                    role.setJsonRoleResource(jsonArray);//角色功能能权限关联信息
                }
            }
        }
        
        dataMap.put("userRoleList", roleList);
        dataMapList.put("data", dataMap);
        dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
        return dataMapList;
    }
    
    /**
     * 根据用户名查询用户角色关联信息(用户角色关联表)
     * name为空查全部角色信息及关联的功能权限信息,根据用户名查询用户角色关联信息及关联的功能权限信息
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/userRoleIndex", method = RequestMethod.POST)
    @ApiOperation(value="name为空查全部角色信息及关联的功能权限信息,根据用户名查询用户角色关联信息及关联的功能权限信息",notes="修改用", response = UserRoleResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query")})
    @ResponseBody
    public Map<String, Object> listUserRole(HttpServletRequest request, String name){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        User currentUser = (User) request.getAttribute("user");
        try {
            List<Role> roleNameList = new ArrayList<>();
            String rName = "N";//不是超级管理员
            if(currentUser.getId()!=null && !currentUser.getId().equals("")){
                //根据当前登录用户名查询是否是超级管理员
                List<UserRole> userRole = userRoleService.listUserRole(currentUser.getName(), null);//根据用户名查询用户是否是超级管理员
                if(!CollectionUtils.isEmpty(userRole)){
                    for(UserRole uRole : userRole){
                        Role roleName = roleService.getRoleById(uRole.getrId());
                        roleNameList.add(roleName); 
                    }
                }
            }
            if(!CollectionUtils.isEmpty(roleNameList)){
                for(Role roles : roleNameList){
                    if(roles.getRoleName().equals("超级管理员")){//是超级管理员
                        rName = null;
                    }
                }
            }
            //name为空查全部角色信息及关联的功能权限信息
            List<Role> roleList = roleService.listRole("",rName);//查询全部参数为空
            for(Role uRole : roleList){
                List<RoleResource> roleResource = roleResourceService.listRoleResource(uRole.getId());
                List<TreeNode<RoleResource>> nodes = new ArrayList<>();
                JSONObject jsonObject = new JSONObject();
                JSONArray  jsonArray = new JSONArray();
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
                    if(jsonObject.getString("id").equals("-1")){
                        jsonArray = jsonObject.getJSONArray("children");
                    }else{
                        jsonArray.add(jsonObject);
                    }
                    uRole.setJsonRoleResource(jsonArray);//角色功能能权限关联信息
                }
            }
            if(name == null || name.equals("")){
                dataMap.put("userRoleList", roleList);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }else{
                //不为空根据name查询关联角色信息
                List<UserRole> userRole = userRoleService.listUserRole(name, null);
                if(!CollectionUtils.isEmpty(userRole)){
                    for(Role role : roleList){
                        role.setMathc(false);
                        for(UserRole uRole : userRole){
                            if(role.getId().contains(uRole.getrId())){
                                role.setMathc(true);
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
    /*@RequiresPermissions("permission:create")
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
    }*/
    /**
     * 修改(用户角色关联表)先删除用户关联的角色信息，再重新添加该用户的角色信息（根据用户id修改用户角色关联信息）
     * @param request
     * @param response
     * @param uid
     * @param rid
     * @param updateTime
     */
    /*@RequiresPermissions("permission:update")
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
    }*/
}
