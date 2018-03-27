package cn.financial.controller;

import java.text.SimpleDateFormat;
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
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    /**
     * 新增组织结构
     * 
     * @ code是根据父节点找到其下子节点，然后根据子节点的序号，往后排。（比如01子节点有0101和0102，那么需要查到这两个，
     * 然后根据算法生成第三个 ）
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:create")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Map<String, Object> saveOrganization(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Integer i = 0;
        try {
            Organization organization = new Organization();
            String uuid = UuidUtil.getUUID();
            organization.setId(uuid);// 组织结构id
            if (null != request.getParameter("orgName") && !"".equals(request.getParameter("orgName"))) {
                organization.setOrgName(new String(request.getParameter("orgName").getBytes("ISO-8859-1"), "UTF-8"));// 组织架构名
            }
            if (null != request.getParameter("uId") && !"".equals(request.getParameter("uId"))) {
                organization.setuId(request.getParameter("uId"));// 提交人id
            }
            if (null != request.getParameter("parentOrgId") && !"".equals(request.getParameter("parentOrgId"))) {
                // 新增的时候这里保存的是此节点的code
                i = organizationService.saveOrganization(organization, request.getParameter("parentOrgId"));
            }
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
     * 根据条件查询组织结构信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/listBy", method = RequestMethod.GET)
    public Map<String, Object> listOrganizationBy(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Map<Object, Object> map = new HashMap<>();
            if (null != request.getParameter("orgName") && !"".equals(request.getParameter("orgName"))) {
                map.put("orgName", new String(request.getParameter("orgName").getBytes("ISO-8859-1"), "UTF-8"));// 组织架构名
            }
            if (null != request.getParameter("createTime") && !"".equals(request.getParameter("createTime"))) {
                map.put("createTime", format.parse(request.getParameter("createTime")));// 创建时间
            }
            if (null != request.getParameter("updateTime") && !"".equals(request.getParameter("updateTime"))) {
                map.put("updateTime", format.parse(request.getParameter("updateTime")));// 更新时间
            }
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                map.put("id", request.getParameter("id")); // 组织结构id
            }
            if (null != request.getParameter("code") && !"".equals(request.getParameter("code"))) {
                map.put("code", request.getParameter("code"));// 该组织机构节点的序号
            }
            if (null != request.getParameter("uId") && !"".equals(request.getParameter("uId"))) {
                map.put("uId", request.getParameter("uId"));// 提交人id
            }
            if (null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId"))) {
                map.put("parentId", request.getParameter("parentId"));// 父id
            }
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
     * 根据id修改组织结构信息,必要参数Id
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/updatebyid", method = RequestMethod.POST)
    public Map<String, Object> updateOrganizationById(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Map<Object, Object> map = new HashMap<>();
            if (null != request.getParameter("orgName") && !"".equals(request.getParameter("orgName"))) {
                map.put("orgName", new String(request.getParameter("orgName").getBytes("ISO-8859-1"), "UTF-8"));// 组织架构名
            }
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                map.put("id", request.getParameter("id"));// 组织id
            }
            if (null != request.getParameter("code") && !"".equals(request.getParameter("code"))) {
                map.put("code", request.getParameter("code"));// 该组织机构节点的序号
            }
            if (null != request.getParameter("uId") && !"".equals(request.getParameter("uId"))) {
                map.put("uId", request.getParameter("uId"));// 提交人id
            }
            if (null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId"))) {
                map.put("parentId", request.getParameter("parentId"));// 父id
            }
            if (null != request.getParameter("his_permission") && !"".equals(request.getParameter("his_permission"))) {
                map.put("his_permission", request.getParameter("his_permission"));// 历史权限记录
            }
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
     * 停用(单条停用)，根据组织结构ID修改状态为0，即已停用
     * 
     * @param request
     * @return
     */
    @RequiresPermissions("organization:stop")
    @RequestMapping(value = "/deletebystatus", method = RequestMethod.POST)
    public Map<Object, Object> deleteOrganizationByStatus(HttpServletRequest request) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = 0;
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                i = organizationService.deleteOrganizationByStatus(request.getParameter("id"));
            }
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "停用成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "停用失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 停用(级联停用，将此节点下的所有子节点停用)，根据组织结构ID修改状态为0，即已停用
     * 
     * @param request
     * @return
     */
    @RequiresPermissions("organization:stop")
    @RequestMapping(value = "/deletebycascade", method = RequestMethod.POST)
    public Map<Object, Object> deleteOrganizationByStatusCascade(HttpServletRequest request) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = 0;
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                i = organizationService.deleteOrganizationByStatusCascade(request.getParameter("id"));
            }
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "停用成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "停用失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据id查询所有该节点的子节点,构建tree的string字符串
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/getsubnode", method = RequestMethod.POST)
    public Map<Object, Object> getSubnode(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            String jsonTree = "";
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                jsonTree = organizationService.TreeByIdForSon(request.getParameter("id"));
            }
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", jsonTree);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据id查询所有该节点的父节点
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/getparnode", method = RequestMethod.POST)
    public Map<Object, Object> getParnode(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            List<Organization> list = null;
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                list = organizationService.listTreeByIdForParent(request.getParameter("id"));
            }
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
     * 移动组织机构
     * 
     * @ 将要移动的原组织机构极其下所有子节点都停用（status=0），将 要移动到的节点成为父节点，原来组织上的所有节点按原来的架构都新增到该节点下
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions({ "organization:update", "organization:create" })
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public Map<Object, Object> moveOrganization(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        String id = null;
        String parentOrgId = null;
        try {
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                id = request.getParameter("id");
            }
            if (null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId"))) {
                parentOrgId = request.getParameter("parentId");
            }
            Integer i = organizationService.moveOrganization(id, parentOrgId);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "移动成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "移动失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据id或者name判断是否该节点存在子节点（这里的name必须是公司名称，查询该公司是否有部门； 其他节点只能通过id查询）
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/hasSon", method = RequestMethod.POST)
    public Map<Object, Object> hasOrganizationSon(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Map<Object, Object> map = new HashMap<>();
            if (null != request.getParameter("orgName") && !"".equals(request.getParameter("orgName"))) {
                map.put("orgName", new String(request.getParameter("orgName").getBytes("ISO-8859-1"), "UTF-8"));
            }
            if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                map.put("id", request.getParameter("id"));
            }
            Boolean flag = organizationService.hasOrganizationSon(map);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", flag);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

}
