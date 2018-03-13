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
import org.springframework.web.bind.annotation.RequestParam;

import cn.financial.model.Organization;
import cn.financial.service.OrganizationService;
import cn.financial.util.UuidUtil;

/**
 * 组织结构相关操作
 * 
 * @author zlf 2018/3/9
 *
 */
@Controller
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    /**
     * 新增组织结构
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/organization/save", method = RequestMethod.POST)
    public Map<String, Object> saveOrganization(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String orgName = request.getParameter("orgName");
            if (orgName != null && !"".equals(orgName)) {
                orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
            }
            Organization organization = new Organization();
            organization.setId(UuidUtil.getUUID());// 组织结构id
            organization.setOrgName(orgName);// 组织架构名
            organization.setuId(request.getParameter("uId"));// 提交人id
            organization.setParentId(request.getParameter("parentId"));// 父id
            organization.setCreateTime(new Date());// 创建时间
            Integer i = organizationService.saveOrganization(organization);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 查询所有的组织结构信息
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/organization/list", method = RequestMethod.POST)
    public Map<String, Object> listOrganization(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<Organization> list = organizationService.listOrganization();
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", list);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据条件查询组织结构信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/organization/listBy", method = RequestMethod.POST)
    public Map<String, Object> listOrganizationBy(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String orgName = request.getParameter("orgName");
            String createTime = request.getParameter("createTime");
            String updateTime = request.getParameter("updateTime");
            Date createTimeOfDate = null;
            Date updateTimeOfDate = null;
            if (orgName != null && !"".equals(orgName)) {
                orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
            }
            if (createTime != null && !"".equals(createTime)) {
                createTimeOfDate = dateFormat.parse(createTime);
            }
            if (updateTime != null && !"".equals(updateTime)) {
                updateTimeOfDate = dateFormat.parse(updateTime);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("id", request.getParameter("id")); // 组织结构id
            map.put("orgName", orgName);// 组织架构名
            map.put("uId", request.getParameter("uId"));// 提交人id
            map.put("parentId", request.getParameter("parentId"));// 父id
            map.put("createTime", createTimeOfDate);// 创建时间
            map.put("updateTime", updateTimeOfDate);// 更新时间
            List<Organization> list = organizationService.listOrganizationBy(map);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", list);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据ID查询组织结构信息
     * 
     * @param request
     * @param id
     *            要查询组织结构的ID（required = true，必须存在）
     * @return
     */
    @RequestMapping(value = "/organization/getbyid", method = RequestMethod.POST)
    public Map<String, Object> getOrganizationById(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Organization organization = organizationService.getOrganizationById(id);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", organization);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     * 
     * @param request
     * @param response
     * @param id
     *            组织结构id（required = true，必须存在）
     * @return
     */
    @RequestMapping(value = "/organization/updatebyid", method = RequestMethod.POST)
    public Map<String, Object> updateOrganizationById(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "id", required = true) String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String orgName = request.getParameter("orgName");
            if (orgName != null && !"".equals(orgName)) {
                orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("id", id);// 组织id
            map.put("orgName", orgName);// 组织架构名
            map.put("uId", request.getParameter("uId"));// 提交人id
            map.put("parentId", request.getParameter("parentId"));// 父id
            map.put("updateTime", new Date());// 更新时间
            Integer i = organizationService.updateOrganizationById(map);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "修改成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "修改失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 伪删除 <根据组织结构ID修改状态为0，即已删除>
     * 
     * @param request
     * @param id
     *            传入的组织结构id（required = true，必须存在）
     * @return
     */
    @RequestMapping(value = "/organization/deletebystatus", method = RequestMethod.POST)
    public Map<Object, Object> deleteOrganizationByStatus(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = organizationService.deleteOrganizationByStatus(id);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据条件删除组织结构信息 （这个方法已标记为过时，请使用deleteOrganizationByStatus方法来删除）
     * 
     * @param request
     * @param id
     *            传入的组织结构id（required = true，必须存在）
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/organization/deletebyid", method = RequestMethod.POST)
    public Map<Object, Object> deleteOrganizationById(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = organizationService.deleteOrganizationById(id);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
