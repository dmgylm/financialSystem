package cn.financial.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

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

import cn.financial.model.BusinessData;
import cn.financial.model.Organization;
import cn.financial.model.response.StaticInfo;
import cn.financial.model.response.StaticJson;
import cn.financial.service.StatisticJsonService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.UuidUtil;

/**
 * 统计相关操作
 * 
 * @author gh 2018/5/7
 *
 */
@Api(tags = "统计相关操作")
@Controller
@RequestMapping("/statistic")
public class StatisticJsonController {
	
    @Autowired
    private StatisticJsonService statisticService;

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
	@ApiOperation(value = "根据组织统计数据",notes = "根据组织统计数据",response = StaticJson.class)
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "reportType", value = "报表类型(如:BUDGET代表预算,PROFIT_LOSS代表损益,英文)", required = true),
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
    public StaticJson staticJson(String reportType,String businessType,String startDate,String endDate,String orgId) {
//        Map<String, Object> dataMap = new HashMap<String, Object>();
    	StaticJson sj = new StaticJson();
        try {
        	
        	if(reportType == null || reportType.equals("")){
                ElementXMLUtils.returnValue(ElementConfig.STATIC_REPORTTYPE_NULL, sj);
                return sj;
            }
        	if(businessType == null || businessType.equals("")){
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
    		//获取底层对应数据的集合
    		List<BusinessData> businessDataList = statisticService.valueList(startDate,endDate,typeIdList);
        	//获取传递到页面的数据集合
        	JSONObject ja = statisticService.jsonCalculation(reportType,businessType,businessDataList);
        	//存入缓存
    		statisticService.staticInfoMap(companyList,businessDataList,caCheUuid);
			HtmlGenerate hg = new HtmlGenerate();
			String html = hg.generateHtml(ja.toString(),HtmlGenerate.HTML_TYPE_PREVIEW);
			sj.setCaCheId(caCheUuid);
			sj.setData(html);
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
}
