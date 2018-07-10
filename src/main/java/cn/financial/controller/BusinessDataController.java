package cn.financial.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.financial.model.Business;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.Capital;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.ExcelUtil;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * 损益表Controller
 * @author lmn
 *
 */
@Api(tags = "损益/预算数据")
@Controller
@RequestMapping("/businessData")
public class BusinessDataController {
 
    
        @Autowired
        private  BusinessDataService businessDataService;
        
        @Autowired
        private  UserOrganizationService userOrganizationService;  //角色的权限
        
        @Autowired
        private  OrganizationService organizationService;  //组织架构
        
        @Autowired
        private  DataModuleService dmService;  
        
        @Autowired
        private  BusinessDataInfoService businessDataInfoService;  
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        /**
         * 根据条件查损益数据(提交不传就是查询全部)
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("businessData:view")
        @RequestMapping(value="/listBy", method = RequestMethod.POST)
        @ApiOperation(value="查询损益/预算数据", notes="根据条件查数据 (不传数据就是查询所有的)",response = BusinessData.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "page", value = "查询数据的开始页码（第一页开始）page=1", required = true, dataType = "integer"),
                @ApiImplicitParam(name = "pageSize", value = "每页显示数据的条数（如每页显示10条数据）", required = true, dataType = "integer"),
                @ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String"),
                @ApiImplicitParam(name = "month", value = "月份", required = false, dataType = "String"),
                @ApiImplicitParam(name = "sId", value = "判断是损益还是预算表  1损益  2 预算", required = true, dataType = "String"),
                })
        @ResponseBody
        public Map<String, Object> listBusinessDataBy(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                    map.put("year",request.getParameter("year")); //年份
                }
                if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                    map.put("month",request.getParameter("month"));  //月份
                }
                if(request.getParameter("sId")!=null && !request.getParameter("sId").equals("")){
                    map.put("sId",request.getParameter("sId"));  //判断是损益还是预算表  1损益  2 预算
                }
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据
                List<JSONObject> listOrganization=new ArrayList<>();   //筛选过后就的权限数据
                for (int i = 0; i < userOrganization.size(); i++) {
                    JSONObject pidJosn=userOrganization.get(i);
                    String orgType=pidJosn.getString("orgType");
                    if(orgType.equals(BusinessData.NUM)&&!pidJosn.getString("name").contains(BusinessData.NAME)){ //公司以下的节点的数据
                        listOrganization.add(userOrganization.get(i));
                    }else if(pidJosn.getString("name").contains(BusinessData.NAME)){ //查出汇总一下的子节点
                      List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(pidJosn.getString("pid")); //根据id查出子节点集合
                      JSONArray jsonArr=(JSONArray) JSONArray.toJSON(listTreeByIdForSon);
                      for (int j = 0; j < jsonArr.size(); j++) {
                        JSONObject json=jsonArr.getJSONObject(j);
                        if(!json.getString("orgName").contains(BusinessData.NAME)){
                            listOrganization.add(jsonArr.getJSONObject(j)); 
                        }
                      }
                    }
                } 
                if(listOrganization.size()>0){
                    //List<BusinessData> list = businessDataService.listBusinessDataBy(map); //查询所有符合搜索条件的表数据
                    String[] typeId=new String[listOrganization.size()];//获取权限的typeId
                    //List<BusinessData> businessData=new ArrayList<>();  //所有符合权限的数据
                    for (int i = 0; i < listOrganization.size(); i++) { //循环权限全部数据    
                        JSONObject pidJosn=userOrganization.get(i);
                        String pid =pidJosn.getString("pid"); //找到权限数据里面的组织id
                        typeId[i]=pid;
                        //找权限的pid和损益表的typeId进行筛选
                         /* for (int j = 0; j < list.size(); j++) {
                            String typeId=list.get(j).getTypeId();//找损益表里面的typeId
                            if(pid.equals(typeId)){  //判断权限pid 和全部数据的typeId是否相同  
                               businessData.add(list.get(j));  // 可以显示的损益数据
                            }
                        }*/
                    }
                    List<String> typeIds = Arrays.asList(typeId);
                    map.put("typeId", typeIds);//根据权限的typeId查询相对应的数据
                    List<BusinessData> total = businessDataService.businessDataExport(map); //查询权限下的所有数据 未经分页
                    Integer pageSize=10;
                    if(request.getParameter("pageSize")!=null && !request.getParameter("pageSize").equals("")){
                        pageSize=Integer.parseInt(request.getParameter("pageSize"));
                        map.put("pageSize",pageSize);
                    }else{
                        map.put("pageSize",pageSize);
                    }
                    Integer start=0;
                    if(request.getParameter("page")!=null && !request.getParameter("page").equals("")){
                        start=pageSize * (Integer.parseInt(request.getParameter("page")) - 1);
                        map.put("start",start);
                    }else{
                        map.put("start",start);
                    }
                    List<BusinessData> businessData = businessDataService.listBusinessDataBy(map); //查询权限下分页数据
                    //根据oId查询部门信息
                    //循环合格数据的oid 去查询他的所有部门
                    List<Business> businessList=new ArrayList<>();//页面列表排列数据
                    for (int i = 0; i < businessData.size(); i++) {
                        List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(businessData.get(i).getTypeId()); //根据oId查出公司以下的部门
                        Organization CompanyName= organizationService.getCompanyNameBySon(businessData.get(i).getoId());//查询所属的公司名
                        for (int j = 0; j < listTreeByIdForSon.size(); j++) {
                            if(listTreeByIdForSon.get(j).getOrgType()==BusinessData.NUM){ //找到公司以下的节点业务
                                  Business business=new Business();
                                  business.setYear(businessData.get(i).getYear()); //年份
                                  business.setMonth(businessData.get(i).getMonth()); //月份
                                  business.setUserName(user.getName()); //用户
                                  business.setUpdateTime(businessData.get(i).getUpdateTime()); //操作时间
                                  business.setStatus(businessData.get(i).getStatus());//状态
                                  business.setCompany(CompanyName.getOrgName()); //公司
                                  business.setStructures(listTreeByIdForSon.get(j).getOrgName()); //业务方式
                                  businessList.add(business); 
                            }
                        }
                    }   
                   /* //数据分页
                        Integer p=(page - 1) * pageSize; //开始下标
                        Integer s=page* pageSize;  //结束下标
                        Integer totalPage = businessList.size() / pageSize; //总页数
                        if (businessList.size() % pageSize != 0){
                            totalPage++;
                        }
                        List<Business> subList =new ArrayList<>();
                        if(businessList.size()<pageSize){    //判断总得数据长度是否小于每页大小
                            subList=businessList.subList(0,businessList.size());
                        }else if(businessList.size()<s){     //判断总得数据长度是否小于结束下标大小
                            subList=businessList.subList(p,businessList.size());
                        }else{
                            subList=businessList.subList(p,s);
                        }*/
                        //Integer totalPage = businessList.size() / pageSize; //总页数
                        dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                        dataMap.put("data", businessList);
                        dataMap.put("total", total.size());
                }else{
                    throw new Exception("您没有权限操作损益表！");
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 根据id查询损益数据
         * 
         * @param request
         * @param id 业务表id
         * @param htmlType 1.HTML类型:配置模板 2HTML类型:录入模板
         *           
         * @return
         */
        @RequiresPermissions("businessData:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        @ApiOperation(value="根据id查询资金数据", notes="根据url的id来获取资金流水的信息")
        @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="表id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "htmlType", value = "1：HTML类型:配置模板  2：HTML类型:录入页面 3：HTML类型:查看页面 这里的htmlType是2", required = true, dataType = "integer")})
        @ResponseBody
        public Map<String, Object> selectBusinessDataById(HttpServletRequest request, @PathVariable String id,@PathVariable int htmlType) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                BusinessData  businessData=businessDataService.selectBusinessDataById(id);
                DataModule dm=dmService.getDataModule(businessData.getDataModuleId());
                BusinessDataInfo busInfo=businessDataInfoService.selectBusinessDataById(id);
                JSONObject joTemp=JSONObject.parseObject(dm.getModuleData());
                JSONObject joInfo=JSONObject.parseObject(busInfo.getInfo());
                JsonConvertProcess.mergeJson(joTemp, joInfo);
                HtmlGenerate htmlGenerate=new HtmlGenerate();
                //Integer htmlType=htmlType; //表示录入页面
                String html= htmlGenerate.generateHtml(JsonConvertProcess.mergeJson(joTemp, joInfo), htmlType);
                System.out.println(html);
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("data", html);
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 新增损益数据
         * @param request
         * @param response
         * @return
         */
       /* @RequiresPermissions("businessData:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
        public Map<String, Object> insertBusinessData(HttpServletRequest request,BusinessData businessData){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                businessData.setId(UuidUtil.getUUID());
                businessData.setuId(uId);
                businessData.setStatus(2);
                businessData.setDelStatus(1);
                Integer i = businessDataService.insertBusinessData(businessData);
                if (i == 1) {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                } else {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
               
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }*/
       /* 
        *//**
         * 修改损益数据
         * @param request
         * @return
         *//*
        @RequiresPermissions("businessData:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        public Map<String, Object> updateBusinessData(HttpServletRequest request,BusinessData businessData) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                businessData.setuId(uId);
                businessData.setDelStatus(1);
                Integer i = businessDataService.updateBusinessData(businessData);
                if (i == 1) {
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
        */
        /**
         * 删除损益数据 （修改Status为0）
         * @param request
         * @return
         *//*
        @RequiresPermissions("businessData:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                Integer i =businessDataService.deleteBusinessData(id);
                if (i == 1) {
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
        */
        
        /***
         * 导出下载
         * @param response
         * @throws Exception 
         */
        @RequiresPermissions("businessData:download")
        @RequestMapping(value="/export",method = RequestMethod.POST)
        @ApiOperation(value="导出损益/预算数据", notes="根据条件导出所有的数据",response = BusinessData.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String"),
                @ApiImplicitParam(name = "month", value = "月份", required = false, dataType = "String"),
                @ApiImplicitParam(name = "sId", value = "判断是损益还是预算表  1损益  2 预算", required = true, dataType = "String"),
                })
        @ResponseBody
        public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{
            OutputStream os = null;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                    map.put("year",request.getParameter("year")); //年份
                }
                if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                    map.put("month",request.getParameter("month"));  //月份
                }
                if(request.getParameter("sId")!=null && !request.getParameter("sId").equals("")){
                    map.put("sId",request.getParameter("sId"));  //月份
                }
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据
                List<JSONObject> listOrganization=new ArrayList<>();   //筛选过后就的权限数据
                for (int i = 0; i < userOrganization.size(); i++) {
                    JSONObject pidJosn=userOrganization.get(i);
                    String orgType=pidJosn.getString("orgType");
                    if(orgType.equals(BusinessData.NUM)&&!pidJosn.getString("name").contains(BusinessData.NAME)){ //公司以下的节点的数据
                        listOrganization.add(userOrganization.get(i));
                    }else if(pidJosn.getString("name").contains(BusinessData.NAME)){ //查出汇总一下的子节点
                      List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(pidJosn.getString("pid")); //根据id查出子节点集合
                      JSONArray jsonArr=(JSONArray) JSONArray.toJSON(listTreeByIdForSon);
                      for (int j = 0; j < jsonArr.size(); j++) {
                        JSONObject json=jsonArr.getJSONObject(j);
                        if(!json.getString("orgName").contains(BusinessData.NAME)){
                            listOrganization.add(jsonArr.getJSONObject(j)); 
                        }
                      }
                    }
                } 
                if(listOrganization.size()>0){
                    //List<BusinessData> list = businessDataService.listBusinessDataBy(map); //查询所有符合搜索条件的表数据
                    String[] typeId=new String[listOrganization.size()];//获取权限的typeId
                    //List<BusinessData> businessData=new ArrayList<>();  //所有符合权限的数据
                    for (int i = 0; i < listOrganization.size(); i++) { //循环权限全部数据    
                        JSONObject pidJosn=userOrganization.get(i);
                        String pid =pidJosn.getString("pid"); //找到权限数据里面的组织id
                        typeId[i]=pid;
                        //找权限的pid和损益表的typeId进行筛选
                         /* for (int j = 0; j < list.size(); j++) {
                            String typeId=list.get(j).getTypeId();//找损益表里面的typeId
                            if(pid.equals(typeId)){  //判断权限pid 和全部数据的typeId是否相同  
                               businessData.add(list.get(j));  // 可以显示的损益数据
                            }
                        }*/
                    }
                    List<String> typeIds = Arrays.asList(typeId);
                    map.put("typeId", typeIds);//根据权限的typeId查询相对应的数据
                    List<BusinessData> businessData = businessDataService.businessDataExport(map); //查询权限下的所有数据
                    //根据oId查询部门信息
                    //循环合格数据的oid 去查询他的所有部门
                    List<Business> businessList=new ArrayList<>();//页面列表排列数据
                    for (int i = 0; i < businessData.size(); i++) {
                        List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(businessData.get(i).getTypeId()); //根据oId查出公司以下的部门
                        Organization CompanyName= organizationService.getCompanyNameBySon(businessData.get(i).getoId());//查询所属的公司名
                        for (int j = 0; j < listTreeByIdForSon.size(); j++) {
                            if(listTreeByIdForSon.get(j).getOrgType()==BusinessData.NUM){ //找到公司以下的节点业务
                                  Business business=new Business();
                                  business.setYear(businessData.get(i).getYear()); //年份
                                  business.setMonth(businessData.get(i).getMonth()); //月份
                                  business.setUserName(user.getName()); //用户
                                  business.setUpdateTime(businessData.get(i).getUpdateTime()); //操作时间
                                  business.setStatus(businessData.get(i).getStatus());//状态
                                  business.setCompany(CompanyName.getOrgName()); //公司
                                  business.setStructures(listTreeByIdForSon.get(j).getOrgName()); //业务方式
                                  businessList.add(business); 
                            }
                        }
                    }   
                    List<String[]> strList=new ArrayList<>();
                    String[] ss={"年份","月份","公司名称","业务方式","创建用户","操作时间","状态"};
                    strList.add(ss);
                    for (int i = 0; i < businessList.size(); i++) {
                        String[] str=new String[7];
                        Business business=businessList.get(i);
                        if(business.getYear()!=null &&!business.getYear().equals("")){
                            str[0]=business.getYear().toString();
                         }
                        if(business.getMonth()!=null &&!business.getMonth().equals("")){
                            str[1]=business.getMonth().toString();
                         }
                        if(business.getCompany()!=null &&!business.getCompany().equals("")){
                            str[2]=business.getCompany();
                         }
                        if(business.getStructures()!=null &&!business.getStructures().equals("")){
                            str[3]=business.getStructures();
                         }
                        if(business.getUserName()!=null &&!business.getUserName().equals("")){
                            str[4]=business.getUserName();
                         }
                        if(business.getUpdateTime()!=null &&!business.getUpdateTime().equals("")){
                            str[5]=sdf.format(business.getUpdateTime());
                         }
                        if(business.getStatus()!=null &&! business.getStatus().equals("")){
                           str[6]=business.getStatus().toString();
                        }
                         strList.add(str);
                    }
                    response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("管理损益表", "UTF-8")+".xls");
                    response.setContentType("application/octet-stream");
                    os = response.getOutputStream();
                    ExcelUtil.export(strList, os);
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    dataMap.put("result","导出成功！");
                }else{
                    throw new Exception("您没有权限操作损益表！导出失败");
                }
            } catch (IOException e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                dataMap.put("result","导出失败！");
                e.printStackTrace();
            } finally {
                if(os != null)
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
}
