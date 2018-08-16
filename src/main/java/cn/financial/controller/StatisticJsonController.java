package cn.financial.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.BusinessData;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.response.OganizationNode;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.StaticInfo;
import cn.financial.model.response.StaticJson;
import cn.financial.service.DataModuleService;
import cn.financial.service.MessageService;
import cn.financial.service.OrganizationService;
import cn.financial.service.RedisCacheService;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertExcel;
import cn.financial.util.SiteConst;
import cn.financial.util.StringUtils;
import cn.financial.util.UuidUtil;

/**
 * 统计相关操作
 * 
 * @author gh 2018/5/7
 *
 */
@Api(tags = "汇总中心")
@Controller
@RequestMapping("/statistic")
public class StatisticJsonController {
	
    @Autowired
    private StatisticJsonService statisticService;

    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private RedisCacheService redisCacheService;
    
    @Autowired
    private DataModuleService dataModuleService;
    
    @Autowired
    private MessageService messageservice;

    protected Logger logger = LoggerFactory.getLogger(StatisticJsonController.class);
    
    /**
     * 统计数据相关
     * 
     * @param request
     * @param response
     * @return 返回结果为总的json数据
     */
    @ResponseBody
    @RequiresPermissions(value={"collect:view"},logical=Logical.OR)
	@ApiOperation(value = "根据组织汇总数据",notes = "根据组织汇总数据",response = StaticJson.class)
    @ApiImplicitParams({
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "reportType", value = "报表类型(如:BUDGET:预算;PROFIT_LOSS:损益;TAX:税金;ASSESSMENT:考核;SUMMARY_BUDGET:预算简易汇总;SUMMARY_PROFIT_LOSS:损益简易汇总;,英文)", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "businessType", value = "业务板块(如:b1503ff8da124fa3bce0bf07f16f56f6 具体数据从组织架构orgkey取得,接口是listBy)", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "startDate", value = "开始时间(年月用/分隔,string类型)", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "endDate", value = "结束时间(年月用/分隔,string类型)", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgId", value = "选中组织架构id集合(jsonarray形式,比如['0303e625211c47ee86efb8007053186b'] 所取数据从前台选择的组织架构id传来)", required = true) 
    	})
//	@ApiResponses({@ApiResponse(code = 200, message = "成功"),
//		@ApiResponse(code = 400, message = "失败"),
//	    @ApiResponse(code = 500, message = "系统错误"),
//	    @ApiResponse(code = 280, message = "报表类型不能为空"),
//	    @ApiResponse(code = 281, message = "业务板块不能为空"),
//	    @ApiResponse(code = 282, message = "开始时间不能为空"),
//	    @ApiResponse(code = 283, message = "结束时间不能为空"),
//	    @ApiResponse(code = 284, message = "选中组织架构id集合不能为空")})
    @PostMapping(value = "/staticJson")
    public StaticJson staticJson(HttpServletRequest request, HttpServletResponse response,String reportType,String businessType,String startDate,String endDate,String orgId) {
//        Map<String, Object> dataMap = new HashMap<String, Object>();
    	StaticJson sj = new StaticJson();
        try {
			User user = (User) request.getAttribute("user");
        	if(!StringUtils.isValid(reportType)){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_REPORTTYPE_NULL, sj);
                return sj;
            }
			if (!DataModule.REPORT_TYPE_BUDGET_SUMMARY.equals(reportType)
					&& !DataModule.REPORT_TYPE_PROFIT_LOSS_SUMMARY.equals(reportType)
					&& !StringUtils.isValid(businessType)) {
                ElementXMLUtils.returnValue(ElementConfig.STATIC_BUSINESSTYPE_NULL, sj);
                return sj;
            }
        	if(startDate == null || startDate.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_STARTDATE_NULL, sj);
                return sj;
            }
        	if(endDate == null || endDate.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_ENDDATE_NULL, sj);
                return sj;
            }
        	if(orgId == null || orgId.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_ORGID_NULL, sj);
                return sj;
            }
        	
        	String caCheUuid = UuidUtil.getUUID();
     		//获取所选机构
     		List<Organization> codeSonList = statisticService.companyList(JSONArray.parseArray(orgId));
    		//获取最底层数据
    		List<String> typeIdList = statisticService.typeIdList(codeSonList);
    		//获取对应公司列表
     		Map<String,List<String>> companyList =  statisticService.companyCacheList(codeSonList);
     		if(companyList==null){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_ORGLIST_FAIL, sj);
                return sj;
     		}
    		//获取底层对应数据的集合
    		List<BusinessData> businessDataList = statisticService.valueList(startDate,endDate,typeIdList);
        	//获取传递到页面的数据集合
        	JSONObject ja = statisticService.jsonCalculation(reportType,businessType,businessDataList);
        	if(ja==null){
            	ElementXMLUtils.returnValue(ElementConfig.STATIC_REPORTTYPE_FAIL,sj);
            	return sj;
        	}
        	//存入缓存
    		statisticService.staticInfoMap(companyList,businessDataList,caCheUuid);
    		JSONObject excelCacheJson = new JSONObject();
    		excelCacheJson.put("json", ja);
    		excelCacheJson.put("reportType", reportType);
    		excelCacheJson.put("businessType", businessType);
    		redisCacheService.put("staticInfoMap", caCheUuid, excelCacheJson);
			HtmlGenerate hg = new HtmlGenerate(true);
			String html = hg.generateHtml(ja,HtmlGenerate.HTML_TYPE_PREVIEW);
			sj.setCaCheId(caCheUuid);
			sj.setData(html);
			
            String fileName = "汇总数据表单.xlsx";
            String saveName = SiteConst.FILEURL + "\\" + fileName;
			Workbook wb = JsonConvertExcel.getExcel(ja,fileName);
			FileOutputStream fos = new FileOutputStream(saveName);
			wb.write(fos);
			fos.close();
			
			String sessionId = request.getSession().getId();
			messageservice.saveMessageByUser(user, saveName, sessionId);
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,sj);
//            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
//            dataMap.put("caCheId", caCheUuid);
//            dataMap.put("data", html);
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,sj);
//            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return sj;
    }
    
    @ResponseBody
    @RequiresPermissions(value={"collect:view"},logical=Logical.OR)
    @ApiOperation(value = "查询业务板块信息",notes = "查询业务板块信息",response=OganizationNode.class)
    @PostMapping(value="listBusinessSector")
    public OganizationNode listBusinessSector() {
        OganizationNode organiza=new OganizationNode();
        try {
            List<Organization> list = organizationService.
            	listOrganizationBy(null,null,null,null,null,null,null,4,null);
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
     * 查看数据详情相关
     * 
     * @param request
     * @param response
     * @return 返回结果为总的json数据
     */
    @ResponseBody
    @RequiresPermissions(value={"collect:view"},logical=Logical.OR)
	@ApiOperation(value = "根据key查询详情",notes = "根据key查询详情",response = StaticInfo.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "caCheUuid", value = "缓存id(如:9ee9313edc334232b7a64a60f27ce5dd 对应缓存id,从staticJson接口获取caCheUuid值)", required = true), 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "infoKey", value = "查询详情key(如:guanlifeiyong_zhufanggongjijinbenyueshiji 对应需要查看具体字段的公司详情,从staticJson接口的html里取key)", required = true) 
    	})
