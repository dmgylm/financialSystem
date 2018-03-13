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

import cn.financial.model.Role;
import cn.financial.service.impl.RoleServiceImpl;
import cn.financial.util.FileUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

/**
 * 角色
 * @author gs
 * 2018/3/13
 */
@Controller
public class RoleController {
    @Autowired
    RoleServiceImpl roleService;
    
    protected Logger logger = LoggerFactory.getLogger(RoleController.class);
    /**
     * 查询所有用户
     * @param request
     * @param response
     */
    @RequestMapping(value = "/role/index", method = RequestMethod.POST)
    public void listRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Role> role = roleService.listRole();
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
    @RequestMapping(value = "/role/roleById", method = RequestMethod.POST)
    public void getRoleById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Role role = roleService.getRoleById(request.getParameter("roleId"));
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
     * 新增角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequestMapping(value = "/role/insert", method = RequestMethod.POST)
    public void insertRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = new String(request.getParameter("roleName").getBytes("ISO-8859-1"), "UTF-8");
            Role role = new Role();
            role.setId(UuidUtil.getUUID());
            role.setRoleName(roleName);
            role.setCreateTime(new Date());
            int roleList = roleService.insertRole(role);
            if(roleList>0){
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
    /**
     * 修改角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public void updateRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = new String(request.getParameter("roleName").getBytes("ISO-8859-1"), "UTF-8");
            Role role = new Role();
            role.setId(UuidUtil.getUUID());
            role.setRoleName(roleName);
            role.setUpdateTime(new Date());
            Integer roleList = roleService.updateRole(role);
            if(roleList>0){
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
     * 删除角色
     * @param request
     * @param response
     * @param roleId
     */
    @RequestMapping(value = "/role/delete", method = RequestMethod.POST)
    public void deleteRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Integer flag = roleService.deleteRole(request.getParameter("roleId"));
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
