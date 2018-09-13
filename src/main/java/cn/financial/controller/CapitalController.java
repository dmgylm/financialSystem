package cn.financial.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.financial.model.BusinessData;
import cn.financial.model.Capital;
import cn.financial.model.CapitalNatrue;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.response.CapitalByIdResult;
import cn.financial.model.response.CapitalNatrueResult;
import cn.financial.model.response.CapitalResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.CapitalNatrueService;
import cn.financial.service.CapitalService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.CapitalServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.ExcelUtil;
import cn.financial.util.SiteConst;
import cn.financial.util.TimeUtils;
import cn.financial.util.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 资金表Controller
 * @author lmn
 *
 */

@Api(tags = "资金流水数据")
@Controller
@RequestMapping("/capital")
public class CapitalController {
 
    
        @Autowired
        private  CapitalService capitalService; //资金流水表
        
        @Autowired
        private  BusinessDataServiceImpl businessDataServiceImpl; 
        
        @Autowired
        private  CapitalServiceImpl capitalServiceImpl; //资金流水表
        
        @Autowired
        private  CapitalNatrueService capitalNatrueService; 
        
        @Autowired
        private  UserOrganizationService userOrganizationService; //用户组织的表
        
        @Autowired
        private  OrganizationService organizationService; //组织架构表

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        @ApiOperation(value="跳转到导入页面", notes="")
        @RequestMapping(value="/excel", method = RequestMethod.GET)
        public String getexcel(HttpServletRequest request, HttpServletResponse response) {
            return "cel";
        }


