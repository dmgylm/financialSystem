package cn.financial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.DataModule;
import cn.financial.service.DataModuleService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/dataModule")
public class DataModuleController {
	
	protected Logger logger = LoggerFactory.getLogger(DataModuleController.class);

	@Autowired
	private DataModuleService dataModuleService;
	
	@RequestMapping(value = "/dataModuleList", method = RequestMethod.POST)
    @ResponseBody
	public Map<String,Object> listDataModule(String jsonData){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			JSONObject json=JSONObject.fromObject(jsonData);
			Map<Object, Object> map = new HashMap<>();
			String moduleName = json.getString("moduleName");
			if(null!= moduleName && !"".equals(moduleName)){
               // map.put("moduleName",  new String(json.getString("moduleName").getBytes("ISO-8859-1"), "UTF-8"));//用户名
				map.put("moduleName",moduleName);
            }
			List<DataModule> list=dataModuleService.listDataModule(map);
			dataMap.put("dataModuleList", list);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "成功");
		} catch (Exception e) {
			logger.error("",e);
			dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "系统错误");
		}
		
		return dataMap;
	}
	
	/**
	 * 根据ID查询模板
	 * @param dataModuleId
	 * @return
	 */
	@RequestMapping(value = "/getDataModule")
    @ResponseBody
	public Map<String,Object> getDataModule(String dataModuleId){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			DataModule bean = dataModuleService.getDataModule(dataModuleId);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			dataMap.put("dataModule", bean);
		} catch (Exception e) {
			logger.error("查询错误",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return dataMap;
	}
	
	/**
	 * 根据报表类型及业务板块查询最新版本模板
	 * @param reportType
	 * @param businessType
	 * @return
	 */
	@RequestMapping(value = "/getNewestDataModule")
	@ResponseBody
	public Map<String,Object> getNewestDataModule(String reportType,String businessType){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			DataModule bean = dataModuleService.getDataModule(reportType,businessType);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			dataMap.put("dataModule", bean);
		} catch (Exception e) {
			logger.error("查询错误",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return dataMap;
	}
	
	public Map<String,Object> createDataModule(){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "成功");
		} catch (Exception e) {
			dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "系统错误");
		}
		return dataMap;
	}
}
