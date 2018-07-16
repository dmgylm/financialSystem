package cn.financial.controller;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.Capital;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.response.CapitalByIdResult;
import cn.financial.model.response.CapitalResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.CapitalService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.ExcelUtil;
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
                @ApiImplicitParam(name = "plate", value = "所属的板块", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "bu", value = "所属事业部门（如财务部）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "regionName", value = "所属大区的名称", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "province", value = "所属省份名称", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "company", value = "所属公司名称", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeBeg", value = "开始交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeEnd", value = "结束交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String", paramType = "query")})
        @ResponseBody
        public CapitalResult listCapitalBy(HttpServletRequest request,String plate,String bu,String regionName,String province,String company,
                String accountBank,String accountNature,String tradeTimeBeg,String tradeTimeEnd,String classify,Integer page,Integer pageSize) {
            //Map<String, Object> dataMap = new HashMap<String, Object>();
            CapitalResult capitalResult=new CapitalResult();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                 String uId = user.getId();
                 if(plate==null){ plate="";}
                 if(bu==null){ bu="";}
                 if(regionName==null){ regionName="";}
                 if(province==null){ province="";}
                 if(company==null){ company="";}
                 if(accountBank==null){ accountBank="";}
                 if(accountNature==null){ accountNature="";}
                 if(tradeTimeBeg==null){ tradeTimeBeg="";}
                 if(tradeTimeEnd==null){ tradeTimeEnd="";}
                 if(classify==null){ classify="";}
                 map.put("plate",new String(plate.getBytes("ISO-8859-1"),"UTF-8")); //板块
                 map.put("bu",new String(bu.getBytes("ISO-8859-1"),"UTF-8"));//事业部
                 map.put("regionName",new String(regionName.getBytes("ISO-8859-1"),"UTF-8"));//大区名称
                 map.put("province",new String(province.getBytes("ISO-8859-1"),"UTF-8"));//省份
                 map.put("company",new String(company.getBytes("ISO-8859-1"),"UTF-8"));//公司名称
                 map.put("accountBank",new String(accountBank.getBytes("ISO-8859-1"),"UTF-8"));//开户行
                 map.put("accountNature",new String(accountNature.getBytes("ISO-8859-1"),"UTF-8"));//账户性质
                 map.put("tradeTimeBeg",tradeTimeBeg);//交易起始日期
                 map.put("tradeTimeEnd",tradeTimeEnd);//交易结束日期
                 map.put("classify",new String(classify.getBytes("ISO-8859-1"),"UTF-8"));//项目分类
                 //判断 权限的数据 
                 List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
                 List<JSONObject> listOrganization=new ArrayList<>();   //筛选过后就的权限数据
                 List<JSONObject> listTree=new ArrayList<>();   //筛选过后就的权限数据
                 for (int i = 0; i < userOrganization.size(); i++) {
                     JSONObject obu = (JSONObject) userOrganization.get(i);
                     Integer num=Integer.parseInt(obu.get("orgType").toString());
                     String name=obu.getString("name").toString();
                     if(num<BusinessData.ORGNUM &&!name.contains(Capital.NAME)) {//大于公司级别并且不包含汇总 就查询以下的公司数据
                         //查询该节点下的所有子节点集合 获取公司的级别
                         List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(userOrganization.get(i).getString("pid"));
                         JSONArray jsonArr=(JSONArray) JSONArray.toJSON(listTreeByIdForSon);
                         for (int j = 0; j < jsonArr.size(); j++) {
                           JSONObject json=jsonArr.getJSONObject(j);
                           if(Integer.parseInt(json.getString("orgType"))==BusinessData.ORGNUM){//获取公司级别数据
                               listTree.add(jsonArr.getJSONObject(j)); 
                           }
                         }
                     }else{ //公司级别的 及其部门级别
                        Organization CompanyName= organizationService.getCompanyNameBySon(userOrganization.get(i).getString("pid"));//查询所属的公司名
                        JSONObject json=(JSONObject) JSONObject.toJSON(CompanyName);
                        listOrganization.add(json);
                     }
                    }
                if(listOrganization.size()>0||listTree.size()>0){
                 String[] oId=new String[listOrganization.size()+listTree.size()];//获取权限的oId
                 for (int i = 0; i < listOrganization.size(); i++) { //循环权限全部数据    
                     JSONObject pidJosn=listOrganization.get(i);
                     String id =pidJosn.getString("id"); //找到权限数据里面的组织id
                     oId[i]=id;
                 }
                 for (int i = 0; i < listTree.size(); i++) {
                     String id=listTree.get(i).getString("id");
                     int m=listOrganization.size();
                     oId[m+i]=id;
                 } 
                 List<String> oIds = Arrays.asList(oId);
                 map.put("oId", oIds);//根据权限的typeId查询相对应的数据
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
                }else{
                    capitalResult.setResultDesc("您没有权限操作资金流水数据！");
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
            //Map<String, Object> dataMap = new HashMap<String, Object>();
            CapitalByIdResult capitalByIdResult=new CapitalByIdResult();
            try {
                     if(id!=null&&!id.equals("")){
                       Capital  capital=capitalService.selectCapitalById(id);
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
                       capitalByIdResult.setResultDesc("id不能为空！");
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
            //Map<String, Object> dataMap = new HashMap<String, Object>();
            ResultUtils result=new ResultUtils();
            try {
                Capital capital =new Capital();
                if(id!=null&&!id.equals("")){
                    capital.setId(id);
                    if(classify==null){classify="";}
                    if(remarks==null){remarks="";}
                    capital.setClassify(new String(classify.getBytes("ISO-8859-1"),"UTF-8")); //修改项目分类
                    capital.setRemarks(new String(remarks.getBytes("ISO-8859-1"),"UTF-8"));  //备注
                    Integer i = capitalService.updateCapital(capital);
                    if (i == 1) {
                       ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                    } else {
                       ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                    }               
                }else{
                    result.setResultDesc("修改失败！id不能为空！");
                }
            } catch (Exception e) {
                  ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,result);
                this.logger.error(e.getMessage(), e);
            }
            return result;
        }
        
       /* *//**
         * 删除损益数据 （修改Status为0）
         * @param request
         * @return
         *//*
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
        @ApiOperation(value="删除损益数据 （修改Status为0）", notes="根据id删除资金数据（修改Status为0）")
        @ApiImplicitParam(name = "id", value = "资金流水表的id", required = true, dataType = "String")
        @ResponseBody
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                if(id!=null && !id.equals("")){
                Integer i =capitalService.deleteCapital(id);
                if (i == 1) {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                 } else {
                     dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                 }     
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }*/
        
       
        /***
         * 导入
         */
        @Transactional(rollbackFor = Exception.class)
        @RequiresPermissions("capital:import")
        @RequestMapping(value="/excelImport",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,method = RequestMethod.POST)
        @ApiOperation(value="资金流水上传", notes="资金流水表上传数据",response=ResultUtils.class)
        @ResponseBody
        public ResultUtils excelImport(@RequestPart(value="uploadFile") @ApiParam(name="uploadFile",value="文件流对象,接收数组格式",required=true) MultipartFile uploadFile,HttpServletRequest request) throws IOException{
            //Map<String, Object> dataMap = new HashMap<String, Object>();
            ResultUtils result=new ResultUtils();
            User user = (User) request.getAttribute("user");
            String uId = user.getId();
            //判断 权限的数据 公司及其公司以下的级别才可以上传数据
            List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
            boolean insertFlag = true;//上传数据是否有错
            boolean isImport = true;//是否可上传
            if(uploadFile.getOriginalFilename().contains(".")){
            String name=uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().indexOf("."));
            if(name.equals(".xls")||name.equals(".xlsx")){
            Long size=uploadFile.getSize();
            if(uploadFile.getSize()>0 && size<5242880){  //判断文件大小是否是5M以下的
              try {
                  int row=1;
                  Integer a=0;
                  List<String []> list=ExcelUtil.read(uploadFile.getInputStream(), row);//读取excel表格数据
                  String[] arr=new String[]{"爱车贷","保险","维修","车管家","技术服务","专车","融资租赁","集团总部"};
                  List<String> fauCodeList = new ArrayList<String>();
                  fauCodeList=Arrays.asList(arr);
                  List<Capital> listCapital=new ArrayList<>();
                  try {
                     for (int i = 0; i < userOrganization.size(); i++) {
                         JSONObject obu = (JSONObject) userOrganization.get(i);
                         Integer num=Integer.parseInt(obu.get("orgType").toString());
                         if(num!=Capital.DEPNUM && num!=Capital.ORGNUM){
                             isImport=false;
                        }
                     }
                     if(isImport){
                         for (int i = 0; i < list.size(); i++){
                             String[] str=list.get(i);   //在excel得到第i条数据
                             Capital capital=new Capital();
                             capital.setId(UuidUtil.getUUID());
                             Map<Object, Object> map = new HashMap<>();
                             map.put("orgName",str[5]);  //拿到excel里面的company 公司名称去下面这个组织架构里面去
                             List<Organization>  listOrganization= organizationService.listOrganizationBy(map); //查询对应的公司里面的组织架构数据
                             if(listOrganization.size()>0){
                                 capital.setoId(listOrganization.get(0).getId()); //获取公司名称对应的组织id
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第六个单元格公司名称不存在");
                                 insertFlag=false;
                                 break;  
                             }
                             if(!str[0].equals("")){
                                boolean bResult = false;
                                if(fauCodeList.contains(str[0])==true)
                                {
                                    bResult=true;
                                }
                                if(bResult==true){
                                   capital.setPlate(str[0]);
                                }else{
                                   result.setResultDesc("Excel表格第"+(i+2)+"行第一个单元格模板不存在，请核对后再上传");
                                   insertFlag=false;
                                   break; 
                                }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第一个单元格模板不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[1].equals("")){
                                 capital.setBu(str[1]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第二个单元格事业部不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[2].equals("")){
                                 capital.setRegionName(str[2]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第三个单元格大区名称不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[3].equals("")){
                                 capital.setProvince(str[3]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第四个单元格省份不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[4].equals("")){
                                 capital.setCity(str[4]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第五个单元格城市不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[5].equals("")){
                                 capital.setCompany(str[5]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第六个单元格公司名称不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[6].equals("")){
                                 capital.setAccountName(str[6]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第七个单元格账户名不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[7].equals("")){
                                 capital.setAccountBank(str[7]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第八个单元格开户行不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[8].equals("")){
                                 capital.setAccount(str[8]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第九个单元格账户不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[9].equals("")){
                                 capital.setAccountNature(str[9]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十个单元格账户性质数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[10].equals("")){
                                try {
                                    capital.setTradeTime(sdf.parse(str[10]));  
                                } catch (Exception e) {
                                    result.setResultDesc("上传的交易日期格式不对，正确的格式：2018-01-01 00:00:00");
                                    insertFlag=false;
                                    break;
                                }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十一个单元格的交易日期数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[11].equals("")){
                                 if(str[11].matches("^\\d+$")){//判断单元格数据是否是数字
                                     capital.setStartBlack(Integer.parseInt(str[11]));  
                                 }else{
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十二个单元格只能是数字");
                                     insertFlag=false;
                                     break; 
                                 }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十二个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[12].equals("")){
                                 if(str[12].matches("^\\d+$")){//判断单元格数据是否是数字
                                     capital.setIncom(Integer.parseInt(str[12]));
                                 }else{
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十三个单元格数据只能是数字");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十三个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[13].equals("")){
                                 if(str[13].matches("^\\d+$")){//判断单元格数据是否是数字
                                     capital.setPay(Integer.parseInt(str[13]));
                                 }else{
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十四个单元格数据只能是数字");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十四个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[14].equals("")){
                                 if(str[14].matches("^\\d+$")){//判断单元格数据是否是数字
                                     capital.setEndBlack(Integer.parseInt(str[14]));
                                 }else{
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十五个单元格数据只能是数字");
                                     insertFlag=false;
                                     break;
                                 }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十五个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[15].equals("")){
                                 if(str[15].length()<=200){
                                     capital.setAbstrac(str[15]); 
                                 }else{
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十六个单元格里面字数最多200字");
                                     insertFlag=false;
                                     break;   
                                 }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十六个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[16].equals("")){
                                 capital.setClassify(str[16]);
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十七个单元格数据不能为空");
                                 insertFlag=false;
                                 break;
                             }
                             if(!str[17].equals("")){
                                 if(str[17].length()<=200){
                                    capital.setRemarks(str[17]); 
                                 }else{
                                     result.setResultDesc("Excel表格第"+(i+2)+"行第十八个单元格里面最多200字");
                                     insertFlag=false;
                                     break;  
                                 }
                             }else{
                                 result.setResultDesc("Excel表格第"+(i+2)+"行第十八个单元格数据不能为空");
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
                          }
                         if (a == 1) {
                             ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                         } else {
                             ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                         }  
                     }else{
                         result.setResultDesc("您没有权限上传资金数据");
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
          result.setResultDesc("您上传的不是Excel文档！");
      }
      }else{
           result.setResultDesc("您上传的不是Excel文档！");
       }
          return result;
      }
        
        
        /***
         * 导出
         * @param response
         * @throws Exception 
         */
        @RequiresPermissions("capital:download")
        @RequestMapping(value="/export",method = RequestMethod.POST)
        @ApiOperation(value="导出资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的) 并且导出",response = ResultUtils.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "plate", value = "所属的板块", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "bu", value = "所属事业部门（如财务部）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "regionName", value = "所属大区的名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "province", value = "所属省份名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "company", value = "所属公司名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeBeg", value = "开始交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeEnd", value = "结束交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String",paramType = "query")})
        @ResponseBody
        public void export(HttpServletRequest request,HttpServletResponse response,String plate,String bu,String regionName,String province,String company,
                String accountBank,String accountNature,String tradeTimeBeg,String tradeTimeEnd,String classify) throws Exception{
            OutputStream os = null;
            //Map<String, Object> dataMap = new HashMap<String, Object>();
            ResultUtils result=new ResultUtils();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(plate==null){ plate="";};
                if(bu==null){ bu="";};
                if(regionName==null){ regionName="";};
                if(province==null){ province="";};
                if(company==null){ company="";};
                if(accountBank==null){ accountBank="";};
                if(accountNature==null){ accountNature="";};
                if(tradeTimeBeg==null){ tradeTimeBeg="";};
                if(tradeTimeEnd==null){ tradeTimeEnd="";};
                if(classify==null){ classify="";};
                map.put("plate",new String(plate.getBytes("ISO-8859-1"),"UTF-8")); //板块
                map.put("bu",new String(bu.getBytes("ISO-8859-1"),"UTF-8"));//事业部
                map.put("regionName",new String(regionName.getBytes("ISO-8859-1"),"UTF-8"));//大区名称
                map.put("province",new String(province.getBytes("ISO-8859-1"),"UTF-8"));//省份
                map.put("company",new String(company.getBytes("ISO-8859-1"),"UTF-8"));//公司名称
                map.put("accountBank",new String(accountBank.getBytes("ISO-8859-1"),"UTF-8"));//开户行
                map.put("accountNature",new String(accountNature.getBytes("ISO-8859-1"),"UTF-8"));//账户性质
                map.put("tradeTimeBeg",tradeTimeBeg);//交易起始日期
                map.put("tradeTimeEnd",tradeTimeEnd);//交易结束日期
                map.put("classify",new String(classify.getBytes("ISO-8859-1"),"UTF-8"));//项目分类
                //判断 权限的数据 
                List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
                List<JSONObject> listOrganization=new ArrayList<>();   //筛选过后就的权限数据
                List<JSONObject> listTree=new ArrayList<>();   //筛选过后就的权限数据
                for (int i = 0; i < userOrganization.size(); i++) {
                    JSONObject obu = (JSONObject) userOrganization.get(i);
                    Integer num=Integer.parseInt(obu.get("orgType").toString());
                    String name=obu.getString("name").toString();
                    if(num<BusinessData.ORGNUM &&!name.contains(Capital.NAME)) {//大于公司级别并且不包含汇总 就查询以下的公司数据
                        //查询该节点下的所有子节点集合 获取公司的级别
                        List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(userOrganization.get(i).getString("pid"));
                        JSONArray jsonArr=(JSONArray) JSONArray.toJSON(listTreeByIdForSon);
                        for (int j = 0; j < jsonArr.size(); j++) {
                          JSONObject json=jsonArr.getJSONObject(j);
                          if(Integer.parseInt(json.getString("orgType"))==BusinessData.ORGNUM){//获取公司级别数据
                              listTree.add(jsonArr.getJSONObject(j)); 
                          }
                        }
                    }else{
                        //公司级别的 及其部门级别
                        Organization CompanyName= organizationService.getCompanyNameBySon(userOrganization.get(i).getString("pid"));//查询所属的公司名
                        JSONObject json=(JSONObject) JSONObject.toJSON(CompanyName);
                        listOrganization.add(json);
                    }
                   }
                if(listOrganization.size()>0 ||listTree.size()>0){
                    String[] oId=new String[listOrganization.size()+listTree.size()];//获取权限的oId
                    for (int i = 0; i < listOrganization.size(); i++) { //循环权限全部数据    
                        JSONObject pidJosn=userOrganization.get(i);
                        String pid =pidJosn.getString("pid"); //找到权限数据里面的组织id
                        oId[i]=pid;
                    }
                    for (int i = 0; i < listTree.size(); i++) {
                        String id=listTree.get(i).getString("id");
                        int m=listOrganization.size();
                        oId[m+i]=id;
                    }  
                    List<String> oIds = Arrays.asList(oId);
                    map.put("oId", oIds);//根据权限的typeId查询相对应的数据
                   List<Capital> listData = capitalService.capitalExport(map); //根据权限oId查询里面的权限数据
                  List<String[]> strList=new ArrayList<>();
                    String[] ss={"模板","事业部","大区名称","省份","城市","公司名称","户名","开户行","账户","账户性质",
                            "交易日期","期初余额","本期收入","本期支出","期末余额","摘要","项目分类","备注"};
                    strList.add(ss);
                    for (int i = 0; i < listData.size(); i++) {
                        String[] str=new String[18];
                        Capital capital=listData.get(i);
                        if(capital.getPlate()!=null &&!capital.getPlate().equals("")){
                            str[0]=capital.getPlate();
                         }
                        if(capital.getBu()!=null &&!capital.getBu().equals("")){
                            str[1]=capital.getBu();
                         }
                        if(capital.getRegionName()!=null &&!capital.getRegionName().equals("")){
                            str[2]=capital.getRegionName();
                         }
                        if(capital.getProvince()!=null &&!capital.getProvince().equals("")){
                            str[3]=capital.getProvince();
                         }
                        if(capital.getCity()!=null &&!capital.getCity().equals("")){
                            str[4]=capital.getCity();
                         }
                        if(capital.getCompany()!=null &&!capital.getCompany().equals("")){
                            str[5]=capital.getCompany();
                         }
                        if(capital.getAccountName()!=null && !capital.getAccountName().equals("")){
                           str[6]=capital.getAccountName();
                        }
                        if(capital.getAccountBank()!=null&&!capital.getAccountBank().equals("")){
                           str[7]=capital.getAccountBank();
                        }
                        if(capital.getAccount()!=null&&!capital.getAccount().equals("")){
                           str[8]=capital.getAccount();
                        }
                        if(capital.getAccountNature()!=null &&!capital.getAccountNature().equals("")){
                           str[9]=capital.getAccountNature();
                        }
                        if(capital.getTradeTime()!=null&&!capital.getTradeTime().equals("")){
                           str[10]=sdf.format(capital.getTradeTime());
                        }
                        if(capital.getStartBlack()!=null &&!capital.getStartBlack().equals("")){
                           str[11]=capital.getStartBlack().toString();
                        }
                        if(capital.getIncom()!=null &&!capital.getIncom().equals("")){
                           str[12]=capital.getIncom().toString();
                        }
                        if(capital.getPay()!=null&&!capital.getPay().equals("")){
                           str[13]=capital.getPay().toString();
                        }
                        if(capital.getEndBlack()!=null&&!capital.getEndBlack().equals("")){
                           str[14]=capital.getEndBlack().toString();
                        }
                        if(capital.getAbstrac()!=null&&!capital.getAbstrac().equals("")){
                            str[15]=capital.getAbstrac();
                        }
                        if(capital.getClassify()!=null&&!capital.getClassify().equals("")){
                            str[16]=capital.getClassify();
                        }
                        if(capital.getRemarks()!=null&&!capital.getRemarks().equals("")){
                            str[17]=capital.getRemarks();
                        } 
                            strList.add(str);
                    }
                    response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("资金流水表", "UTF-8")+".xls");
                    response.setContentType("application/octet-stream");
                    os = response.getOutputStream();
                    ExcelUtil.export(strList, os);
                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
                }else{
                    result.setResultDesc("您没有权限导出资金数据"); 
                }
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
        }
        
}
