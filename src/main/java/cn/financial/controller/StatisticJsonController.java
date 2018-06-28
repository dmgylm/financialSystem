package cn.financial.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.financial.service.StatisticJsonService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;

/**
 * 统计相关操作
 * 
 * @author gh 2018/5/7
 *
 */
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
    @RequestMapping(value = "/staticJson")
    public Map<String, Object> staticJson(String reportType,String businessType,String startDate,String endDate,List<String> orgId) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            JSONObject ja = statisticService.jsonCalculation(reportType,businessType,startDate,endDate,orgId);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("data", ja);
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
	
    
}
