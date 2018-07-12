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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.DataModule;
import cn.financial.model.OutResult;
import cn.financial.model.response.DataModulesResult;
import cn.financial.model.response.DataResult;
import cn.financial.model.response.ResultUtils;
import cn.financial.service.DataModuleService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;

@Api(tags = "数据模板配置")
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
	@PostMapping("/dataModuleList")
	@ApiOperation(value = "查询模板列表",notes = "查询模板列表",response=DataModulesResult.class)
	@ApiImplicitParams({
		  @ApiImplicitParam(name="moduleName",value="模板名称",dataType="string", paramType = "query",required=false)})
    @ResponseBody
	public DataModulesResult listDataModule(String moduleName){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		OutResult<List<DataModule>> outResult=new OutResult<List<DataModule>>();
		DataModulesResult result=new DataModulesResult();
		try {
			//JSONObject json=JSONObject.fromObject(jsonData);
			Map<Object, Object> map = new HashMap<>();
			//String moduleName = json.getString("moduleName");
			if(null!= moduleName && !"".equals(moduleName)){
               // map.put("moduleName",  new String(json.getString("moduleName").getBytes("ISO-8859-1"), "UTF-8"));//用户名
				map.put("moduleName",moduleName);
            }
			List<DataModule> list=dataModuleService.listDataModule(map);
			/*dataMap.put("data", list);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));*/
			
			result.setData(list);
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
		} catch (Exception e) {
			logger.error("模板列表查询错误",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		
		return result;
	}
	
	/**
	 * 根据ID查询模板
	 * @param dataModuleId
	 * @return
	 */
	@RequiresPermissions("data:search")
	@PostMapping("getDataModule")
	@ApiOperation(value = "根据ID查询模板",notes = "根据ID查询模板",response = DataResult.class)
	@ApiImplicitParams({
		  @ApiImplicitParam(name="dataModuleId",required=true,value="模板id",dataType="string", paramType = "query")})
    @ResponseBody
	public DataResult getDataModule(String dataModuleId){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DataResult dataResult=new DataResult();
		try {
			DataModule bean = dataModuleService.getDataModule(dataModuleId);
			bean.setModuleData("");  //清空返回值的json数据
			/*dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			dataMap.put("data", bean);*/
			dataResult.setData(bean);
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,dataResult);
		} catch (Exception e) {
			logger.error("根据ID查询模板错误",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return dataResult;
	}
	
	/**
	 * 根据报表类型及业务板块查询最新版本模板
	 * @param reportType
	 * @param businessType
	 * @return
	 */
	@RequiresPermissions("data:search")
	@PostMapping("getNewestDataModule")
	@ApiOperation(value = "根据报表类型及业务板块查询最新版本模板",notes = "根据报表类型及业务板块查询最新版本模板",response = DataResult.class)
	@ApiImplicitParams({
		  @ApiImplicitParam(name="reportType",required=true,value="报表类型",dataType="string", paramType = "query"),
		  @ApiImplicitParam(name="businessType",required=true,value="业务板块",dataType="string", paramType = "query")})
	@ResponseBody
	public DataResult getNewestDataModule(String reportType,String businessType){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DataResult dataResult=new DataResult();
		try {
			DataModule bean = dataModuleService.getDataModule(reportType,businessType);
			/*dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			dataMap.put("data", bean);*/
			dataResult.setData(bean);
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,dataResult);
		} catch (Exception e) {
			logger.error("根据报表类型及业务板块查询最新版本模板错误",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return dataResult;
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
	@PostMapping("editDataModule")
	@ApiOperation(value = "修改或新增模板",notes = "修改或新增模板",response = ResultUtils.class)
	@ApiImplicitParams({
		  @ApiImplicitParam(name="reportType",required=true,value="报表类型",dataType="string", paramType = "query"),
		  @ApiImplicitParam(name="businessType",required=true,value="业务板块",dataType="string", paramType = "query"),
		  @ApiImplicitParam(name="html",required=true,value="模板html",dataType="string", paramType = "query"),
		  @ApiImplicitParam(name="firstRowNum",required=true,value="横向标题前缀(模块)",dataType="Integer", paramType = "query"),
		  @ApiImplicitParam(name="secondRowNum",required=true,value="横向标题后缀(科目)",dataType="Integer", paramType = "query"),
		  @ApiImplicitParam(name="firstColNum",required=true,value="纵向标题前缀",dataType="Integer", paramType = "query"),
		  @ApiImplicitParam(name="secondColNum",required=true,value="纵向标题后缀",dataType="Integer", paramType = "query")})
	@ResponseBody
	public ResultUtils editDataModule(String reportType,String businessType,String html, 
			Integer firstRowNum,Integer secondRowNum,Integer firstColNum, Integer secondColNum) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		ResultUtils result=new ResultUtils();
		try {
			dataModuleService.editDataModule(reportType,businessType,html,firstRowNum,secondRowNum,firstColNum,secondColNum);
			/*dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));*/
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,result);
		} catch (Exception e) {
			logger.error("模板修改失败",e);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
		}
		return result;
	}
}
