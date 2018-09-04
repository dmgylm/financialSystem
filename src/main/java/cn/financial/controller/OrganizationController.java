package cn.financial.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.response.ChildrenObject;
import cn.financial.model.response.OganizationNode;
import cn.financial.model.response.OrangizaSubnode;
import cn.financial.model.response.OrganizaHason;
import cn.financial.model.response.OrganizaParnode;
import cn.financial.model.response.OrganizaResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.service.BusinessDataService;
import cn.financial.service.MessageService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.SiteConst;
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
    
	@Autowired
	private BusinessDataService businessDataService;
	
	@Autowired
	private BusinessDataInfoService businessDataInfoService;
	@Autowired
	private DataModuleServiceImpl dataModuleServiceImpl;
	@Autowired
	private UserOrganizationService userOrganizationService;
	@Autowired
	private BusinessDataInfoServiceImpl businessDataInfoServices;
	@Autowired
	private OrganizationServiceImpl organizationServices;
	@Autowired
	private BusinessDataServiceImpl businessDataServices;
	@Autowired
	private  MessageService messageService;

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
    	@ApiImplicitParam(paramType="query", dataType = "int", name = "orgType", value = "类别（1:汇总，2:公司，3:部门,4:板块）", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "parentOrgId", value = "父节点", required = true),
    })
    @PostMapping(value = "/save")
    public ResultUtils saveOrganization(String orgName,Integer orgType,
    		String parentOrgId,HttpServletRequest request, HttpServletResponse response) {
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
            JSONObject json=organizationService.TreeByIdForSon(parentOrgId);
            JSONArray jsonlist=JSONArray.parseArray(json.get("children").toString());
            for(int z=0;z<jsonlist.size();z++){
               JSONObject jsonss=JSONObject.parseObject(jsonlist.get(z).toString());
               if(jsonss.get("name").equals(orgName)){
            	   ElementXMLUtils.returnValue(ElementConfig.NAMELY_NOSAME,result);
         	       return result;
               }
          	
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {                     
            List<Organization> lists =organizationService.listTreeByIdForParent(parentOrgId);
            for(Organization ll:lists){
             	int typeList=ll.getOrgType();
            	if(orgType==2&&typeList==orgType){
            		 ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY,result);
           	         return result;
                 }
            	if(orgType==4&&typeList==orgType){
              	     ElementXMLUtils.returnValue(ElementConfig.PLATE_PLATELEVEL,result);
         	         return result;
                }
              }
            int parentOrgType=lists.get(0).getOrgType();

                // 新增的时候这里保存的是此节点的code
                if(parentOrgType==3){
               	      ElementXMLUtils.returnValue(ElementConfig.DEPER_REMOVE,result);
            	      return result;
              	} 
               if(orgType==3){//部门级别
            		int sum=0;
               	    for(int z=0;z<lists.size();z++){
                		   int num=lists.get(z).getOrgType();
                		     if(num==2){//公司级别
                			  sum++;
                		     }
                       }
               	          if(sum==0){
          	    	         ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY,result);
          	                 return result;
          	               }
            	     }
            	  if(orgType==4){
                 	   int sum=0;
                 	    for(int z=0;z<lists.size();z++){
                		     int num=lists.get(z).getOrgType();
                		      if(num==2){//判断父级是公司的数量
                			    sum++;
                		       }
                  	      }
                 	           if(sum>0){
                 		          ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATELEVEL,result);
                	              return result;
                                 }
                       }
            	  if(orgType==2){
            		   int sum=0;
                 	   for(int z=0;z<lists.size();z++){
               		     int num=lists.get(z).getOrgType();
               		       if(num==4){//判断父级是板块的数量
               			     sum++;
               		       }
                 	 }
                 	 if(sum==0){
               	    	ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATE,result);
               	        return result;
               	     }
            	  }
               	 i = organizationService.saveOrganization(organization,parentOrgId);
            }
            if (Integer.valueOf(1).equals(i)) {
             	JSONObject jsonTree= organizationService.TreeByIdForSon(parentOrgId);
            	JSONArray jsonarray=JSONArray.parseArray(jsonTree.get("children").toString());
            	for(int z=0;z<jsonarray.size();z++){
            		 JSONObject jsonss=JSONObject.parseObject(jsonarray.get(z).toString());
            		 String name= jsonss.get("name").toString();
            		  if(orgType==3&&name.equals(orgName)){
            			  String reportType = DataModule.REPORT_TYPE_BUDGET;
            			  String pid=jsonss.get("pid").toString();
            			  List<Organization> orgDep = organizationService.listOrganizationBy(null,null,null,pid,null,null,null,3,null);
                    	  int year = Calendar.getInstance().get(Calendar.YEAR);
                    	  int month = Calendar.getInstance().get(Calendar.MONTH)+1;
                    	  Organization orgCompany = organizationService.getCompanyNameBySon(pid);// 获取对应部门的公司
                  		  Organization getOrgDep=orgDep.get(0);//部门
                  		  DataModule dm=dataModuleServiceImpl.getDataModule(reportType,orgDep.get(0).getOrgPlateId());
                  		  businessDataService.createBusinessData(getOrgDep, year, month, dm, logger, businessDataServices, businessDataInfoServices, organizationServices);
                  		  businessDataService.createBunsinessDataMessage(year,logger,orgCompany,getOrgDep,messageService);
                    	  ElementXMLUtils.returnValue(ElementConfig.BUDGET_GENERATE,result);
                       }
 
            		  else{
                 		 ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                 	   }
            	}

            	
            	
            } else {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
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
    @ApiOperation(value = "根据条件查询组织结构信息",notes = "根据条件查询组织结构信息",response=OganizationNode.class)
    @PostMapping(value="/listBy")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgName", value = "组织架构名", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "createTime", value = "创建时间", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "updateTime", value = "更新时间", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织结构id", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "code", value = "该组织机构节点的序号", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "uId", value = "提交人id", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "parentId", value = "父id", required = false),
    	@ApiImplicitParam(paramType="query", dataType = "int", name = "orgType", value = "1：汇总，2：公司，3：部门 ，4：板块(默认是汇总)", required = false),
    })
    public OganizationNode listOrganizationBy(String orgName,String createTime,String updateTime,
    		String id,String code,String uId, String parentId,Integer orgType,HttpServletRequest request, HttpServletResponse response) {
        OganizationNode organiza=new OganizationNode();
        try {
             List<Organization> list = organizationService.
            	listOrganizationBy(orgName,createTime,updateTime,id,code,uId,parentId,orgType,null);
          // if (!CollectionUtils.isEmpty(list)) {
            	organiza.setData(list);
            	ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,organiza);
           /* } else {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,organiza);
            }*/
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,organiza);
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
    	@ApiImplicitParam(paramType="query", dataType = "int", name = "orgType", value = "类别（1：汇总:2：公司:3：部门,4:板块）", required = false),
    	})
    @PostMapping(value = "/updateByid")
    public ResultUtils updateOrganizationById(String id,String orgName,Integer orgType, HttpServletRequest request, HttpServletResponse response) {
        ResultUtils result=new ResultUtils();
        try {
            User user = (User) request.getAttribute("user");
            List<Organization> list = organizationService.listOrganizationBy(null,null,null,id,null,null,null,null,null);
        	if(list.get(0).getOrgType()==2){
        		 int count= organizationService.TreeByIdForSonList(id);
        		 if(count==1&&orgType!=2){
        			 ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY,result);
           	         return result;
        		 }
        	}
        	if(list.get(0).getOrgType()!=2){
       		 int count= organizationService.TreeByIdForSonSum(id);
       		 if(count==1&&orgType==2){//修改为公司节点，看子节点有没有公司判断
       			 ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY,result);
      	         return result;
       		 }
       	   }
            List<Organization> lists =organizationService.listTreeByIdForParent(id);
            int parentOrgType=lists.get(1).getOrgType();//父节点
            String parentOrgId=lists.get(1).getId();
            List<Organization> listype =organizationService.listTreeByIdForParent(parentOrgId);
            for(Organization ll:listype){
             	int typeList=ll.getOrgType();
            	if(orgType==2&&typeList==orgType){//修改为公司节点，看父节点有没有公司判断
            		 ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY,result);
           	         return result;
                 }
            	if(orgType==4&&typeList==orgType){
              	     ElementXMLUtils.returnValue(ElementConfig.PLATE_PLATELEVEL,result);
         	         return result;
                }
              }
            JSONObject json=organizationService.TreeByIdForSon(parentOrgId);
            JSONArray jsonlist=JSONArray.parseArray(json.get("children").toString());
            for(int z=0;z<jsonlist.size();z++){
               JSONObject jsonss=JSONObject.parseObject(jsonlist.get(z).toString());
               if(jsonss.get("name").equals(orgName)){
            	   ElementXMLUtils.returnValue(ElementConfig.NAMELY_NOSAME,result);
         	       return result;
               }
          	
            }
           if(parentOrgType==3){
         	      ElementXMLUtils.returnValue(ElementConfig.DEPER_REMOVE,result);
      	          return result;
        	} 

            if(orgType==3){//部门
            	 int sum=0;
         		 for(int i=0;i<lists.size();i++){
           		    int num=lists.get(i).getOrgType();
           		     if(num==2){//代表有公司
           			  sum++;
           		    }
         		  }
         		   if(sum==0){
              	       ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY,result);
              	       return result;
              	     }
                }
              if(orgType==2){//公司
            	    int sum=0;
             	    for(int i=0;i<lists.size();i++){
           		     int num=lists.get(i).getOrgType();
           		        if(num==4){//判断父级是板块的数量
           			      sum++;
           		         }
             	       }
             	      if(sum==0){
           	    	    ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATE,result);
           	            return result;
           	          }
              }  
              if(orgType==4){
                 	int sum=0;
           	        for(int i=0;i<lists.size();i++){
          		      int num=lists.get(i).getOrgType();
          		       if(num==2){//判断父级是公司的数量
          			     sum++;
          		        }
            	     }
           	          if(sum>0){
           		           ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATELEVEL,result);
          	               return result;
           	           }
                }
            Integer i = organizationService.updateOrganizationById(orgName,id,orgType,user.getId());
            if (Integer.valueOf(1).equals(i)) {
            	 if(list.get(0).getOrgType()!=3&&orgType==3){         		
            		  String reportType = DataModule.REPORT_TYPE_BUDGET;
                 	  List<Organization> orgDep = organizationService.listOrganizationBy(null,null,null,id,null,null,null,3,null);
                	  int year = Calendar.getInstance().get(Calendar.YEAR);
                	  int month = Calendar.getInstance().get(Calendar.MONTH)+1;
                	  Organization orgCompany = organizationService.getCompanyNameBySon(id);// 获取对应部门的公司
              		  MessageService messageService = null;
              		  Organization getOrgDep=orgDep.get(0);//部门
              		  DataModule dm=dataModuleServiceImpl.getDataModule(reportType,orgDep.get(0).getOrgPlateId());
              		  businessDataService.createBusinessData(getOrgDep, year, month, dm, logger, businessDataServices, businessDataInfoServices, organizationServices);
              		  businessDataService.createBunsinessDataMessage(year,logger,orgCompany,getOrgDep,messageService); 
              		  ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                 	  return result;
            	 }
            	 else{
            	     ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
               	     return result;
            	 }
            	
       			
            } else {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
            	return result;
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
            this.logger.error(e.getMessage(), e);
            return result;
        }
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
    public ResultUtils deleteOrganizationByStatusCascade(String id,HttpServletRequest request) {
        ResultUtils result=new ResultUtils();
        try {
            Integer i = 0;
            User user = (User) request.getAttribute("user");
            if (null !=id && !"".equals(id)) {
                // 这里判断此节点下是否存在没有被停用的节点。
                HashMap<Object, Object> mmp = new HashMap<Object, Object>();
                mmp.put("id", request.getParameter("id"));
                Calendar cal = Calendar.getInstance();
    		    //int day = cal.get(Calendar.DATE);
    		    int day=12;
                int start_time=SiteConst.MOVE_ORGANIZATION_START_TIME;
                int stop_time=SiteConst.MOVE_ORGANIZATION_STOP_TIME;
                if(day<start_time||day>stop_time){
                	ElementXMLUtils.returnValue(ElementConfig.MOBILE_ORGANIZATION_DISABLE,result);
                	return result;
                }
                Boolean boolean1 = organizationService.hasOrganizationSon(mmp);
                if (!boolean1) {
                    i = organizationService.deleteOrganizationById(request.getParameter("id"), user);
                    if (Integer.valueOf(1).equals(i)) {
                    	ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                    } else {
                    	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                    }
                } else {
                	ElementXMLUtils.returnValue(ElementConfig.ORGANIZA_DELEFALSE,result);
                }
            } else {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
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
    @ApiOperation(value = "根据id查询所有该节点的子节点",notes = "根据id查询所有该节点的子节点",response = OrangizaSubnode.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = false),
    	})
    @PostMapping(value = "/getSubnode")
    public  Map<Object, Object> getSubnode(String id,HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        OrangizaSubnode orgin=new OrangizaSubnode();
        OrganizaResult loginResult = new OrganizaResult();
        List<OrganizaResult> loginResultList = new ArrayList<>();
        try {
        	
       	 User user = (User) request.getAttribute("user");
       	 List<JSONObject> list=userOrganizationService.userOrganizationList(user.getId());
       	// JSONObject json=new JSONObject();
       	// json.put("data", list);
            JSONObject jsonTree = new JSONObject();
            if (null !=id && !"".equals(id)) {
                id =id;
            }
           // jsonTree= organizationService.TreeByIdForSon(id);
            List<ChildrenObject> ChildrenObject = new ArrayList<>();
            //OrganizaList.organiza(jsonTree, ChildrenObject);
            loginResult.setChildren(ChildrenObject);
            loginResultList.add(loginResult);
            orgin.setData(loginResultList);
          //  if (jsonTree != null) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("data", list);
          /*  } else {
               dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }*/
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
    @ApiOperation(value = "根据id查询所有该节点的所有父节点",notes = "根据id查询所有该节点的所有父节点",response=OrganizaParnode.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "组织id", required = true),
    	})
    @PostMapping(value = "/getParnode")
    public OrganizaParnode getParnode(String id,HttpServletRequest request, HttpServletResponse response) {
        OrganizaParnode node=new OrganizaParnode();
        try {
            List<Organization> list = null;
            if (null != id && !"".equals(id)) {
                list = organizationService.listTreeByIdForParent(request.getParameter("id"));
                node.setData(list);
            }
           // if (!CollectionUtils.isEmpty(list)) {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,node);
          /*  } else {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,node);
            }*/
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,node);
            this.logger.error(e.getMessage(), e);
        }
        return node;
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
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "parentId", value = "父id", required = true),
    	})
    @PostMapping(value = "/move")
    public ResultUtils moveOrganization(String id,String parentId,HttpServletRequest request, HttpServletResponse response) {
        ResultUtils result=new ResultUtils();
        String parentOrgId = null;
        try {
            if (null != id && !"".equals(id)) {
                id =id;
            }
            if (null !=parentId && !"".equals(parentId)) {
                parentOrgId =parentId;
            }
            User user = (User) request.getAttribute("user");
            Calendar cal = Calendar.getInstance();
		    //int day = cal.get(Calendar.DATE);
            int day=12;
            int start_time=SiteConst.MOVE_ORGANIZATION_START_TIME;
            int stop_time=SiteConst.MOVE_ORGANIZATION_STOP_TIME;
            if(day<start_time||day>stop_time){
            	ElementXMLUtils.returnValue(ElementConfig.MOBILE_ORGANIZATION_FAIL,result);
            	return result;
            }
            
           else{
                List<Organization> list = organizationService.listOrganizationBy(null,null,null,id,null,null,null,null,null);
                int orgType=list.get(0).getOrgType();//当前级别
                String orgName=list.get(0).getOrgName();
                JSONObject json=organizationService.TreeByIdForSon(parentOrgId);
                JSONArray jsonlist=JSONArray.parseArray(json.get("children").toString());
                for(int z=0;z<jsonlist.size();z++){
                   JSONObject jsonss=JSONObject.parseObject(jsonlist.get(z).toString());
                   if(jsonss.get("name").equals(orgName)){
                	   ElementXMLUtils.returnValue(ElementConfig.NAMELY_NOSAME,result);
             	       return result;
                   }
              	
                }
                List<Organization> lists =organizationService.listTreeByIdForParent(parentId);
                int parentOrgType=lists.get(0).getOrgType();//父节点级别节点
                if(parentOrgType==3){
             	      ElementXMLUtils.returnValue(ElementConfig.DEPER_REMOVE,result);
          	          return result;
            	} 
         		int count= organizationService.TreeByIdForSonSum(id);
         		int sums= organizationService.TreeByIdForSonList(id);
         		int assum=lists.size();
         		int acount=0;
                for(Organization ll:lists){
                 	int typeList=ll.getOrgType();
                	if(orgType==2&&typeList==orgType){
                		 ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY,result);
               	         return result;
                     }
                	if(orgType==4&&typeList==orgType){
                  	     ElementXMLUtils.returnValue(ElementConfig.PLATE_PLATELEVEL,result);
             	         return result;
                    }
                    if(typeList==2&&orgType!=2&&count==1){//公司
                         ElementXMLUtils.returnValue(ElementConfig.COMPANY_COMPANY,result);
                  	     return result;
                    }
                    if(typeList!=2){//判断是不是公司
                    	acount++;
                    }
                  } 
                  int ascount=assum-acount;
                  if(ascount==0&&sums==1&&count==1){
                  	int cum= organizationService.TreeByIdForSonOryType(id);
                  	if(cum==0){
                  		ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY,result);
             	        return result;
                  	}
                		
                  }
                if(orgType==4){
                	int sum=0;
                	 for(int i=0;i<lists.size();i++){
               		    int num=lists.get(i).getOrgType();
               		    if(num==2){//判断父级是公司的数量
               			 sum++;
               		    }
                 	 }
                	 if(sum>0){
                		 ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATELEVEL,result);
               	         return result;
                	 }
                }
                if(orgType==2){
                	 int sum=0;
                	 for(int i=0;i<lists.size();i++){
              		    int num=lists.get(i).getOrgType();
              		    if(num==4){//判断父级是板块的数量
              			 sum++;
              		    }
                	 }
                	 if(sum==0){
              	    	ElementXMLUtils.returnValue(ElementConfig.DEPER_PLATE,result);
              	        return result;
              	     }
                }
                if(orgType==3){//判断该组织是否是部门级别
                	  int sum=0;
                	  for(int i=0;i<lists.size();i++){
                 		    int num=lists.get(i).getOrgType();
                 		    if(num==2){
                 			 sum++;
                 		    }
                          }
                	  if(sum==0){
                 	    	ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY,result);
                 	        return result;
                 	     }
                 	     
                       if(lists.get(0).getOrgType()==3){
                      	 ElementXMLUtils.returnValue(ElementConfig.DEPER_COMPANY,result);
                  	     return result;
                       }
           	  }
            Map<String, String> listBefore=organizationService.TreeByIdForSonAfter(id);  
            Integer i = organizationService.moveOrganization(user, id, parentOrgId);
            if (Integer.valueOf(1).equals(i)) {
            	      Map<String, String> listAfter=organizationService.TreeByIdForSonAfter(parentOrgId);
            	      organizationService.TreeByIdForSonShow(listBefore,listAfter);
            	      for(Organization ll:lists){
            	    	  if(ll.getOrgType()==2){
            	    		  ElementXMLUtils.returnValue(ElementConfig.BUDGET_GENERATE,result);
                	    	  return result;
            	    	  }
            	      }
            	    	  ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                          return result;
            	     
            	        
            } else {
            	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
            }
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据条件判断是否该节点存在子节点
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"organization:view" },logical=Logical.OR)
    @ApiOperation(value = "根据条件判断是否该节点存在子节点",notes = "根据条件判断是否该节点存在子节点",response=OrganizaHason.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgName", value = "组织架构名", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "id", value = "id", required = true),
    	})
    @PostMapping(value = "/hasSon")
    public OrganizaHason hasOrganizationSon(String id,String orgName,HttpServletRequest request, HttpServletResponse response) {
        OrganizaHason hason=new OrganizaHason();
        try {
            Map<Object, Object> map = new HashMap<>();
           
            if (null !=orgName && !"".equals(orgName)) {
            	orgName=new String(orgName.getBytes("iso8859-1"),"utf-8");
                map.put("orgName",orgName.trim().toString());
            }
            if (null != id && !"".equals(id)) {
                map.put("id", id);
            }
            Boolean flag = organizationService.hasOrganizationSon(map);
            hason.setData(flag);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,hason);
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,hason);
            this.logger.error(e.getMessage(), e);
        }
        return hason;
    }
}
