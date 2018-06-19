package cn.financial.controller;

import java.util.ArrayList;
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

import cn.financial.model.RoleResource;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.RoleResourceService;
import cn.financial.service.UserRoleService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.TreeNode;
import net.sf.json.JSONObject;

/**
 * 登录认证
 * @author gs
 *
 */
@Controller
public class MainController {
    
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleResourceService roleResourceService;
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
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(request.getParameter("username"),request.getParameter("password"));   
        try {
            subject.login(token);
            session.setAttribute("password", request.getParameter("password"));
            dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, "code"));
            dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, "description"));
        }catch (UnknownAccountException e) {
            System.out.println( "该用户不存在");
            dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.LOGIN_NO_USER, "code"));
            dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.LOGIN_NO_USER, "description"));
        }catch (IncorrectCredentialsException e) { 
            System.out.println( "密码或账户错误，请重新输入");
            dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.LOGIN_USERNAME_ERROR, "code"));
            dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.LOGIN_USERNAME_ERROR, "description"));
        } catch (ExcessiveAttemptsException e) {  
            System.out.println("账户已锁，请联系管理员");
            dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.LOGIN_USER_LOCKOUT, "code"));
            dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.LOGIN_USER_LOCKOUT, "description"));
        } catch (AuthenticationException e) {  
            System.out.println( "其他错误：" + e.getMessage());
            // 其他错误，比如锁定，如果想单独处理请单独catch处理  
            dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.LOGIN_FAILURE, "code"));
            dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.LOGIN_FAILURE, "description"));
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
        dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, "code"));
        dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, "description"));
        currentUser.logout();  
        return dataMap;  
    } 
    /**
     * index(根据角色显示对应的功能权限)
     * @param request
     * @param respons
     * @return
     */
	@RequestMapping(value="/index", method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object> index(HttpServletRequest request, HttpServletResponse response, HttpSession session){
	    Map<String, Object> dataMap = new HashMap<String, Object>();
	    User user = (User) request.getAttribute("user");
	    String userName = user.getName();
	    String passWord = session.getAttribute("password").toString();
	    if(passWord!=null && !passWord.equals("")){
            if(passWord.equals("Welcome1")){
                dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.RESET_PWD, "code"));
                dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.RESET_PWD, "description"));
                return dataMap;
            }
	    }
	    if(userName!=null && !"".equals(userName)){
	        List<UserRole> userRole = userRoleService.listUserRole(userName);//根据用户名查询对应角色信息
	        List<RoleResource> roleResource = new ArrayList<RoleResource>();
	        if(userRole.size()>0){ //CollectionUtils.isEmpty(userRole)
	            for(UserRole list:userRole){
	                roleResource.addAll(roleResourceService.listRoleResource(list.getrId()));//根据角色id查询对应功能权限信息
	            }  
	        }
	        List<TreeNode<RoleResource>> nodes = new ArrayList<>();
	        JSONObject jsonObject = null;
	        if(roleResource.size()>0){
	            for (RoleResource rss : roleResource) {
	                TreeNode<RoleResource> node = new TreeNode<>();
	                node.setId(rss.getCode().toString());
	                String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
	                node.setParentId(b);
	                node.setName(rss.getName());
	               // node.setNodeData(rss);
	                nodes.add(node);
	            }
	            jsonObject = JSONObject.fromObject(TreeNode.buildTree(nodes));
	        }
            
            //System.out.println(jsonObject); 
	        dataMap.put("roleResource", jsonObject.toString());
	        dataMap.put("resultCode", ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, "code"));
            dataMap.put("resultDesc", ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, "description"));
	    }
    	return dataMap;
    }
}