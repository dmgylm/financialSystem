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

import cn.financial.model.UserRole;
import cn.financial.service.impl.UserRoleServiceImpl;
import cn.financial.util.FileUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

/**
 * 用户角色关联表
 * @author gs
 * 2018/3/13
 */
@Controller
public class UserRoleController {
    @Autowired
    UserRoleServiceImpl roleService;
    
    protected Logger logger = LoggerFactory.getLogger(UserRoleController.class);
    /**
     * 查询所有
     * @param request
     * @param response
     */
    @RequestMapping(value = "/userRole/index", method = RequestMethod.POST)
    public void listUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<UserRole> role = roleService.listUserRole();
            dataMap.put("userList", role);
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
    @RequestMapping(value = "/userRole/userRoleById", method = RequestMethod.POST)
    public void getUserRoleById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            UserRole role = roleService.getUserRoleById(request.getParameter("userRoleId"));
            dataMap.put("userById", role);
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
     * 新增
     * @param request
     * @param response
     * @param uid
     * @param rid
     * @param createTime
     * @param updateTime
     */
    @RequestMapping(value = "/userRole/insert", method = RequestMethod.POST)
    public void insertUserRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            UserRole userRole = new UserRole();
            userRole.setId(UuidUtil.getUUID());
            userRole.setuId(request.getParameter("uId"));
            userRole.setrId(request.getParameter("rId"));
            userRole.setCreateTime(new Date());
            int userRoleList = roleService.insertUserRole(userRole);
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
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
}
