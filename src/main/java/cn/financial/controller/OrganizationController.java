package cn.financial.controller;

import java.text.SimpleDateFormat;
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
        try {
            String uuid = UuidUtil.getUUID();
            String orgName = request.getParameter("orgName");
            String parentOrgId = request.getParameter("parentOrgId");
            if (orgName != null && !"".equals(orgName)) {
                orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
            }
            Organization organization = new Organization();
            organization.setId(uuid);// 组织结构id
            organization.setOrgName(orgName);// 组织架构名
            organization.setuId(request.getParameter("uId"));// 提交人id
            // 新增的时候这里保存的是此节点的code
            Integer i = organizationService.saveOrganization(organization, parentOrgId);
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
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
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
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/listBy", method = RequestMethod.POST)
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
            map.put("code", request.getParameter("code"));// 该组织机构节点的序号
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
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/getbyid", method = RequestMethod.POST)
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
    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/updatebyid", method = RequestMethod.POST)
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
            map.put("code", request.getParameter("code"));// 该组织机构节点的序号
            map.put("orgName", orgName);// 组织架构名
            map.put("uId", request.getParameter("uId"));// 提交人id
            map.put("parentId", request.getParameter("parentId"));// 父id
            map.put("his_permission", request.getParameter("his_permission"));// 历史权限记录
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
     * 伪删除 <根据组织结构ID修改状态为0，即已删除>,停用(单条停用)
     * 
     * @param request
     * @param id
     *            传入的组织结构id（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/deletebystatus", method = RequestMethod.POST)
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
     * 伪删除 <根据组织结构ID修改状态为0，即已删除>,停用(级联停用，将此节点下的所有子节点停用)
     * 
     * @param request
     * @param id
     *            传入的组织结构id（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("organization:update")
    @RequestMapping(value = "/deletebycascade", method = RequestMethod.POST)
    public Map<Object, Object> deleteOrganizationByStatusCascade(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = organizationService.deleteOrganizationByStatusCascade(id);
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
    @RequiresPermissions("organization:update")
    @Deprecated
    @RequestMapping(value = "/deletebyid", method = RequestMethod.POST)
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

    /**
     * 传入一个节点的id,或者name，查询所有该节点的子节点,构建tree的string字符串
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/getsubnode", method = RequestMethod.POST)
    public Map<Object, Object> getSubnode(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", request.getParameter("id"));// 组织id
        map.put("orgName", request.getParameter("orgName"));// 组织架构名
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            String jsonTree = organizationService.listTreeByNameOrIdForSon(map);
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
     * 传入一个节点的id,或者name，查询所有该节点的父节点
     * 
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/getparnode", method = RequestMethod.POST)
    public Map<Object, Object> getParnode(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", request.getParameter("id"));// 组织id
        map.put("orgName", request.getParameter("orgName"));// 组织架构名
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            List<Organization> list = organizationService.listTreeByNameOrIdForParent(map);
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
    @RequiresPermissions({"organization:update","organization:create"})
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public Map<Object, Object> moveOrganization(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        dataMap.put("resultCode", 200);
        dataMap.put("resultDesc", "移动失败");
        String id = request.getParameter("id");
        String parentOrgId = request.getParameter("parentOrgId");
        if (id == null && "".equals(id)) {
            return dataMap;
        }
        if (parentOrgId == null && "".equals(parentOrgId)) {
            return dataMap;
        }
        try {
            Integer i = organizationService.moveOrganization(id, parentOrgId);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "移动成功!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

}
