package cn.financial.controller;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Role;
import cn.financial.model.RoleResource;
import cn.financial.service.RoleResourceService;
import cn.financial.service.RoleService;
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
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;
    
    protected Logger logger = LoggerFactory.getLogger(RoleController.class);
    /**
     * 查询所有角色
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public Map<String, Object> listRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<Role> role = roleService.listRole("");
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
     * 根据roleId查询
     * @param request
     * @param response
     * @param roleId
     * @return
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/roleById", method = RequestMethod.POST)
    public Map<String, Object> getRoleById(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = request.getParameter("roleId");
            if(roleId == null || "".equals(roleId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "角色id为空");
            }else{
                Role role = roleService.getRoleById(roleId);
                dataMap.put("roleById", role);
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
     * 新增角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Map<String, Object> insertRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = request.getParameter("roleName");
            String createTime = request.getParameter("createTime");
            if(roleName == null || "".equals(roleName)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "角色名称为空");
            }else if(createTime == null || "".equals(createTime)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "创建时间为空");
            }else{
                roleName = new String(roleName.getBytes("ISO-8859-1"), "UTF-8");
                Role role = new Role();
                role.setId(UuidUtil.getUUID());
                role.setRoleName(roleName);
                role.setCreateTime(createTime);
                int roleList = roleService.insertRole(role);
                if(roleList>0){
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
     * 修改角色
     * @param request
     * @param response
     * @param roleName
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleName = request.getParameter("roleName");
            String updateTime = request.getParameter("updateTime");
            if(roleName == null || "".equals(roleName)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "角色名称为空");
            }else if(updateTime == null || "".equals(updateTime)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "修改时间为空");
            }else{
                roleName = new String(roleName.getBytes("ISO-8859-1"), "UTF-8");
                Role role = new Role();
                role.setId(UuidUtil.getUUID());
                role.setRoleName(roleName);
                role.setUpdateTime(updateTime);
                Integer roleList = roleService.updateRole(role);
                if(roleList>0){
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
     * 删除角色
     * @param request
     * @param response
     * @param roleId
     */
    @RequiresPermissions("permission:update")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> deleteRole(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String roleId = request.getParameter("roleId");
            if(roleId == null || "".equals(roleId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "角色id为空");
            }else{
                Integer flag = roleService.deleteRole(roleId);
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
     * 查询所有/根据角色id查对应的功能权限(角色资源关联表)
     * @param request
     * @param response
     */
    @RequiresPermissions("permission:view")
    @RequestMapping(value = "/roleResourceIndex", method = RequestMethod.POST)
    public Map<String, Object> listRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String rId = request.getParameter("rId");
            if(rId == null || "".equals(rId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "角色id为空");
            }else{
                List<RoleResource> roleResource = roleResourceService.listRoleResource(rId);
                dataMap.put("roleResourceList", roleResource);
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
     * 新增(角色资源关联表)
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param createTime
     * @param updateTime
     */
    @RequiresPermissions("permission:create")
    @RequestMapping(value = "/RoleResourceInsert", method = RequestMethod.POST)
    public Map<String, Object> insertRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String createTime = request.getParameter("creatTime");
            String resourceId = request.getParameter("resourceId");
            if(createTime == null || "".equals(createTime)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "创建时间为空");
            }else if(resourceId == null || "".equals(resourceId)){
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "资源权限id为空");
            }else{
                JSONArray sArray = JSON.parseArray(resourceId);
                int roleResourceList = 0;
                RoleResource roleResource = null;
                for (int i = 0; i < sArray.size(); i++) {
                    JSONObject object = (JSONObject) sArray.get(i);
                    String resourceIdStr = (String)object.get("resourceId");
                    System.out.println("resouceId:==="+resourceIdStr);
                    if(resourceIdStr!=null && !"".equals(resourceIdStr)){
                        roleResource = new RoleResource();
                        roleResource.setId(UuidUtil.getUUID());
                        roleResource.setsId(resourceIdStr);
                        roleResource.setrId(request.getParameter("rId"));
                        roleResource.setCreateTime(createTime);
                        roleResourceList = roleResourceService.insertRoleResource(roleResource);
                    }
                }
                if(roleResourceList>0){
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
}
