package cn.financial.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import java.util.HashMap;
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
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
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
	@ApiOperation(value = "根据组织统计数据",notes = "根据组织统计数据")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "reportType", value = "报表类型", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "businessType", value = "业务板块", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "startDate", value = "开始时间", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "endDate", value = "结束时间", required = true),
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "orgId", value = "选中组织架构id集合(jsonarray形式)", required = true) 
    	})
	@ApiResponse(code = 200, message = "成功")
    @PostMapping(value = "/staticJson")
    public Map<String, Object> staticJson(String reportType,String businessType,String startDate,String endDate,String orgId) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
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
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("caCheId", caCheUuid);
            dataMap.put("data", html);
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
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
	@ApiOperation(value = "根据key查询详情",notes = "根据key查询详情")
    @ApiImplicitParams({ 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "caCheUuid", value = "缓存id", required = true), 
    	@ApiImplicitParam(paramType="query", dataType = "String", name = "infoKey", value = "查询详情key", required = true) 
    	})
	@ApiResponse(code = 200, message = "成功")
    @PostMapping(value = "/staticInfo")
    public Map<String, Object> staticInfo(String caCheUuid,String infoKey) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Map<String, Map<String, String>> ja = statisticService.staticInfoMap(null,null,caCheUuid);
            Map<String, String> companyInfo = ja.get(infoKey);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("data", companyInfo);
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
