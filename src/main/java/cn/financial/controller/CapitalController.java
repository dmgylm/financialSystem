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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.financial.model.Capital;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
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
        @ApiOperation(value="查询资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的)",response = Capital.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "page", value = "查询数据的开始页码（第一页开始）page=1", required = true, dataType = "integer", paramType = "query"),
                @ApiImplicitParam(name = "pageSize", value = "每页显示数据的条数（如每页显示10条数据）", required = true, dataType = "integer", paramType = "query"),
                @ApiImplicitParam(name = "plate", value = "所属的板块", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "BU", value = "所属事业部门（如财务部）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "regionName", value = "所属大区的名称", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "province", value = "所属省份名称", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "company", value = "所属公司名称", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeBeg", value = "开始交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeEnd", value = "结束交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String", paramType = "query")})
        @ResponseBody
        public Map<String, Object> listCapitalBy(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                    map.put("plate",request.getParameter("plate")); //板块
                }
                if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                    map.put("BU",request.getParameter("BU"));//事业部
                }
                if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                    map.put("regionName",request.getParameter("regionName"));//大区名称
                }
                if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                    map.put("province",request.getParameter("province"));//省份
                }
                if(request.getParameter("company")!=null && !request.getParameter("company").equals("")){
                    map.put("company",request.getParameter("company"));//公司名称
                }
                if(request.getParameter("accountBank")!=null && !request.getParameter("accountBank").equals("")){
                    map.put("accountBank",request.getParameter("accountBank"));//开户行
                }
                if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                    map.put("accountNature",request.getParameter("accountNature"));//账户性质
                }
                if(request.getParameter("tradeTimeBeg")!=null && !request.getParameter("tradeTimeBeg").equals("")){
                    map.put("tradeTimeBeg",(request.getParameter("tradeTimeBeg")));//交易起始日期
                }
                if(request.getParameter("tradeTimeEnd")!=null && !request.getParameter("tradeTimeEnd").equals("")){
                    map.put("tradeTimeEnd",request.getParameter("tradeTimeEnd"));//交易结束日期
                }
                if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                    map.put("classify",request.getParameter("classify"));//项目分类
                }
                List<UserOrganization> userOrganization= userOrganizationService.listUserOrganization(uId); //判断 权限的数据
                if(userOrganization.size()>0){
                 String[] oId=new String[userOrganization.size()];//获取权限的oId
                 for (int i = 0; i < userOrganization.size(); i++) {
                     String id=userOrganization.get(i).getoId(); //找到权限数据里面的oId
                     oId[i]=id;
                 }  
                 List<String> oIds = Arrays.asList(oId);
                 map.put("oId", oIds);//根据权限的typeId查询相对应的数据
                 List<Capital> total = capitalService.capitalExport(map); //根据权限oId查询里面的权限的全部数据未经过分页
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
                 dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                 dataMap.put("data", list);
                 dataMap.put("total", total.size());
                }else{
                    throw new Exception("您没有权限查看资金流水数据！"); 
                }
               /* List<Capital> list = capitalService.listCapitalBy(map); //查询全部符合条件的数据
                JSONArray arr=JSONArray.fromObject(list);  
                List<Capital> listData=new ArrayList<>();
                for (int i = 0; i < jsonArr.size(); i++) { //循环全部数据    
                    JSONObject jsonObject=jsonArr.getJSONObject(i);
                    String id=jsonObject.getString("oId"); //找到权限数据里面的oId
                    //找权限里面的公司名称  去全部数据里面去匹配   如果有这个公司存在的话  这条数据是展示的是需要的   如果没这个公司存在就剔除这条数据
                    for (int j = 0; j < arr.size(); j++) {
                        JSONObject jsonObj=arr.getJSONObject(j);
                        String arrId=jsonObj.get("oId").toString();//找全部资金流水表的oId
                        if(id.equals(arrId)){  //判断权限oId 和全部数据的oId是否相同  
                            listData.add(list.get(j));  // 可以显示的资料流水数据
                        }
                    }
                }*/
               /* if(listData.size()>0){  //判断是否有符合权限的数据  没有则是抛出异常  有就进行数据分页传到前台
                    Integer p=(page - 1) * pageSize; //开始下标
                    Integer s=page* pageSize;  //结束下标
                    Integer totalPage = listData.size() / pageSize; //总页数
                    if (listData.size() % pageSize != 0){
                        totalPage++;
                    }
                    List<Capital> subList =new ArrayList<>();
                    if(listData.size()<pageSize){    //判断总得数据长度是否小于每页大小
                        subList=listData.subList(0,listData.size());
                    }else if(listData.size()<s){     //判断总得数据长度是否小于结束下标大小
                        subList=listData.subList(p,listData.size());
                    }else{
                        subList=listData.subList(p,s);
                    }
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    dataMap.put("data", subList);
                    dataMap.put("totalPage", totalPage);
                }else{
                    throw new Exception("您没有权限查看资金流水数据！");
                }*/
                /*dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("data", subList);*/
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 根据id查询资金数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @ApiOperation(value="根据id查询资金数据", notes="根据url的id来获取资金流水的信息")
        @ApiImplicitParams({@ApiImplicitParam(name="id",value="资金的id",dataType="string", paramType = "query", required = true)})
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> selectCapitalById(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                if(request.getParameter("id")!=null&&!request.getParameter("id").equals("")){
                   Capital  Capital=capitalService.selectCapitalById(request.getParameter("id"));
                   Date  newTime=new Date();
                   Date begTime=Capital.getCreateTime(); //得到开始时间
                   Date endTime=Capital.getUpdateTime(); //得到更新时间
                   Integer num=TimeUtils.daysBetween(begTime, newTime); //比较开始到现在的时间差
                   Integer editor=Capital.getEditor();
                   if(num<=7||endTime==null){
                       editor=1;//可编辑数据
                   }
                   Capital.setEditor(editor);
                   dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                   dataMap.put("data", Capital);
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
     
        
        /**
         * 修改资金数据
         * @param request
         * @return
         */
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        @ApiOperation(value="修改资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的)")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "资金流水表id", required = true, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "plate", value = "所属的板块", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "BU", value = "所属事业部门（如财务部）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "regionName", value = "所属大区的名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "province", value = "所属省份名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "city", value = "所属城市名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "company", value = "所属公司名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountName", value = "账户名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "account", value = "账户", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "tradeTime", value = "交易日期", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "startBlack", value = "期初余额", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "income", value = "本期收入", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "pay", value = "本期支出", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "endBlack", value = "期末余额", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "abstrac", value = "摘要", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "month", value = "月份", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "remarks", value = "备注", required = false, dataType = "String",paramType = "query")})
        @ResponseBody
        public Map<String, Object> updateCapital(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                Capital capital =new Capital();
                if(request.getParameter("id")!=null && !request.getParameter("id").equals("")){
                    capital.setId(request.getParameter("id"));
                }
                if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                    capital.setPlate(new String(request.getParameter("plate").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                    capital.setBU(new String(request.getParameter("BU").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                    capital.setRegionName(new String(request.getParameter("regionName").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                    capital.setProvince(new String(request.getParameter("province").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("city")!=null && !request.getParameter("city").equals("")){
                    capital.setCity(new String(request.getParameter("city").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("company")!=null && !request.getParameter("company").equals("")){
                    capital.setCompany(new String(request.getParameter("company").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountName")!=null && !request.getParameter("accountName").equals("")){
                    capital.setAccountName(new String(request.getParameter("accountName").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountBank")!=null && !request.getParameter("accountBank").equals("")){
                    capital.setAccountBank(new String(request.getParameter("accountBank").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("account")!=null && !request.getParameter("account").equals("")){
                    capital.setAccount(new String(request.getParameter("account").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                    capital.setAccountNature(new String(request.getParameter("accountNature").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("tradeTime")!=null && !request.getParameter("tradeTime").equals("")){
                    capital.setTradeTime(sdf.parse(request.getParameter("tradeTime")));
                }
                if(request.getParameter("startBlack")!=null && !request.getParameter("startBlack").equals("")){
                    capital.setStartBlack(Integer.getInteger(request.getParameter("startBlack")));
                }
                if(request.getParameter("incom")!=null && !request.getParameter("incom").equals("")){
                    capital.setIncom(Integer.getInteger(request.getParameter("incom")));
                }
                if(request.getParameter("pay")!=null && !request.getParameter("pay").equals("")){
                    capital.setPay(Integer.getInteger(request.getParameter("pay")));
                }
                if(request.getParameter("endBlack")!=null && !request.getParameter("endBlack").equals("")){
                    capital.setEndBlack(Integer.getInteger(request.getParameter("endBlack")));
                }
                if(request.getParameter("abstrac")!=null && !request.getParameter("abstrac").equals("")){
                    capital.setAbstrac(new String(request.getParameter("abstrac").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                    capital.setClassify(new String(request.getParameter("classify").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("remarks")!=null && !request.getParameter("remarks").equals("")){
                    capital.setRemarks(new String(request.getParameter("remarks").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(uId!=null && !uId.equals("")){
                    capital.setuId(uId);
                }
                if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                    capital.setYear(Integer.getInteger(request.getParameter("year")));
                }
                if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                   capital.setMonth(Integer.getInteger(request.getParameter("month")));
                }
                capital.setStatus(1);
                capital.setEditor(0);
                Integer i = capitalService.updateCapital(capital);
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
        @RequestMapping(value="/excelImport",method = RequestMethod.POST)
        @ApiOperation(value="资金流水上传", notes="资金流水表上传数据")
        @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件流对象,接收数组格式", required = true,dataType = "MultipartFile",paramType = "query")
           })
        @ResponseBody
        public void excelImport(@RequestParam(value="file") MultipartFile uploadFile,HttpServletRequest request) throws IOException{
            Map<String, Object> dataMap = new HashMap<String, Object>();
            User user = (User) request.getAttribute("user");
            String uId = user.getId();
            List<UserOrganization> userOrganization= userOrganizationService.listUserOrganization(uId); //根据提交人id查询 本人所拥有的权限
            JSONArray jsonArr=JSONArray.fromObject(userOrganization);
            if(uploadFile.getSize()>0 && uploadFile.getSize()<5242880){  //判断文件大小是否是5M以下的
              try {
                  int row=1;
                  Integer a=0;
                  List<String []> list=ExcelUtil.read(uploadFile.getInputStream(), row);//读取excel表格数据
                 try {
                     Integer num=0;  //判断excel里面是否 存在 没有在权限内的数据    1表示不存在 不存在才能进行导入 0表示存在  抛出异常
                     for (int i = 0; i < list.size(); i++) {  //判断excel表格里面是否有不符合权限的数据  如果有就不能导入,抛出异常
                         String[] str=list.get(i);   //在excel得到第i条数据
                         Map<Object, Object> map = new HashMap<>();
                         map.put("orgName",str[5]);  //拿到excel里面的company 公司名称去下面这个组织架构里面去
                         List<Organization>  listOrganization= organizationService.listOrganizationBy(map); //查询对应的公司里面的组织架构数据
                         //List<Capital> listData=new ArrayList<>();
                         if(listOrganization.size()>0){  //如果不存在该公司的组织数据，也不能导入
                             for (int k = 0; k < jsonArr.size(); k++) { //循环权限里面全部数据    
                                 JSONObject jsonObject=jsonArr.getJSONObject(k);
                                 String oId=jsonObject.getString("oId"); //找到权限数据里面的oId
                                 //找权限里面的oId组织架构id  去全部数据里面去匹配   如果有这个oId的话  证明有新增资金流水的权限
                                 String id=listOrganization.get(0).getId();
                                 if(id.equals(oId)){
                                     num=1;
                                 }
                               }
                              }else{
                                num=0;
                                break;
                              }
                          }
                         if(num==1){  //没有不符合权限的数据  进行导入
                             for (int i = 0; i < list.size(); i++){
                                 String[] str=list.get(i);   //在excel得到第i条数据
                                 Capital capital=new Capital();
                                 capital.setId(UuidUtil.getUUID());
                                 Map<Object, Object> map = new HashMap<>();
                                 map.put("orgName",str[5]);  //拿到excel里面的company 公司名称去下面这个组织架构里面去
                                 List<Organization>  listOrganization= organizationService.listOrganizationBy(map); //查询对应的公司里面的组织架构数据
                                 capital.setoId(listOrganization.get(0).getId()); //获取公司名称对应的组织id
                                 capital.setPlate(str[0]);
                                 capital.setBU(str[1]);
                                 capital.setRegionName(str[2]);
                                 capital.setProvince(str[3]);
                                 capital.setCity(str[4]);
                                 capital.setCompany(str[5]);
                                 capital.setAccountName(str[6]);
                                 capital.setAccountBank(str[7]);
                                 capital.setAccount(str[8]);
                                 capital.setAccountNature(str[9]);
                                 capital.setTradeTime(sdf.parse(str[10]));
                                 capital.setStartBlack(Integer.parseInt(str[11]));
                                 capital.setIncom(Integer.parseInt(str[12]));
                                 capital.setPay(Integer.parseInt(str[13]));
                                 capital.setEndBlack(Integer.parseInt(str[14]));
                                 capital.setAbstrac(str[15]);
                                 capital.setClassify(str[16]);
                                 capital.setRemarks(str[17]);
                                 Calendar calendar = Calendar.getInstance();
                                 calendar.setTime(calendar.getTime());
                                 capital.setuId(uId);
                                 capital.setYear(calendar.get(Calendar.YEAR));
                                 capital.setMonth(calendar.get(Calendar.MONTH));
                                 capital.setStatus(1);
                                 capital.setEditor(0);
                                 a = capitalService.insertCapital(capital); //导入新增的数据
                                 if (a == 1) {
                                     dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                                     dataMap.put("result","导入成功！");
                                 } else {
                                     dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                                     dataMap.put("result","导入失败！");
                                 }
                             }
                        }else{
                            throw new Exception("您没有权限导入资金数据！"); 
                        }
                 } catch (Exception e) {
                     dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
                     this.logger.error(e.getMessage(), e);
                 }
                 
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
                this.logger.error(e.getMessage(), e);
            }
        }else{
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.CAPITAL_FILE_EXCEED_5M));
        } 
      }
        
        
        /***
         * 导出
         * @param response
         * @throws Exception 
         */
        @RequiresPermissions("capital:download")
        @RequestMapping(value="/export",method = RequestMethod.POST)
        @ApiOperation(value="导出资金流水数据", notes="根据条件查资金数据 (不传数据就是查询所有的) 并且导出",response = Capital.class)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "plate", value = "所属的板块", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "BU", value = "所属事业部门（如财务部）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "regionName", value = "所属大区的名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "province", value = "所属省份名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "company", value = "所属公司名称", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountBank", value = "开户的银行", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "accountNature", value = "账户性质", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeBeg", value = "开始交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "tradeTimeEnd", value = "结束交易日期（格式：2018-01-02 00:00:00）", required = false, dataType = "String",paramType = "query"),
                @ApiImplicitParam(name = "classify", value = "项目分类", required = false, dataType = "String",paramType = "query")})
        @ResponseBody
        public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{
            OutputStream os = null;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                    map.put("plate",request.getParameter("plate")); //板块
                }
                if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                    map.put("BU",request.getParameter("BU"));//事业部
                }
                if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                    map.put("regionName",request.getParameter("regionName"));//大区名称
                }
                if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                    map.put("province",request.getParameter("province"));//省份
                }
                if(request.getParameter("company")!=null && !request.getParameter("company").equals("")){
                    map.put("company",request.getParameter("company"));//公司名称
                }
                if(request.getParameter("accountBank")!=null && !request.getParameter("accountBank").equals("")){
                    map.put("accountBank",request.getParameter("accountBank"));//开户行
                }
                if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                    map.put("accountNature",request.getParameter("accountNature"));//账户性质
                }
                if(request.getParameter("tradeTimeBeg")!=null && !request.getParameter("tradeTimeBeg").equals("")){
                    map.put("tradeTimeBeg",(request.getParameter("tradeTimeBeg")));//交易起始日期
                }
                if(request.getParameter("tradeTimeEnd")!=null && !request.getParameter("tradeTimeEnd").equals("")){
                    map.put("tradeTimeEnd",request.getParameter("tradeTimeEnd"));//交易结束日期
                }
                
                if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                    map.put("classify",request.getParameter("classify"));//项目分类
                }
                List<UserOrganization> userOrganization= userOrganizationService.listUserOrganization(uId); //判断 权限的数据
                if(userOrganization.size()>0){
                 String[] oId=new String[userOrganization.size()];//获取权限的oId
                 for (int i = 0; i < userOrganization.size(); i++) {
                     String id=userOrganization.get(i).getoId(); //找到权限数据里面的oId
                     oId[i]=id;
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
                        if(capital.getBU()!=null &&!capital.getBU().equals("")){
                            str[1]=capital.getBU();
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
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    dataMap.put("result","导出成功！");
                }else{
                    throw new Exception("您没有权限导出资金数据！"); 
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
