package cn.financial.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.financial.model.Business;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.response.BusinessDataYearResult;
import cn.financial.model.response.BusinessResult;
import cn.financial.model.response.HtmlResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.service.UserService;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.ExcelReckonUtils;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertExcel;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.UuidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 损益表Controller
 * 
 * @author lmn
 *
 */
@Api(tags = "损益/预算数据")
@Controller
@RequestMapping("/businessData")
public class BusinessDataController {

    @Autowired
    private BusinessDataService businessDataService;

    @Autowired
    private BusinessDataInfoServiceImpl businessDataInfoServiceImpl;

    @Autowired
    private DataModuleService dataModuleService;

    @Autowired
    private UserOrganizationService userOrganizationService; // 角色的权限

    @Autowired
    private OrganizationService organizationService; // 组织架构

    @Autowired
    private DataModuleService dmService;
    
    
    @Autowired
    private UserService userService;

    @Autowired
    private BusinessDataInfoService businessDataInfoService;

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
    @RequestMapping(value = "/listBy", method = RequestMethod.POST)
    @ApiOperation(value = "查询损益/预算数据", notes = "根据条件查数据 (不传数据就是查询所有的)", response = BusinessResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "查询数据的开始页码（第一页开始）page=1", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据的条数（如每页显示10条数据）", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月份", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字搜索,地区，公司名字等", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sId", value = "判断是损益还是预算表  1损益  2 预算", required = false, dataType = "String", paramType = "query"), })
    @ResponseBody
    public BusinessResult listBusinessDataBy(HttpServletRequest request, String keyword, String year, String month,
            Integer sId, Integer page, Integer pageSize) {
        BusinessResult businessResult = new BusinessResult();
        try {
            if(keyword!=null&&!keyword.equals("")){
                keyword= new String(keyword.getBytes("iso8859-1"),"utf-8");
            }
            Map<Object, Object> map = new HashMap<>();
            User user = (User) request.getAttribute("user");
            String uId = user.getId();
            List<JSONObject> userOrganization = userOrganizationService.userOrganizationList(uId); // 判断 权限的数据
            if(userOrganization.size()>0){//有权限数据
                List<String> code=new ArrayList<>();
                for (int i = 0; i < userOrganization.size(); i++) {
                    String str=userOrganization.get(i).getString("his_permission");
                    if(str.contains(",")){
                        String[] his_permission=str.split(","); 
                        for (int j = 0; j < his_permission.length; j++) {
                          code.add(his_permission[j]); 
                        }  
                    }else{
                        code.add(str);
                    }
                }
                map.put("code", code);
                map.put("codee", code);
                map.put("orgName", keyword); 
                map.put("year", year); // 年份
                map.put("month", month); // 月份
                if (sId == null || sId < 1 || sId > 2) {
                    map.put("sId", ""); // 判断是损益还是预算表 1损益 2 预算
                } else {
                    map.put("sId", sId); // 判断是损益还是预算表 1损益 2 预算
                }
                List<BusinessData> total = businessDataService.businessDataExport(map); // 查询权限下的所有数据 未经分页
                if (pageSize == null || pageSize == 0) {
                    map.put("pageSize", 10);
                } else {
                    map.put("pageSize", pageSize);
                }
                if (page == null) {
                    map.put("start", 0);
                } else {
                    map.put("start", pageSize * (page - 1));
                }
                List<BusinessData> businessData = businessDataService.listBusinessDataBy(map); // 查询权限下分页数据
                List<Business> businessList = new ArrayList<>();
                for (int i = 0; i < businessData.size(); i++) {
                    Business business = new Business();
                    business.setId(businessData.get(i).getId());// id
                    business.setYear(businessData.get(i).getYear()); // 年份
                    business.setMonth(businessData.get(i).getMonth()); // 月份
                    User userById=userService.getUserById(businessData.get(i).getuId());
                    if(userById!=null){
                      business.setUserName(userById.getName()); // 用户    
                    }
                    business.setUpdateTime(businessData.get(i).getUpdateTime()); // 操作时间
                    business.setStatus(businessData.get(i).getStatus());// 状态
                    business.setCompany(businessData.get(i).getCompanyName()); // 公司
                    business.setStructures(businessData.get(i).getOrgName()); // 业务方式
                    businessList.add(business);
                  }
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, businessResult);
                businessResult.setData(businessList); // 返回的资金流水数据
                businessResult.setTotal(total.size());// 返回的总条数    
            }else{
                List<Business> businessList = new ArrayList<>();
                ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, businessResult);
                businessResult.setData(businessList); // 返回的资金流水数据
                businessResult.setTotal(businessList.size());// 返回的总条数  
            }
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, businessResult);
            this.logger.error(e.getMessage(), e);
        }
        return businessResult;
    }

    /**
     * 根据id查询损益数据
     * 
     * @param request
     * @param id
     *            业务表id
     * @param htmlType
     *            1.HTML类型:配置模板 2HTML类型:录入模板
     * 
     * @return
     */
    @RequiresPermissions("businessData:view")
    @RequestMapping(value = "/listById", method = RequestMethod.POST)
    @ApiOperation(value = "根据id查询损益/预算数据", notes = "根据url的id来获取损益/预算数据", response = HtmlResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "表id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "htmlType", value = "1：HTML类型:配置模板  2：HTML类型:录入页面 3：HTML类型:查看页面 这里的htmlType是2", required = true, dataType = "integer", paramType = "query") })
    @ResponseBody
    public HtmlResult selectBusinessDataById(HttpServletRequest request, String id, Integer htmlType) {
        HtmlResult htmlResult = new HtmlResult();
        try {
            User user = (User) request.getAttribute("user");
            String uId = user.getId();
            BusinessData businessData = businessDataService.selectBusinessDataById(id);//查询id的数据
            List<Organization>  listOrganization=organizationService.listOrganizationBy("", "", "",businessData.getTypeId(), "", "", "", null, null);
            Integer status=listOrganization.get(0).getStatus();
            List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
            boolean isImport = isImport(userOrganization);//是否可编辑
            if (id != null && !id.equals("") && htmlType != null) {
                if(htmlType==2&&isImport==true){ //有权限编辑录入中心页面
                   if(status==0){
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,htmlResult);
                        htmlResult.setResultDesc("当前组织架构数据已被停用！不能进行编辑！"); 
                    }else{
                        DataModule dm = dmService.getDataModule(businessData.getDataModuleId());
                        BusinessDataInfo busInfo = businessDataInfoService.selectBusinessDataById(id);
                        JSONObject joTemp = JSONObject.parseObject(dm.getModuleData());
                        JSONObject joInfo = JSONObject.parseObject(busInfo.getInfo());
                        //JsonConvertProcess.mergeJson(joTemp, joInfo);
                        HtmlGenerate htmlGenerate = new HtmlGenerate();
                        htmlGenerate.disableBudgetInput();
                        String html = htmlGenerate.generateHtml(JsonConvertProcess.mergeJson(joTemp, joInfo), htmlType);
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, htmlResult);
                        htmlResult.setData(html);     
                    }
                }else if(htmlType==3){//查看详情
                    DataModule dm = dmService.getDataModule(businessData.getDataModuleId());
                    BusinessDataInfo busInfo = businessDataInfoService.selectBusinessDataById(id);
                    JSONObject joTemp = JSONObject.parseObject(dm.getModuleData());
                    JSONObject joInfo = JSONObject.parseObject(busInfo.getInfo());
                    //JsonConvertProcess.mergeJson(joTemp, joInfo);
                    HtmlGenerate htmlGenerate = new HtmlGenerate();
                    String html = htmlGenerate.generateHtml(JsonConvertProcess.mergeJson(joTemp, joInfo), htmlType);
                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, htmlResult);
                    htmlResult.setData(html); 
                }else{
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,htmlResult);
                    htmlResult.setResultDesc("您当前没有权限进行此操作！"); 
                }
            } else {
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,htmlResult);
                htmlResult.setResultDesc("id或者htmlType为空！");
            }
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, htmlResult);
            this.logger.error(e.getMessage(), e);
        }
        return htmlResult;
    }

    /**
     * 修改损益数据
     * 
     * @param request
     * 
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("businessData:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改损益/预算的数据", notes = "根据条件修改损益/预算数据", response = ResultUtils.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "strJson", value = "传的json格式:[{'name':'name1','value':'value1'},{'name':'name2','value':'value2'}]", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "表id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "传过来的状态（1保存 , 2提交   ）", required = false, dataType = "String", paramType = "query") })
    @ResponseBody
    public ResultUtils updateBusinessData(HttpServletRequest request, String strJson, Integer status, String id) {
        // 需要参数，前端传来的HTML，业务表的id，状态（1保存 2提交 4退回 ） 0 待提交 1待修改 2已提交 3新增 4 退回修改
        ResultUtils result = new ResultUtils();
        User user = (User) request.getAttribute("user");
        String uId=user.getId();
        try {
            BusinessData business = businessDataService.selectBusinessDataById(id);//查询id的数据
            List<Organization>  listOrganization=organizationService.listOrganizationBy("", "", "",business.getTypeId(), "", "", "", null, null);
            Integer isStatus=listOrganization.get(0).getStatus();//判断组织架构是否被停用  0表示停用已经被删除
            List<JSONObject> userOrganization= userOrganizationService.userOrganizationList(uId); //判断 权限的数据 
            boolean isImport = isImport(userOrganization);//是否可编辑
            if(isImport){
             if(isStatus==0){
                 ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                 result.setResultDesc("当前组织架构数据已被停用！不能进行编辑！");      
             }else{
             // 如果有html则是在编辑页面提交（可能是保存 或者提交）
                if (strJson != null && !strJson.equals("") && id != null && !id.equals("")) {
                    if (status == 1 || status == 2) {
                        if (status == 1) {// 如果状态是保存 数据库状态修改为 待提交 0 否者 改为已提交 2
                            status = 0;
                        } else {
                            status = 2;// 否者 改为已提交 2
                        }
                        /*
                         * Document doc = Jsoup.parse(html);// 得到html // Document doc =
                         * Jsoup.parse(file, "UTF-8", "http://example.com/"); Elements inputHtml =
                         * doc.select("input");// 获取HTML所有input属性
                         */ /* System.out.println(inputHtml); */
                        BusinessData businessDataById = businessDataService.selectBusinessDataById(id); // 查询出表的数据 得到模板id
                        if(status == 2 && businessDataById.getStatus()==status&&businessDataById.getsId()==1){
                            ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                            result.setResultDesc("此数据已经提交！不能重复提交"); 
                        }else{
                            JSONArray jsonArr = JSONArray.parseArray(strJson);
                            Map<String, Object> mo = new HashMap<String, Object>();
                            for (int i = 0; i < jsonArr.size(); i++) {
                                JSONObject json = jsonArr.getJSONObject(i);
                                mo.put(json.getString("name"), json.getString("value"));
                            }
                            DataModule dm = dataModuleService.getDataModule(businessDataById.getDataModuleId());// 获取原始模板
                            JSONObject dataMjo = JSONObject.parseObject(dm.getModuleData());// 获取损益表数据模板
                            /*
                             * Map<String, Object> mo = new HashMap<String, Object>(); for (int i = 0; i <
                             * inputHtml.size(); i++) {// 解析HTML获取所有input name和value值
                             * mo.put(inputHtml.get(i).attr("name"), inputHtml.get(i).val()); }
                             */
                            System.out.println("模板类型~"+dm.getReportType());
                           // System.out.println("shuchu"+businessDataInfoServiceImpl.dgkey(dataMjo, mo,dm.getReportType()));
                          
                            ExcelReckonUtils excelReckonUtils=new ExcelReckonUtils();
                            String merjson=businessDataInfoServiceImpl.dgkey(dataMjo, mo,dm.getReportType()).toString();
                            String newBudgetHtml = excelReckonUtils.computeByExcel(merjson);
                            //System.out.println("excelReckonUtils"+newBudgetHtml);
                            BusinessData businessData = new BusinessData();
                            businessData.setId(id);
                            businessData.setuId(uId);
                            businessData.setStatus(status);
                            // map.put("info",JsonConvertProcess.simplifyJson(newBudgetHtml).toString());
                            Integer i = businessDataService.updateBusinessData(businessData); // 修改损益表/预算的状态
                            if (i == 1) {
                                // 修改从表的info
                                BusinessDataInfo selectBusinessDataById = businessDataInfoService.selectBusinessDataById(id); // 查询出从表的数据
                                BusinessDataInfo businessDataInfo = new BusinessDataInfo();
                                businessDataInfo.setId(selectBusinessDataById.getId());
                                System.out.println("simplifyJson"+JsonConvertProcess.simplifyJson(newBudgetHtml).toString());
                                businessDataInfo.setInfo(JsonConvertProcess.simplifyJson(newBudgetHtml).toString());
                                Integer infoId = businessDataInfoService.updateBusinessDataInfo(businessDataInfo);
                                if (infoId == 1) {
                                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                                    result.setResultDesc("修改成功");
                                } else {
                                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                                    result.setResultDesc("修改失败");
                                }
                            } else {
                                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                                result.setResultDesc("修改失败");
                            } 
                        }
                    } else {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                        result.setResultDesc("您所给的状态不对！状态（1保存 , 2提交   ）");
                    }
                } else if (status == 2 && id != null && !id.equals("")) { // 如果html为空，则是直接提交（此时状态是提交 2）只需要把损益表的状态修改下
                    BusinessData businessDataById = businessDataService.selectBusinessDataById(id); // 查询出表的数据 得到模板id
                    if(businessDataById.getStatus()==status&&businessDataById.getsId()==1){
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                        result.setResultDesc("此数据已经提交！不能重复提交");  
                    }else{
                        BusinessData businessData = new BusinessData();
                        businessData.setId(id);
                        businessData.setuId(uId);
                        businessData.setStatus(status);
                        Integer i = businessDataService.updateBusinessData(businessData); // 修改损益表/预算的状态
                        if (i == 1) {
                            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                            result.setResultDesc("修改成功");
                        } else {
                            ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                            result.setResultDesc("修改失败");
                        }  
                    }
                } else {
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                    result.setResultDesc("id不能为空");
                } 
             }
            }else{
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                result.setResultDesc("您当前所属的组织架构没有此操作权限！"); 
            }
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);

            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 修改退回的信息
     * 
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("businessData:update")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "修改退回的损益/预算的数据", notes = "根据天剑修改退回损益/预算数据", response = ResultUtils.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "表id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 只能为4", required = true, dataType = "integer", paramType = "query") })
    @ResponseBody
    public ResultUtils updateStatus(HttpServletRequest request, String id, Integer status) {
        // 需要参数，前端传来的HTML，业务表的id，状态（1保存 还是 2提交 4退回 ） 0 待提交 1待修改 2已提交 3新增 4 退回修改
        // Map<String, Object> dataMap = new HashMap<String, Object>();
        ResultUtils result = new ResultUtils();
        try {
            BusinessData business = businessDataService.selectBusinessDataById(id);//查询id的数据
            List<Organization>  listOrganization=organizationService.listOrganizationBy("", "", "",business.getTypeId(), "", "", "", null, null);
            Integer isStatus=listOrganization.get(0).getStatus();//判断组织架构是否被停用  0表示停用已经被删除
            if(isStatus==0){
                ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                result.setResultDesc("当前组织架构数据已被停用！不能进行编辑！");    
            }else{
                if (id!=null&&!id.equals("")&&status == 4) {
                    // 如果是退回状态的话 1,2个表里面都增加一条一模一样的数据 2,旧数据的删除状态为0 已经删除不能显示 在新增时候修改新数据的status状态为1 待修改
                    // 版本号状态加1
                    String businessId=UuidUtil.getUUID();
                    BusinessData selectBusinessDataById = businessDataService.selectBusinessDataById(id);// 查询对应id的数据
                    BusinessData businessData = new BusinessData();
                    businessData.setId(businessId); // id自动生成
                    businessData.setoId(selectBusinessDataById.getoId());
                    businessData.setTypeId(selectBusinessDataById.getTypeId());
                    businessData.setuId(selectBusinessDataById.getuId());
                    businessData.setYear(selectBusinessDataById.getYear());
                    businessData.setMonth(selectBusinessDataById.getMonth());
                    businessData.setStatus(1);
                    businessData.setDelStatus(selectBusinessDataById.getDelStatus());
                    businessData.setsId(selectBusinessDataById.getsId());
                    businessData.setDataModuleId(selectBusinessDataById.getDataModuleId());
                    businessData.setVersion(selectBusinessDataById.getVersion() + 1);
                    Integer insertBusinessData = businessDataService.insertBusinessData(businessData); // 新增一条一模一样的新数据
                    if (insertBusinessData == 1) {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                        result.setResultDesc("修改成功");
                    } else {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                        result.setResultDesc("修改失败");
                    }
                    Integer deleteBusinessData = businessDataService.deleteBusinessData(id);// 旧数据的删除状态为0
                    if (deleteBusinessData == 1) {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                        result.setResultDesc("修改成功");
                    } else {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                        result.setResultDesc("修改失败");
                    }
                    BusinessDataInfo selectBusiness = businessDataInfoService.selectBusinessDataById(id);
                    BusinessDataInfo businessDataInfo = new BusinessDataInfo();
                    businessDataInfo.setId(UuidUtil.getUUID()); // id自动生成
                    businessDataInfo.setBusinessDataId(businessId);
                    businessDataInfo.setInfo(selectBusiness.getInfo());
                    businessDataInfo.setuId(selectBusiness.getuId());
                    Integer insertBusinessDataInfo = businessDataInfoService.insertBusinessDataInfo(businessDataInfo);
                    if (insertBusinessDataInfo == 1) {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                        result.setResultDesc("修改成功");
                    } else {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                        result.setResultDesc("修改失败");
                    }
                    Integer deleteBusinessDataInfo = businessDataInfoService.deleteBusinessDataInfo(selectBusiness.getId());
                    if (deleteBusinessDataInfo == 1) {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                        result.setResultDesc("修改成功");
                    } else {
                        ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR, result);
                        result.setResultDesc("修改失败");
                    }
                } else {
                    ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
                    result.setResultDesc("该状态不是4退回状态");
                }   
            }
        } catch (Exception e) {
            ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据业务表id导出excel
     * 
     * @param request
     * @param response
     */
    @ResponseBody
    @RequiresPermissions("businessData:download")
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    @ApiOperation(value = "导出Excel", notes = "根据业务表id导出excel", response = ResultUtils.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businId", value = "业务表id", dataType = "string", paramType = "query", required = true) })
    @ApiResponses({ @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败"),
            @ApiResponse(code = 500, message = "系统错误"), @ApiResponse(code = 251, message = "业务表id为空") })
    public ResultUtils exportExcel(HttpServletRequest request, HttpServletResponse response, String businId)
            throws IOException {
        ResultUtils result = new ResultUtils();
        if (("").equals(businId) || businId == null) {
            ElementXMLUtils.returnValue(ElementConfig.BUSINESSDATA_ID_NULL, result);
        } else {
            try {
                BusinessData businessData = businessDataService.selectBusinessDataById(businId);
                if (businessData == null) {
                    ElementXMLUtils.returnValue(ElementConfig.BUSINESSDATA_ID_FAIL, result);
                } else {
                  //获取公司名字
                    Organization companyName = organizationService.getCompanyNameBySon(businessData.getoId());// 查询部门所属的公司名
                    String company=companyName.getOrgName();
                    //获取业务方式
                    List<Organization>  listOrganization=organizationService.listOrganizationBy("", "", "",businessData.getTypeId(), "", "", "", null, null);
                    String orgName=listOrganization.get(0).getOrgName();
                    DataModule dm = dataModuleService.getDataModule(businessData.getDataModuleId());
                    BusinessDataInfo busInfo = businessDataInfoService.selectBusinessDataById(businId);
                    JSONObject joTemp = JSONObject.parseObject(dm.getModuleData());
                    JSONObject joInfo = JSONObject.parseObject(busInfo.getInfo());
                    // HtmlGenerate htmlGenerate=new HtmlGenerate();
                    JSONObject mergeJson = JsonConvertProcess.mergeJson(joTemp, joInfo);
                    JsonConvertExcel jsonConvertExcel=new JsonConvertExcel();
                    Workbook wb = jsonConvertExcel.getExcel(mergeJson, dm.getModuleName());
                    /* response.setContentType("application/vnd.ms-excel;charset=utf-8"); */
                    response.setContentType("application/x-download");
                    response.setCharacterEncoding("utf-8");
                    // 对文件名进行处理。防止文件名乱码
                    String fileName =company+orgName+dm.getModuleName() + ".xlsx";
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                    System.out.println(fileName + "~~~");
                    // 对文件名进行处理。防止文件名乱码
                    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
                    OutputStream os = response.getOutputStream();
                    wb.write(os);
                    os.flush();
                    os.close();
                    ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
                }
              
            } catch (Exception e) {
                ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE, result);
            }
        }
        return result;
    }
    
    /**
     * 查询年份输入框
     * @param request
     * @return
     */
    @RequiresPermissions("businessData:view")
    @RequestMapping(value = "/businessDataYear", method = RequestMethod.POST)
    @ApiOperation(value = "查询损益/预算的年份输入框数据", notes = "年份输入框降序排列", response = BusinessDataYearResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sId", value = "表sId(1损益 2预算)", required = true, dataType = "Integer", paramType = "query") })
    @ResponseBody
    public BusinessDataYearResult updateStatus(HttpServletRequest request, Integer sId) {
        BusinessDataYearResult result= new BusinessDataYearResult();
        if(sId==1||sId==2){
            Map<Object, Object> map = new HashMap<>();
            map.put("sId", sId);
            List<BusinessData> list=businessDataService.businessDataYear(map);
            List<Integer> listYear =new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
               listYear.add(list.get(i).getYear());
            }
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
            result.setYear(listYear);
         }else{
          ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR,result);
          result.setResultDesc("sId只能为1，或者为2");   
         }
        return result;
       }
    
        /**
         * 判断权限数据
         * @param list
         * @return
         */
        private Boolean isImport(List<JSONObject> list) {
            boolean isImport = true;//是否可编辑
            for (int i = 0; i < list.size(); i++) {
                JSONObject obu = (JSONObject) list.get(i);
                Integer num=Integer.parseInt(obu.get("orgType").toString());
                String emm=obu.getString("name").toString();
                if(!emm.contains(BusinessData.NAME)){
                    if(num==4||num==1){
                        isImport =false;
                        break;
                      }   
               }
            }
          return isImport;
        }

}