        /**
         * 根据条件查资金数据 (不传数据就是查询所有的)
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/listBy", method = RequestMethod.POST)
        @ApiOperation(value="查询资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的)",response = CapitalResult.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "page", value = "查询数据的开始页码（第一页开始）page=1", required = false, dataType = "integer", paramType = "query"),
                @ApiImplicitParam(name = "pageSize", value = "每页显示数据的条数（如每页显示10条数据）", required = false, dataType = "integer", paramType = "query"),
                @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "keyword", value = "关键字搜索", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeBeg", value = "开始交易日期（格式：2018-01-02 ）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeEnd", value = "结束交易日期（格式：2018-01-02 ）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String", paramType = "query")})
         @ResponseBody
        public CapitalResult listCapitalBy(HttpServletRequest request,String keyword,String accountBank,String accountNature,
                String tradeTimeBeg,String tradeTimeEnd,String classify,Integer page,Integer pageSize) {
            CapitalResult capitalResult=new CapitalResult();
            try {
                 if(keyword!=null&&!keyword.equals("")){
                  keyword= new String(keyword.getBytes("iso8859-1"),"utf-8");
                }
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据
                if(userOrganization==null){
                 List<Capital> list=new ArrayList<>();
                 ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,capitalResult);
                 capitalResult.setData(list);
                 capitalResult.setTotal(list.size());      
                }else{
                List<String> code=new ArrayList<>();
                for (int i = 0; i < userOrganization.size(); i++) {
                    Integer type=Integer.parseInt(userOrganization.get(i).getString("orgType").toString()); 
                    String name=userOrganization.get(i).getString("name").toString();
                    String str=userOrganization.get(i).getString("his_permission");
                    if(type==Capital.DEPNUM||name.contains(Capital.NAME)){//如果是部门级别 则 获取公司的oid
                     Organization  organization=organizationService.getCompanyNameBySon(userOrganization.get(i).getString("pid"));
                     if(organization!=null){
                      String his_permission= organization.getHis_permission();//如果是部门级别的则是得到公司的  his_permission
                      if(his_permission.contains(",")){
                          String[] hispermission=his_permission.split(","); 
                          for (int j = 0; j < hispermission.length; j++) {
                            code.add(hispermission[j]); 
                          }  
                      }else{
                          code.add(his_permission);
                      }
                     }else{//如果不是部门级别则是直接取组织架构的his_permission
                         if(str.contains(",")){
                             String[] his_permission=str.split(","); 
                             for (int j = 0; j < his_permission.length; j++) {
                               code.add(his_permission[j]); 
                             }  
                         }else{
                             code.add(str);
                         } 
                     }
                    }else{
                        if(str.contains(",")){
                            String[] his_permission=str.split(","); 
                            for (int j = 0; j < his_permission.length; j++) {
                              code.add(his_permission[j]); 
                            }  
                        }else{
                            code.add(str);
                        }  
                    }
                }
                if(keyword!=null&&!keyword.equals("")){
                    Map<Object, Object> mapKeyword = new HashMap<>();
                    mapKeyword.put("orgName", keyword);
                    List<Organization>  listAllOrganizationBy=organizationService.listAllOrganizationBy(map);
                    for (int i = 0; i < listAllOrganizationBy.size(); i++) {
                        
                    }
                }
                map.put("accountBank",accountBank);//开户行
                map.put("accountNature",accountNature);//账户性质
                map.put("tradeTimeBeg",tradeTimeBeg);//交易起始日期
                map.put("tradeTimeEnd",tradeTimeEnd);//交易结束日期
                map.put("classify",classify);//项目分类
                map.put("code", code);//根据权限的typeId查询相对应的数据
                map.put("company",keyword);//关键字获取的公司名称查询
                List<Capital> total = capitalService.capitalExport(map); //根据权限oId查询里面的权限的全部数据未经过分页
                if(pageSize==null ||pageSize==0){
                    map.put("pageSize",10);
                }else{
                    map.put("pageSize",pageSize);
                }
                if(page==null){
                    map.put("start",0);
                }else{
                    map.put("start",pageSize * (page- 1));
                }
                List<Capital> list = capitalService.listCapitalBy(map); //根据权限oId查询里面的权限数据(分页数据)
                Date  newTime=new Date();
                for (int i = 0; i < list.size(); i++) {
                    Date begTime=list.get(i).getCreateTime(); //得到开始时间
                    Date endTime=list.get(i).getUpdateTime(); //得到更新时间
                    Integer num=TimeUtils.daysBetween(begTime, newTime); //比较开始到现在的时间差
                    Integer editor=list.get(i).getEditor();
                    if(num<=7 && endTime==null){
                        editor=1;//可编辑数据
                    }
                    list.get(i).setEditor(editor);
               }
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,capitalResult);
                capitalResult.setData(list);
                capitalResult.setTotal(total.size());
              }
             } catch (Exception e) {
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,capitalResult);
                this.logger.error(e.getMessage(), e);
            }
            return capitalResult;
        }
        
        /**
         * 根据id查询资金数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @ApiOperation(value="根据id查询资金数据", notes="根据url的id来获取资金流水的信息",response = CapitalByIdResult.class)
        @ApiImplicitParams({@ApiImplicitParam(name="id",value="资金的id",dataType="string", paramType = "query", required = true)})
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        @ResponseBody
        public CapitalByIdResult selectCapitalById(HttpServletRequest request,String id) {
            CapitalByIdResult capitalByIdResult=new CapitalByIdResult();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
                boolean isImport=true;//是否可编辑
                for (int i = 0; i < userOrganization.size(); i++) {
                    Integer orgType=Integer.parseInt(userOrganization.get(i).getString("orgType"));
                    String oid=userOrganization.get(i).getString("pid");
                    isImport=businessDataServiceImpl.isImport(orgType, oid);
                    if(isImport==false){
                        break;
                    }
                }
                Capital  capital=capitalService.selectCapitalById(id);//查询id的数据
                Map<Object, Object> map1=new HashMap<>();
                map1.put("id",capital.getoId());
                List<Organization>  listOrganization=organizationService.listAllOrganizationBy(map1);
                Integer isStatus=listOrganization.get(0).getStatus();//判断组织架构是否被停用  0表示停用已经被删除
                if(isImport){
                  if(isStatus==0){
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,capitalByIdResult);
                    capitalByIdResult.setResultDesc("当前组织架构数据已被停用！不能进行编辑！");    
                  }else{
                    if(id!=null&&!id.equals("")){
                        Date  newTime=new Date();
                        Date begTime=capital.getCreateTime(); //得到开始时间
                        Date endTime=capital.getUpdateTime(); //得到更新时间
                        Integer num=TimeUtils.daysBetween(begTime, newTime); //比较开始到现在的时间差
                        Integer editor=capital.getEditor();
                        if(num<=7||endTime==null){
                            editor=1;//可编辑数据
                        }
                        capital.setEditor(editor);
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,capitalByIdResult);
                        capitalByIdResult.setData(capital);                  
                    }else{
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,capitalByIdResult);
                        capitalByIdResult.setResultDesc("id不能为空！");
                    } 
                  }
                }else{
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,capitalByIdResult);
                    capitalByIdResult.setResultDesc("您当前没有权限进行此操作！");  
                }
            } catch (Exception e) {
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,capitalByIdResult);
                this.logger.error(e.getMessage(), e);
            }
            return capitalByIdResult;
        }
        
     
        
        /**
         * 修改资金数据
         * @param request
         * @return
         */
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        @ApiOperation(value="修改资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的)",response=ResultUtils.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "资金流水表id", required = true, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String",paramType = "query")})
        @ResponseBody
        public ResultUtils updateCapital(HttpServletRequest request,String id,String classify,String remarks) {
            ResultUtils result=new ResultUtils();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
                boolean isImport=true;//是否可编辑
                for (int i = 0; i < userOrganization.size(); i++) {
                    Integer orgType=Integer.parseInt(userOrganization.get(i).getString("orgType"));
                    String oid=userOrganization.get(i).getString("pid");
                    isImport=businessDataServiceImpl.isImport(orgType, oid);
                    if(isImport==false){
                        break;
                    }
                }
                Capital  selectCapitalById=capitalService.selectCapitalById(id);//查询id的数据
                Map<Object, Object> map1=new HashMap<>();
                map1.put("id",selectCapitalById.getoId());
                List<Organization>  listOrganization=organizationService.listAllOrganizationBy(map1);
                Integer isStatus=listOrganization.get(0).getStatus();//判断组织架构是否被停用  0表示停用已经被删除
                if(isImport){
                  if(isStatus==0){
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                    result.setResultDesc("当前组织架构数据已被停用！不能进行此操作！");    
                  }else{
                    if(id!=null&&!id.equals("")){
                        Capital capital =new Capital();
                        capital.setId(id);
                        capital.setClassify(classify); //修改项目分类
                        capital.setRemarks(remarks);  //备注
                        Integer i = capitalService.updateCapital(capital);
                        if (i == 1) {
                           ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                        } else {
                           ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                        }               
                    }else{
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                        result.setResultDesc("id不能为空！");
                    } 
                  }
                }else{
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                    result.setResultDesc("您当前没有权限进行此操作！");
                }
            } catch (Exception e) {
                  ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
                this.logger.error(e.getMessage(), e);
            }
            return result;
        }
        
      
        /***
         * 导入上传
         */
        @Transactional(rollbackFor = Exception.class)
        @RequiresPermissions("capital:import")
        @RequestMapping(value="/excelImport",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,method = RequestMethod.POST)
        @ApiOperation(value="资金流水上传", notes="资金流水表上传数据",response=ResultUtils.class)
        @ResponseBody
        public ResultUtils excelImport(@RequestPart(value="uploadFile") @ApiParam(name="uploadFile",value="文件流对象,接收数组格式",required=true) MultipartFile uploadFile,HttpServletRequest request) throws IOException{
            ResultUtils result=new ResultUtils();
            User user = (User) request.getAttribute("user");
            String uId = user.getId();
            Map<Object, Object> map = new HashMap<>();
            //判断 权限的数据 公司及其公司以下的级别才可以上传数据
            List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
            List<String> code=new ArrayList<>();
            for (int i = 0; i < userOrganization.size(); i++) {
                Integer type=Integer.parseInt(userOrganization.get(i).getString("orgType").toString()); 
                String name=userOrganization.get(i).getString("name").toString();
                String str=userOrganization.get(i).getString("his_permission");
                if(type==Capital.DEPNUM||name.contains(Capital.NAME)){//如果是部门级别 则 获取公司的oid
                 Organization  organization=organizationService.getCompanyNameBySon(userOrganization.get(i).getString("pid"));
                 if(organization!=null){
                  String his_permission= organization.getHis_permission();//如果是部门级别的则是得到公司的  his_permission
                  if(his_permission.contains(",")){
                      String[] hispermission=his_permission.split(","); 
                      for (int j = 0; j < hispermission.length; j++) {
                        code.add(hispermission[j]); 
                      }  
                  }else{
                      code.add(his_permission);
                  }
                 }else{//如果不是部门级别则是直接取组织架构的his_permission
                     if(str.contains(",")){
                         String[] his_permission=str.split(","); 
                         for (int j = 0; j < his_permission.length; j++) {
                           code.add(his_permission[j]); 
                         }  
                     }else{
                         code.add(str);
                     } 
                 }
                }else{
                    if(str.contains(",")){
                        String[] his_permission=str.split(","); 
                        for (int j = 0; j < his_permission.length; j++) {
                          code.add(his_permission[j]); 
                        }  
                    }else{
                        code.add(str);
                    }  
                }
                         
            }
            map.put("code", code);//根据权限的typeId查询相对应的数据
            List<Capital> totalCapital = capitalService.capitalCompany(map); //根据权限oId查询里面的权限的全部数据未经过分页  
            String[] company=new String[totalCapital.size()]; //获取所有的权限公司名字
            for (int i = 0; i < totalCapital.size(); i++) {
                if(totalCapital.size()>0){
                 company[i]=totalCapital.get(i).getCompany();   
                }
            }
            List<String> companyList = new ArrayList<String>();
            companyList=Arrays.asList(company); 
            boolean isImport=true;//是否可编辑
            for (int i = 0; i < userOrganization.size(); i++) {
                Integer orgType=Integer.parseInt(userOrganization.get(i).getString("orgType"));
                String oid=userOrganization.get(i).getString("pid");
                isImport=businessDataServiceImpl.isImport(orgType, oid);
                if(isImport==false){
                    break;
                }
            }
            boolean insertFlag = true;//上传数据是否有错
            if(isImport){
            if(uploadFile.getOriginalFilename().contains(".")){
            String name=uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().indexOf("."));
            //得到账户性质项目分类数据 如果上传的不包含在里面 则是不能上传
            List<CapitalNatrue> capitalNatrueList= capitalNatrueService.listCapitalNatrue(); 
            String[] arr=new String[capitalNatrueList.size()];
            for (int i = 0; i < capitalNatrueList.size(); i++) {
                arr[i]=capitalNatrueList.get(i).getName();
            }
            List<String> fauCodeList = new ArrayList<String>();
            fauCodeList=Arrays.asList(arr);
            if(name.equals(".xls")||name.equals(".xlsx")){
            Long size=uploadFile.getSize();
            if(uploadFile.getSize()>0 && size<5242880){  //判断文件大小是否是5M以下的
              try {
                  int row=1;
                  Integer a=0;
                  List<String []> list=ExcelUtil.read(uploadFile.getInputStream(), row);//读取excel表格数据
                  List<Capital> listCapital=new ArrayList<>();
                  try {
                         for (int i = 0; i < list.size(); i++){
                             String[] str=list.get(i);   //在excel得到第i条数据
                             Capital capital=new Capital();
                             capital.setId(UuidUtil.getUUID());
                             if(!str[0].equals("")){
                                String orgName=str[0];
                                if(!companyList.contains(orgName)){
                                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                    result.setResultDesc("Excel表格第"+(i+2)+"行第一个的单元格公司不在您的权限数据之内不能上传！");
                                    insertFlag=false;
                                    break;    
                                }else{
                                    Map<Object, Object> map1=new HashMap<>();
                                    map1.put("orgName",orgName);
                                    List<Organization>  listOrganization=organizationService.listAllOrganizationBy(map1);
                                    Integer isStatus=0;//判断组织架构是否被停用  0表示停用已经被删除
                                    if(listOrganization.size()>0){
                                    for (int j = 0; j < listOrganization.size(); j++) {
                                        if(listOrganization.get(j).getStatus()==1){
                                           isStatus=listOrganization.get(j).getStatus();
                                        }
                                    }
                                    if(isStatus!=1){
                                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                        result.setResultDesc("Excel表格第"+(i+2)+"行第一个的单元格公司数据已经被停用");
                                        insertFlag=false;
                                        break; 
                                     }else{
                                       capital.setoId(listOrganization.get(0).getId()); //公司名字所对应的组织架构id   
                                     }
                                    }else{
                                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                        result.setResultDesc("Excel表格第"+(i+2)+"行第一个的单元格公司不存在");
                                        insertFlag=false;
                                        break;  
                                    }                     
                                } 
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第一个单元格公司名字不能为空");
                                 insertFlag=false;
                                 break;  
                             }
                             if(!str[0].equals("")){
                                 capital.setCompany(str[0]);
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第一个单元格公司名字不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[1].equals("")){
                                 capital.setAccountName(str[1]);
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第二个单元格户名不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[2].equals("")){
                                 capital.setAccountBank(str[2]);
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第三个单元格开户行不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[3].equals("")){
                                 capital.setAccount(str[3]);
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第四个单元格账户不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[4].equals("")){
                                if(fauCodeList.contains(str[4])){
                                    capital.setAccountNature(str[4]);
                                }else{
                                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                    result.setResultDesc("Excel表格第"+(i+2)+"行第五个单元格此账户性质不存在,请重新修改");
                                    insertFlag=false;
                                    break;  
                                }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第五个单元格账户性质不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[5].equals("")){
                                 try {
                                     capital.setTradeTime(sdf.parse(str[5]));  
                                 } catch (Exception e) {
                                     ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                     result.setResultDesc("上传的交易日期格式不对，正确的格式：2018-01-01 00:00:00");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第六个单元格交易时间不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[6].equals("")){
                                 if(str[6].matches("([1-9]+[0-9]*|0)(\\.[\\d]+)?")){//判断单元格数据是否是数字(包含小数点)
                                     capital.setStartBlack(Double.parseDouble(str[6]));  
                                 }else{
                                     ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第七个单元格只能是数字");
                                     insertFlag=false;
                                     break; 
                                 }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第七个单元格期初余额不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[7].equals("")){
                                 if(str[7].matches("([1-9]+[0-9]*|0)(\\.[\\d]+)?")){//判断单元格数据是否是数字
                                     capital.setIncom(Double.parseDouble(str[7]));
                                 }else{
                                     ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第八个单元格数据只能是数字");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第八个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[8].equals("")){
                                 if(str[8].matches("([1-9]+[0-9]*|0)(\\.[\\d]+)?")){//判断单元格数据是否是数字
                                     capital.setPay(Double.parseDouble(str[8]));
                                 }else{
                                     ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第九个单元格数据只能是数字");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第九个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[9].equals("")){
                                 if(str[9].matches("([1-9]+[0-9]*|0)(\\.[\\d]+)?")){//判断单元格数据是否是数字
                                     capital.setEndBlack(Double.parseDouble(str[9]));
                                 }else{
                                     ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十个单元格数据只能是数字");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[10].equals("")){
                                 if(str[10].length()<=200){
                                     capital.setAbstrac(str[10]); 
                                 }else{
                                     ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十一个单元格里面字数最多200字");
                                     insertFlag=false;
                                     break;   
                                 }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十一个单元格的数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[11].equals("")){
                                if(fauCodeList.contains(str[11])){
                                    capital.setClassify(str[11]); 
                                }else{
                                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                    result.setResultDesc("Excel表格第"+(i+2)+"行第十二个单元格不包含此项目分类");
                                    insertFlag=false;
                                    break;  
                                }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十二个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[12].equals("")){
                                 if(str[12].length()<=200){
                                     capital.setRemarks(str[12]); 
                                  }else{
                                      ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                      result.setResultDesc("Excel表格第"+(i+2)+"行第十三个单元格里面最多200字");
                                      insertFlag=false;
                                      break;  
                                  }
                             }else{
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十三个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             Calendar calendar = Calendar.getInstance();
                             calendar.setTime(calendar.getTime());
                             capital.setuId(uId);
                             capital.setYear(calendar.get(Calendar.YEAR));
                             capital.setMonth(calendar.get(Calendar.MONTH)+1);
                             capital.setStatus(1);
                             capital.setEditor(0);
                             listCapital.add(capital);
                         }
                         if(insertFlag) { //如果没有数据异常才导入数据
                             a = capitalService.batchInsertCapital(listCapital); //导入新增的数据
                             if (a == 1) {
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                             } else {
                                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                             } 
                         }
                 } catch (Exception e) {
                     ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
                     this.logger.error(e.getMessage(), e);
                 }
                 
            } catch (Exception e) {
                 ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
                this.logger.error(e.getMessage(), e);
            }
        }else{
            ElementXMLUtils.returnValue(ElementConfig.CAPITAL_FILE_EXCEED_5M,result);
            result.setResultDesc("文件大于5M不能上传！请传5M以下的数据");
        } 
      }else{
          ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
          result.setResultDesc("您上传的不是Excel文档！");
      }
      }else{
           ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
           result.setResultDesc("您上传的不是Excel文档！");
       }
      }else{
         ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
         result.setResultDesc("您当前没有权限进行此操作！");
      }
          return result;
      }
        
        
        /***
         * 导出
         * @param response
         * @throws Exception 
         */
        @RequiresPermissions("capital:download")
        @RequestMapping(value="/export",method = RequestMethod.GET)
        @ApiOperation(value="导出资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的) 并且导出",response = ResultUtils.class)
        @ApiImplicitParams({
            @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字搜索", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tradeTimeBeg", value = "开始交易日期（格式：2018-01-02 ）", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tradeTimeEnd", value = "结束交易日期（格式：2018-01-02 ）", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String", paramType = "query")})
        @ResponseBody
        public ResultUtils export(HttpServletRequest request,HttpServletResponse response,String keyword,String accountBank,String accountNature,
                String tradeTimeBeg,String tradeTimeEnd,String classify) throws Exception{
            OutputStream os = null;
            //Map<String, Object> dataMap = new HashMap<String, Object>();
            ResultUtils result=new ResultUtils();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据
                List<String> code=new ArrayList<>();
                for (int i = 0; i < userOrganization.size(); i++) {
                    Integer type=Integer.parseInt(userOrganization.get(i).getString("orgType").toString()); 
                    String name=userOrganization.get(i).getString("name").toString();
                    String str=userOrganization.get(i).getString("his_permission");
                    if(type==Capital.DEPNUM||name.contains(Capital.NAME)){//如果是部门级别 则 获取公司的oid
                     Organization  organization=organizationService.getCompanyNameBySon(userOrganization.get(i).getString("pid"));
                     if(organization!=null){
                      String his_permission= organization.getHis_permission();//如果是部门级别的则是得到公司的  his_permission
                      if(his_permission.contains(",")){
                          String[] hispermission=his_permission.split(","); 
                          for (int j = 0; j < hispermission.length; j++) {
                            code.add(hispermission[j]); 
                          }  
                      }else{
                          code.add(his_permission);
                      }
                     }else{//如果不是部门级别则是直接取组织架构的his_permission
                         if(str.contains(",")){
                             String[] his_permission=str.split(","); 
                             for (int j = 0; j < his_permission.length; j++) {
                               code.add(his_permission[j]); 
                             }  
                         }else{
                             code.add(str);
                         } 
                     }
                    }else{
                        if(str.contains(",")){
                            String[] his_permission=str.split(","); 
                            for (int j = 0; j < his_permission.length; j++) {
                              code.add(his_permission[j]); 
                            }  
                        }else{
                            code.add(str);
                        }  
                    }    
                }
                if(keyword!=null&&!keyword.equals("")){
                    keyword= new String(keyword.getBytes("iso8859-1"),"utf-8");
                }
                if(accountBank!=null&&!accountBank.equals("")){
                    accountBank= new String(accountBank.getBytes("iso8859-1"),"utf-8");
                }
                if(accountNature!=null&&!accountNature.equals("")){
                    accountNature= new String(accountNature.getBytes("iso8859-1"),"utf-8");
                }
                if(classify!=null&&!classify.equals("")){
                    classify= new String(classify.getBytes("iso8859-1"),"utf-8");
                }
                map.put("accountBank",accountBank);//开户行
                map.put("accountNature",accountNature);//账户性质
                map.put("tradeTimeBeg",tradeTimeBeg);//交易起始日期
                map.put("tradeTimeEnd",tradeTimeEnd);//交易结束日期
                map.put("classify",classify);//项目分类
                map.put("code", code);//根据权限的typeId查询相对应的数据
                map.put("company",keyword);//关键字获取的公司名称查询
                List<Capital> listData = capitalService.capitalExport(map); //根据权限oId查询里面的权限数据
                List<String[]> strList=new ArrayList<>();
                  String[] ss={"公司名称","户名","开户行","账户","账户性质",
                          "交易日期","期初余额","本期收入","本期支出","期末余额","摘要","项目分类","备注"};
                  strList.add(ss);
                  for (int i = 0; i < listData.size(); i++) {
                      String[] str=new String[13];
                      Capital capital=listData.get(i);
                      if(capital.getCompany()!=null &&!capital.getCompany().equals("")){
                          str[0]=capital.getCompany();
                       }
                      if(capital.getAccountName()!=null && !capital.getAccountName().equals("")){
                         str[1]=capital.getAccountName();
                      }
                      if(capital.getAccountBank()!=null&&!capital.getAccountBank().equals("")){
                         str[2]=capital.getAccountBank();
                      }
                      if(capital.getAccount()!=null&&!capital.getAccount().equals("")){
                         str[3]=capital.getAccount();
                      }
                      if(capital.getAccountNature()!=null &&!capital.getAccountNature().equals("")){
                         str[4]=capital.getAccountNature();
                      }
                      if(capital.getTradeTime()!=null&&!capital.getTradeTime().equals("")){
                         str[5]=sdf.format(capital.getTradeTime());
                      }
                      if(capital.getStartBlack()!=null &&!capital.getStartBlack().equals("")){
                         str[6]=capital.getStartBlack().toString();
                      }
                      if(capital.getIncom()!=null &&!capital.getIncom().equals("")){
                         str[7]=capital.getIncom().toString();
                      }
                      if(capital.getPay()!=null&&!capital.getPay().equals("")){
                         str[8]=capital.getPay().toString();
                      }
                      if(capital.getEndBlack()!=null&&!capital.getEndBlack().equals("")){
                         str[9]=capital.getEndBlack().toString();
                      }
                      if(capital.getAbstrac()!=null&&!capital.getAbstrac().equals("")){
                          str[10]=capital.getAbstrac();
                      }
                      if(capital.getClassify()!=null&&!capital.getClassify().equals("")){
                          str[11]=capital.getClassify();
                      }
                      if(capital.getRemarks()!=null&&!capital.getRemarks().equals("")){
                          str[12]=capital.getRemarks();
                      } 
                          strList.add(str);
                  }
                  response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("资金流水表", "UTF-8")+".xls");
                  response.setContentType("application/octet-stream");
                  os = response.getOutputStream();
                  ExcelUtil.export(strList, os);
                  ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);    
             } catch (IOException e) {
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                e.printStackTrace();
             } finally {
                if(os != null)
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return result;
        }
        
        
        /***
         * 导出
         * @param response
         * @throws Exception 
         */
        @RequiresPermissions("capital:download")
        @RequestMapping(value="/exportModule",method = RequestMethod.GET)
        @ApiOperation(value="下载资金流水模板", notes="下载资金流水模板",response = ResultUtils.class)
        @ApiImplicitParams({
           })
        @ResponseBody
        public ResultUtils exportModule(HttpServletRequest request,HttpServletResponse response) throws Exception{
           ResultUtils result =new ResultUtils();
           String fileURL= SiteConst.CAPITALEXPORT+"资金流水模板.xls";
           File file=new File(fileURL);
           Boolean sucess=capitalServiceImpl.export(request, response,file);
           if(sucess){
               ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result); 
               result.setResultDesc("模板下载成功");
           }else{
               ElementXMLUtils.returnValue(ElementConfig.GETFILE_FAIL,result); 
               result.setResultDesc("模板下载失败，模板文件不存在！");
           } 
           return result;
        }
        
        
        
    
        /** 根据条件查询账户性质项目分类数据
        * 
        * @param request
        * @param id
        *           
        * @return
        */
       @ApiOperation(value="根据条件查询账户性质项目分类数据", notes="查询账户性质项目分类数据",response = CapitalNatrueResult.class)
       @RequiresPermissions("capital:view")
       @RequestMapping(value="/listCapitalNatrue", method = RequestMethod.POST)
       @ResponseBody
       public CapitalNatrueResult listCapitalNatrue(HttpServletRequest request) {
           CapitalNatrueResult result=new CapitalNatrueResult();
           try {
              List<CapitalNatrue> list= capitalNatrueService.listCapitalNatrue();
              List<CapitalNatrue> natrueList=new  ArrayList<CapitalNatrue>();//账户性质的数据
              List<CapitalNatrue> classifyList=new  ArrayList<CapitalNatrue>();//项目分类的数据
              for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getcId()==1){
                    natrueList.add(list.get(i));
                }else{
                    classifyList.add(list.get(i));
                }
             }
              String[] natrueName=new String[natrueList.size()];//获取账户性质的name
              for (int i = 0; i < natrueList.size(); i++) {
                  natrueName[i]=natrueList.get(i).getName();
              }
              String[] classifyName=new String[classifyList.size()];//获取项目分类的name
              for (int i = 0; i < classifyList.size(); i++) {
                  classifyName[i]=classifyList.get(i).getName();
              }
              Map<String, Object> map = new HashMap<String, Object>();
              map.put("natrueName", natrueName);
              map.put("classifyName", classifyName);
              result.setData(map);
              result.setTotal(list.size());
              ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
              result.setResultDesc("成功！");
           } catch (Exception e) {
               ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
               this.logger.error(e.getMessage(), e);
           }
           return result;
       }
       
}
