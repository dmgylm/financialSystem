package cn.financial.controller;

import java.util.ArrayList;
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
    @RequiresPermissions("statistic:view")
    @RequestMapping(value = "/staticjson", method = RequestMethod.POST)
    public Map<String, Object> staticjson(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<JSONObject> valuemode = new ArrayList<JSONObject>();
        try {
            JSONObject ja = statisticService.jsonCalculation(JSONObject.parseObject(request.getParameter("jsondata")),valuemode);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "统计成功!");
            dataMap.put("resultData", ja);
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
	
    
}
