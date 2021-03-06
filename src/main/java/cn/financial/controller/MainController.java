package cn.financial.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.model.response.LoginInfo;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserRoleService;
import cn.financial.service.UserService;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.webSocket.FinancialSocketHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 登录认证
 * @author gs
 *
 */
@Controller
@Api(value="登录/登出controller",tags={"登录/登出操作接口"})
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleResourceServiceImpl roleResourceService;
    @Autowired
    private UserOrganizationService userOrganizationService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    @Qualifier("apiRedisTemplate")
    private RedisTemplate redis;
    /**
     * swagger插件默认接口
     * @param request
     * @param response
     * @param session
     * @return
     */
    @Bean
   	public FinancialSocketHandler financialWebSocketHandler() {
   		
   		return new FinancialSocketHandler();
   		
   	}
    
    @RequestMapping(value="/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String userName = "aa";
        String passWord = "12345aA";
        
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,passWord);   
        try {
            subject.login(token);
            return "redirect:/docTest";
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
        }
        return "redirect:/test"; 
    }
    
    @RequestMapping(value="/docTest", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getTest(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/swagger-ui.html";
    }
    
    /**
     * 跳转登录页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/subLogin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
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
    @ApiOperation(value="登录认证",notes="返回用户关联功能权限信息", response = LoginInfo.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="username",value="用户名",dataType="string", paramType = "query",  required = true),
        @ApiImplicitParam(name="password",value="密码",dataType="string", paramType = "query",  required = true)})
    @ResponseBody
    public Map<String, Object> login(String username, String password){
        Map<String, Object> dataMapList = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            if(username == null || username.equals("")){
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.USERNAME_NULL_ERROR));
                return dataMapList;
            }
            if(password == null || password.equals("")){
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.PASSWORD_NULL_ERROR));
                return dataMapList;
            }
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);   
            subject.login(token);
            User user = userService.getUserByName(username);//查询用户信息
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            //等于，则返回值 0；之前，则返回小于 0 的值；之后，则返回大于 0 的值。
            if(user.getExpreTime()!=null && sf.format(new Date()).compareTo(user.getExpreTime())>=0){
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.PASSWORD_INVALID_ERROR));
                return dataMapList;
            }
            System.out.println("~~~session:"+subject.getSession().getId());
            List<String> roleName = new ArrayList<>();
            List<UserRole> userRole = userRoleService.listUserRole(username, null);//根据用户名查询对应角色信息
            if(userRole.size()>0){//用户可能拥有多个角色
                for(UserRole list : userRole){
                    roleName.add(list.getRoleName());
                }
            }
            JSONArray jsonArray = new JSONArray();
            List<JSONObject> jsonObject = roleResourceService.roleResourceList(username);
            if(!CollectionUtils.isEmpty(jsonObject)){
                for(JSONObject obj : jsonObject){
                    if(obj.getString("id").equals("-1")){
                        jsonArray = obj.getJSONArray("children");
                    }else{
                        jsonArray.add(obj);
                    }
               }
            }     
            List<JSONObject> jsonOrg = userOrganizationService.userOrganizationList(user.getId());
            dataMap.put("userName", username);
            dataMap.put("userId", user.getId());
            dataMap.put("roleName", roleName);
            dataMap.put("userOrganization", jsonOrg);
            dataMap.put("sessionId", subject.getSession().getId());
            //判断密码是否为Welcome1
            if(password.equals(User.INITIALCIPHER)){
                List<JSONObject> arrays = new ArrayList<>();
                /*JSONArray jsonArrays = new JSONArray();
                for(int i = 0;i < jsonArray.size(); i++){
                    JSONObject array = jsonArray.getJSONObject(i);
                    if(array.getString("name").equals("修改密码")){
                        for(Object item : array.getJSONArray("children")){
                            JSONObject  itemChildren = (JSONObject)JSONObject.toJSON(item);
                            if(itemChildren.getString("name").equals("修改密码")){
                                jsonArrays.add(itemChildren);
                                array.put("children", jsonArrays);
                            }
                        }
                        arrays.add(array);
                    }
                }*/
                dataMap.put("roleResource", arrays);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RESET_PWD));
            }else{
                dataMap.put("roleResource", jsonArray);
                dataMapList.put("data", dataMap);
                dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            }
        }catch (UnknownAccountException e) {
            System.out.println( "该用户不存在");
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_NO_USER));
        }catch (IncorrectCredentialsException e) { 
            Object obj = redis.opsForValue().get("financialSystem"+"_cache_"+username.toLowerCase()+"_count");//获取错误次数
            int count = 3 - (int)obj;
            System.out.println( "密码错误,你还可以输入"+count+"次");
            dataMapList.put("resultCode","202");
            dataMapList.put("resultDesc","密码错误,你还可以输入"+count+"次");
            //dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_USERNAME_ERROR));
        } catch (ExcessiveAttemptsException e) {  
            System.out.println("账户已锁，请联系管理员");
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_USER_LOCKOUT));
        } catch (Exception e) {  
            System.out.println( "其他错误：" + e.getMessage());
            // 其他错误，比如锁定，如果想单独处理请单独catch处理  
            dataMapList.putAll(ElementXMLUtils.returnValue(ElementConfig.LOGIN_FAILURE));
        }
        return dataMapList; 
    }
    /**
     * 登出
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})  
    @ApiOperation(value="登出",notes="登出", response = ResultUtils.class)
    @ResponseBody  
    public ResultUtils logout(HttpServletRequest request)  {  
    	 String url = request.getRequestURI();
         String sessionId= url.substring(url.lastIndexOf("=")+1);
         ResultUtils result = new ResultUtils();
         try {
			financialWebSocketHandler(). afterConnectionClosed("MessageSocketServerInfo;JSESSIONID="+sessionId,null);
	        Subject currentUser = SecurityUtils.getSubject();
	        currentUser.logout(); 
	        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return result;  
    } 
    
    /**
     * 空接口,用于前端某些情况下检测用户是否登录
     * @return
     */
    @RequestMapping(value = "/nullApi",method=RequestMethod.POST)
    @RequiresUser
    @ApiOperation(value="空接口,用于前端某些情况下检测用户是否登录",notes="", response = ResultUtils.class)
    @ResponseBody
    public ResultUtils nullApi(){
    	ResultUtils result = new ResultUtils();
    	ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
    	return result;
    }
}