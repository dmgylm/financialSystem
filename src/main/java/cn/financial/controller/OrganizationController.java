package cn.financial.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.response.Oganization;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.OrganizationService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.UuidUtil;

/**
 * 组织结构相关操作
 * 
 * @author zlf 2018/3/9
 *
 */
@Api(tags = "组织结构相关操作")
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
     *   然后根据算法生成第三个 ）
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:create"},logical=Logical.OR)
    @ApiOperation(value = "新增组织结构",notes = "新增组织结构",response=ResultUtils.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgName", value = "组织架构名", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgType", value = "类别（汇总，公司，部门）", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "parentOrgId", value = "父节点", required = false),
    })
    @PostMapping(value = "/save")
    public ResultUtils saveOrganization(String orgName,String orgType,
    		String parentOrgId,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        ResultUtils result=new ResultUtils();
        Integer i = 0;
        try {
            Organization organization = new Organization();
            String uuid = UuidUtil.getUUID();
            organization.setId(uuid);// 组织结构id
            if (null != orgName && !"".equals(orgName)) {
                organization.setOrgName(orgName.toString().trim());// 组织架构名
            }
            if (null != orgType && !"".equals(orgType)) {
                organization.setOrgType(Integer.parseInt(orgType.toString().trim()));// 类别（汇总，公司，部门）
            }
            User user = (User) request.getAttribute("user");
            organization.setuId(user.getId());// 提交人id
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                // 新增的时候这里保存的是此节点的code
                i = organizationService.saveOrganization(organization,parentOrgId);
            }
            if (Integer.valueOf(1).equals(i)) {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据条件查询组织结构信息。如果存在参数，则根据传递的参数查询相应的节点信息；如果参数不存在，则查询所有节点的信息
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:view"},logical=Logical.OR)
    @ApiOperation(value = "根据条件查询组织结构信息",notes = "根据条件查询组织结构信息",response=Oganization.class)
    @PostMapping(value="/listBy")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgName", value = "组织架构名", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "createTime", value = "创建时间", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "updateTime", value = "更新时间", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织结构id", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "code", value = "该组织机构节点的序号", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "uId", value = "提交人id", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "parentId", value = "父id", required = false),
    })
    public Oganization listOrganizationBy(String orgName,String createTime,String updateTime,
    		String id,String code,String uId, String parentId,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Oganization organiza=new Oganization();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Map<Object, Object> map = new HashMap<>();
            if (null !=orgName && !"".equals(orgName)) {
                map.put("orgName", orgName.trim().toString());//组织架构名
            }
            if (null !=createTime && !"".equals(createTime)) {
                map.put("createTime", createTime);//创建时间
            }
            if (null != updateTime && !"".equals(updateTime)) {
                map.put("updateTime", updateTime);//更新时间
            }
            if (null !=id && !"".equals(id)) {
                map.put("id", id); // 组织结构id
            }
            if (null !=code && !"".equals(code)) {
                map.put("code", code);// 该组织机构节点的序号
            }
            if (null != uId && !"".equals(uId)) {
                map.put("uId",uId);// 提交人id
            }
            if (null != parentId && !"".equals(parentId)) {
                map.put("parentId", parentId);// 父id
            }
            List<Organization> list = organizationService.listOrganizationBy(map);
            if (!CollectionUtils.isEmpty(list)) {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,organiza);
                //dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                //dataMap.put("data", list);
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return organiza;
    }

    /**
     * 根据id修改组织结构信息,必要参数Id
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:update"},logical=Logical.OR)
    @ApiOperation(value = "根据id修改组织结构信息",notes = "根据id修改组织结构信息",response=ResultUtils.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgName", value = "组织架构名", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgType", value = "类别（汇总，公司，部门）", required = false),
    	})
    @PostMapping(value = "/updateByid")
    public Map<String, Object> updateOrganizationById(String id,String orgName,String orgType, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Map<Object, Object> map = new HashMap<>();
            if (null !=orgName && !"".equals(orgName)) {
                map.put("orgName",orgName.trim().toString());//组织架构名
            }
            if (null !=id && !"".equals(id)) {
                map.put("id", id);// 组织id
            }
            if (null !=orgType && !"".equals(orgType)) {
                map.put("orgType", orgType);// 类别（汇总，公司，部门）
            }
            User user = (User) request.getAttribute("user");
            map.put("uId", user.getId());
            Integer i = organizationService.updateOrganizationById(map);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 停用(先判断此节点下是否存在未停用的子节点，若存在，则返回先删除子节点;否则继续停用此节点)，根据组织结构ID修改状态为0，即已停用
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:stop"},logical=Logical.OR)
    @ApiOperation(value = "根据组织结构ID修改状态为停用",notes = "根据组织结构ID修改状态为停用",response=ResultUtils.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = true),
    	})
    @PostMapping(value = "/disconTinuate")
    public Map<Object, Object> deleteOrganizationByStatusCascade(String id,HttpServletRequest request) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = 0;
            User user = (User) request.getAttribute("user");
            if (null !=id && !"".equals(id)) {
                // 这里判断此节点下是否存在没有被停用的节点。
                HashMap<Object, Object> mmp = new HashMap<Object, Object>();
                mmp.put("id", request.getParameter("id"));
                Boolean boolean1 = organizationService.hasOrganizationSon(mmp);
                if (!boolean1) {
                    i = organizationService.deleteOrganizationById(request.getParameter("id"), user);
                    if (Integer.valueOf(1).equals(i)) {
                        dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    } else {
                        dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                    }
                } else {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.ORGANIZA_DELEFALSE));
                }
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据id查询所有该节点的子节点,构建tree的string字符串
     * <p>
     * 如果id存在，则查询改id所有子节点，构成树结构
     * <p>
     * 如果id不存在，则查询全部节点，构成树结构
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:view"},logical=Logical.OR)
    @ApiOperation(value = "根据id查询所有该节点的子节点",notes = "根据id查询所有该节点的子节点",response=Oganization.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = true),
    	})
    @PostMapping(value = "/getSubnode")
    public Map<Object, Object> getSubnode(String id,HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            JSONObject jsonTree = new JSONObject();
            if (null !=id && !"".equals(id)) {
                id =id;
            }
            jsonTree = organizationService.TreeByIdForSon(id);
            if (jsonTree != null) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("data", jsonTree);
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据id查询所有该节点的所有父节点
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:view"},logical=Logical.OR)
    @ApiOperation(value = "根据id查询所有该节点的所有父节点",notes = "根据id查询所有该节点的所有父节点",response=Oganization.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = true),
    	})
    @PostMapping(value = "/getParnode")
    public Map<Object, Object> getParnode(String id,HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            List<Organization> list = null;
            if (null != id && !"".equals(id)) {
                list = organizationService.listTreeByIdForParent(request.getParameter("id"));
            }
            if (!CollectionUtils.isEmpty(list)) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("data", list);
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
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
    @ResponseBody
    @RequiresPermissions(value={ "organization:update", "organization:create" },logical=Logical.OR)
    @ApiOperation(value = "移动组织机构",notes = "移动组织机构",response=ResultUtils.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "parentId", value = "父id", required = false),
    	})
    @PostMapping(value = "/move")
    public Map<Object, Object> moveOrganization(String id,String parentId,HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        String parentOrgId = null;
        try {
            if (null != id && !"".equals(id)) {
                id =id;
            }
            if (null !=parentId && !"".equals(parentId)) {
                parentOrgId =parentId;
            }
            User user = (User) request.getAttribute("user");
            Integer i = organizationService.moveOrganization(user, id, parentOrgId);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            } else {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据条件判断是否该节点存在子节点
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:view" },logical=Logical.OR)
    @ApiOperation(value = "根据条件判断是否该节点存在子节点",notes = "根据条件判断是否该节点存在子节点",response=Oganization.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgName", value = "组织架构名", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "id", required = true),
    	})
    @PostMapping(value = "/hasSon")
    public Map<Object, Object> hasOrganizationSon(String id,String orgName,HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Map<Object, Object> map = new HashMap<>();
            if (null !=orgName && !"".equals(orgName)) {
                map.put("orgName",orgName.trim().toString());//new String(request.getParameter("orgName").getBytes("ISO-8859-1"), "UTF-8"));
            }
            if (null != id && !"".equals(id)) {
                map.put("id", id);
            }
            Boolean flag = organizationService.hasOrganizationSon(map);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("data", flag);
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

}
