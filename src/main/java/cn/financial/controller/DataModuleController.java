package cn.financial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
	
	/**
	 * 查询模板列表
	 * @param moduleName 模板名称
	 * @return
	 */
	@RequiresPermissions(value={"data:view","data:search"},logical=Logical.OR)
	@RequestMapping(value = "/dataModuleList", method = RequestMethod.POST)
    @ResponseBody
	public Map<String,Object> listDataModule(String moduleName){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			//JSONObject json=JSONObject.fromObject(jsonData);
			Map<Object, Object> map = new HashMap<>();
			//String moduleName = json.getString("moduleName");
			if(null!= moduleName && !"".equals(moduleName)){
               // map.put("moduleName",  new String(json.getString("moduleName").getBytes("ISO-8859-1"), "UTF-8"));//用户名
				map.put("moduleName",moduleName);
            }
			List<DataModule> list=dataModuleService.listDataModule(map);
			dataMap.put("data", list);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
		} catch (Exception e) {
			logger.error("",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		
		return dataMap;
	}
	
	/**
	 * 根据ID查询模板
	 * @param dataModuleId
	 * @return
	 */
	@RequiresPermissions("data:search")
	@RequestMapping(value = "/getDataModule")
    @ResponseBody
	public Map<String,Object> getDataModule(String dataModuleId){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			DataModule bean = dataModuleService.getDataModule(dataModuleId);
			bean.setModuleData("");  //清空返回值的json数据
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			dataMap.put("data", bean);
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
	@RequiresPermissions("data:search")
	@RequestMapping(value = "/getNewestDataModule")
	@ResponseBody
	public Map<String,Object> getNewestDataModule(String reportType,String businessType){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			DataModule bean = dataModuleService.getDataModule(reportType,businessType);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			dataMap.put("data", bean);
		} catch (Exception e) {
			logger.error("查询错误",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return dataMap;
	}
	
	/**
	 * 修改或新增模板
	 * @param reportType 报表类型
	 * @param businessType 业务类型
	 * @param html html代码
	 * @param firstRowNum 横向标题前缀(模块)
	 * @param secondRowNum 横向标题后缀(科目)
	 * @param firstColNum 纵向标题前缀
	 * @param secondColNum 纵向标题后缀
	 * @return
	 */
	@RequiresPermissions(value={"data:create","data:update"},logical=Logical.OR)
	@RequestMapping(value = "/editDataModule")
	@ResponseBody
	public Map<String, Object> editDataModule(String reportType,String businessType,String html, Integer firstRowNum,
			Integer secondRowNum, Integer firstColNum, Integer secondColNum) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			dataModuleService.editDataModule(reportType,businessType,html,firstRowNum,secondRowNum,firstColNum,secondColNum);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
		} catch (Exception e) {
			logger.error("模板修改失败",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return dataMap;
	}
}