//	@ApiResponses({@ApiResponse(code = 200, message = "成功"),
//		@ApiResponse(code = 400, message = "失败"),
//	    @ApiResponse(code = 500, message = "系统错误"),
//	    @ApiResponse(code = 285, message = "缓存id不能为空"),
//	    @ApiResponse(code = 286, message = "查询详情key不能为空")})
    @PostMapping(value = "/staticInfo")
    public StaticInfo staticInfo(String caCheUuid,String infoKey) {
//        Map<String, Object> dataMap = new HashMap<String, Object>();
    	StaticInfo si = new StaticInfo();
        try {
        	if(caCheUuid == null || caCheUuid.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_CACHEUUID_NULL, si);
                return si;
            }
        	if(infoKey == null || infoKey.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_INFOKEY_NULL, si);
                return si;
            }
        	
            Map<String, Map<String, String>> ja = statisticService.staticInfoMap(null,null,caCheUuid);
            Map<String, String> companyInfo = ja.get(infoKey);
            si.setData(companyInfo);
            ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,si);
//            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
//            dataMap.put("data", companyInfo);
        } catch (Exception e) {
        	ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,si);
//            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return si;
    }
    
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "导出Excel",notes = "导出Excel",response = ResultUtils.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "caCheUuid", value = "缓存id(如:9ee9313edc334232b7a64a60f27ce5dd 对应缓存id,从staticJson接口获取caCheUuid值)", required = true)
    	})
    public ResultUtils exportExcel(HttpServletResponse response,String caCheUuid){
    	ResultUtils result = new ResultUtils();
    	try {
    		if(!StringUtils.isValid(caCheUuid)) {
    			 return ElementXMLUtils.returnValue(ElementConfig.STATIC_CACHEUUID_NULL, result);
    		}
    		JSONObject json = (JSONObject) redisCacheService.get("staticInfoMap", caCheUuid);
    		String reportType = json.getString("reportType");
    		String businessType = json.getString("businessType");
    		String dataModuleName = dataModuleService.getDataModuleName(reportType, businessType);
			Workbook excel = JsonConvertExcel.getExcel(json.getJSONObject("json"), dataModuleName);
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			// 对文件名进行处理。防止文件名乱码
			String fileName =  dataModuleName+".xlsx";
			fileName = URLEncoder.encode(fileName, "UTF-8");
			// Content-disposition属性设置成以附件方式进行下载
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			OutputStream os = response.getOutputStream();
			excel.write(os);
			os.flush();
			os.close();
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY, result);
		} catch (IOException e) {
			logger.error("导出汇总数据失败:",e);
		}
    	return result;
    }
}
