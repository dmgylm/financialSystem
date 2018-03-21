package cn.financial.controller;

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

import cn.financial.model.Role;
import cn.financial.model.RoleResource;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.service.impl.RoleServiceImpl;
import cn.financial.util.UuidUtil;

/**
 * 角色(角色资源关联表)
 * @author gs
 * 2018/3/13
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    RoleResourceServiceImpl roleResourceService;
    
    protected Logger logger = LoggerFactory.getLogger(RoleController.class);
    /**
     * 查询所有角色
     * @param request
     * @param response
     */
    @RequiresPermissions("role:view")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public Map<String, Object> listRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Role> role = roleService.listRole();
            dataMap.put("roleList", role);
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
     * @param roleId
     * @return
     */
    @RequiresPermissions("role:view")
    @RequestMapping(value = "/roleById", method = RequestMethod.POST)
    public Map<String, Object> getRoleById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Role role = roleService.getRoleById(request.getParameter("roleId"));
            dataMap.put("roleById", role);
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
     * 新增角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Map<String, Object> insertRole(HttpServletRequest request,HttpServletResponse response){
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
        return dataMap;
    }
    /**
     * 修改角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateRole(HttpServletRequest request,HttpServletResponse response){
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
        return dataMap;
    }
    /**
     * 删除角色
     * @param request
     * @param response
     * @param roleId
     */
    @RequiresPermissions("role:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteRole(HttpServletRequest request,HttpServletResponse response){
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
        return dataMap;
    }
    
    /**
     * 查询所有/根据角色id查对应的功能权限(角色资源关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions({"role:view","resource:view"})
    @RequestMapping(value = "/roleResource/index", method = RequestMethod.POST)
    public Map<String, Object> listRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<RoleResource> roleResource = roleResourceService.listRoleResource(request.getParameter("rId"));
            dataMap.put("roleResourceList", roleResource);
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
     * 新增(角色资源关联表)
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions({"role:create","resource:create"})
    @RequestMapping(value = "/RoleResource/insert", method = RequestMethod.POST)
    public Map<String, Object> insertRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getUUID());
            roleResource.setsId(request.getParameter("sId"));
            roleResource.setrId(request.getParameter("rId"));
            roleResource.setCreateTime(new Date());
            int roleResourceList = roleResourceService.insertRoleResource(roleResource);
            if(roleResourceList>0){
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
