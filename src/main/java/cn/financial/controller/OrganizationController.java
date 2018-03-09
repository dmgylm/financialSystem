package cn.financial.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.financial.model.Organization;
import cn.financial.service.OrganizationService;
import cn.financial.util.UuidUtil;

/**
 * 组织结构相关操作
 * 
 * @author zlf 2018-03-07
 *
 */
@Controller
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    /**
     * 新增组织结构
     * 
     * @param request
     * @param orgName
     *            组织架构名
     * @param uId
     *            提交人id
     * @param parentId
     *            父id
     * @param createTimeOfDate
     *            创建时间
     * @param updateTimeOfDate
     *            更新时间
     * @return
     */
    @RequestMapping("/organization/save")
    public Map<String, Object> saveOrganization(HttpServletRequest request, String orgName, String uId,
            String parentId, Date createTimeOfDate, Date updateTimeOfDate) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String id = UuidUtil.getUUID();
        try {
            orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
            uId = new String(uId.getBytes("ISO-8859-1"), "UTF-8");
            parentId = new String(parentId.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Organization organization = new Organization();
        organization.setId(id);
        organization.setOrgName(orgName);
        organization.setuId(uId);
        organization.setParentId(parentId);
        organization.setCreateTime(createTimeOfDate);
        organization.setUpdateTime(updateTimeOfDate);
        organization.setStatus(1);
        try {
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
        }
        return dataMap;
    }

    /**
     * 根据条件查询组织结构信息
     * 
     * @param request
     * @param map
     *            根据传入的map查询相应的组织结构
     * @return
     */
    @RequestMapping("/organization/listBy")
    public Map<String, Object> listOrganizationBy(HttpServletRequest request, Map<Object, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            List<Organization> list = organizationService.listOrganizationBy(map);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", list);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
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
    public Map<String, Object> getOrganization(HttpServletRequest request, String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Organization organization = organizationService.getOrganization(id);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", organization);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
        }
        return dataMap;
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     * 
     * @param request
     * @param map
     *            传递的map必须包含有组织结构的ID
     * @return
     */
    @RequestMapping("/organization/update")
    public Map<String, Object> updateOrganization(HttpServletRequest request, Map<Object, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
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
    public Map<Object, Object> deleteOrganizationByStatus(HttpServletRequest request, String id) {
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
    public Map<Object, Object> deleteOrganization(HttpServletRequest request, String id) {
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
        }
        return dataMap;
    }

}
