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

import cn.financial.model.UserOrganization;
import cn.financial.service.impl.UserOrganizationServiceImpl;
import cn.financial.util.FileUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
@Controller
public class UserOrganizationController {
    @Autowired
    UserOrganizationServiceImpl userOrganizationService;
    
    protected Logger logger = LoggerFactory.getLogger(UserOrganizationController.class);
    /**
     * 查询所有
     * @param request
     * @param response
     */
    @RequestMapping(value = "/userOrganization/index", method = RequestMethod.POST)
    public void listUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
    	try {
            List<UserOrganization> userOrganization = userOrganizationService.listUserOrganization("uId");
            dataMap.put("userOrganizationList", userOrganization);
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
     */
    @RequestMapping(value = "/userOrganization/insert", method = RequestMethod.POST)
    public void insertUserOrganization(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            UserOrganization userOrganization = new UserOrganization();
            userOrganization.setId(UuidUtil.getUUID());
            userOrganization.setoId(request.getParameter("oId"));
            userOrganization.setuId(request.getParameter("uId"));
            userOrganization.setCreateTime(new Date());
            int userOrganizationList = userOrganizationService.insertUserOrganization(userOrganization);
            if(userOrganizationList>0){
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
