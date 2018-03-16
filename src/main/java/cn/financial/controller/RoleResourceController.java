package cn.financial.controller;

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

import cn.financial.model.RoleResource;
import cn.financial.service.impl.RoleResourceServiceImpl;
import cn.financial.util.FileUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

/**
 * 角色资源关联表
 * @author gs
 * 2018/3/14
 */
@Controller
public class RoleResourceController {
    @Autowired
    RoleResourceServiceImpl roleService;
    
    protected Logger logger = LoggerFactory.getLogger(RoleResourceController.class);
    /**
     * 查询所有/根据角色id查对应的功能权限
     * @param request
     * @param response
     */
    @RequestMapping(value = "/roleResource/index", method = RequestMethod.POST)
    public void listRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<RoleResource> roleResource = roleService.listRoleResource(request.getParameter("rId"));
            dataMap.put("roleResourceList", roleResource);
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
     * 新增
     * @param request
     * @param response
     * @param sid
     * @param rid
     * @param createTime
     * @param updateTime
     */
    @RequestMapping(value = "/roleResource/insert", method = RequestMethod.POST)
    public void insertRoleResource(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getUUID());
            roleResource.setsId(request.getParameter("sId"));
            roleResource.setrId(request.getParameter("rId"));
            roleResource.setCreateTime(new Date());
            int roleResourceList = roleService.insertRoleResource(roleResource);
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
        FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
    }
}
