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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @RequestMapping("/organization/save")
    public Map<String, Object> saveOrganization(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        // 组织结构id
        String id = UuidUtil.getUUID();
        // 组织架构名
        String orgName = request.getParameter("orgName");
        // 提交人id
        String uId = request.getParameter("uId");
        // 父id
        String parentId = request.getParameter("parentId");
        // 创建时间
        String createTime = request.getParameter("createTime");
        // 更新时间
        String updateTime = request.getParameter("updateTime");
        Date createTimeOfDate = null;
        Date updateTimeOfDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (createTime != null && !"".equals(createTime)) {
                createTimeOfDate = dateFormat.parse(createTime);
            }
            if (updateTime != null && !"".equals(updateTime)) {
                updateTimeOfDate = dateFormat.parse(updateTime);
            }
            orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
            Organization organization = new Organization();
            organization.setId(id);
            organization.setOrgName(orgName);
            organization.setuId(uId);
            organization.setParentId(parentId);
            organization.setCreateTime(createTimeOfDate);
            organization.setUpdateTime(updateTimeOfDate);
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
    @RequestMapping("/organization/list")
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
     * @param id
     *            组织结构id
     * @param orgName
     *            组织架构名
     * @param uId
     *            提交人id
     * @param parentId
     *            父id
     * @param createTime
     *            创建时间
     * @param updateTime
     *            更新时间
     * @return
     */
    @RequestMapping("/organization/listBy")
    public Map<String, Object> listOrganizationBy(HttpServletRequest request, HttpServletResponse response, String id,
            String orgName, String uId, String parentId, Date createTime, Date updateTime) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Map<Object, Object> map = new HashMap<>(6);
            map.put("id", id);
            map.put("orgName", orgName);
            map.put("uId", uId);
            map.put("parentId", parentId);
            map.put("createTime", createTime);
            map.put("updateTime", updateTime);
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
     *            要查询组织结构的ID
     * @return
     */
    @RequestMapping("/organization/get")
    public Map<String, Object> getOrganization(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Organization organization = organizationService.getOrganization(id);
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
     * @param orgName
     *            组织架构名
     * @param uId
     *            提交人id
     * @param parentId
     *            父id
     * @param createTime
     *            创建时间
     * @param updateTime
     *            更新时间
     * @return
     */
    @RequestMapping("/organization/update")
    public Map<String, Object> updateOrganization(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "id", required = true) String id, String orgName, String uId, String parentId,
            Date createTime, Date updateTime) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Map<Object, Object> map = new HashMap<>(6);
            map.put("id", id);
            map.put("orgName", orgName);
            map.put("uId", uId);
            map.put("parentId", parentId);
            map.put("createTime", createTime);
            map.put("updateTime", updateTime);
            Integer i = organizationService.updateOrganization(map);
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
     *            传入的组织结构id
     * @return
     */
    @RequestMapping("/organization/deletebystatus")
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
     *            传入的组织结构id
     * @return
     */
    @Deprecated
    @RequestMapping("/organization/delete")
    public Map<Object, Object> deleteOrganization(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = organizationService.deleteOrganization(id);
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
     * 格式化controller方法的参数接收的日期类型
     * @param binder
     */
    @org.springframework.web.bind.annotation.InitBinder
    public void InitBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
    }

}
