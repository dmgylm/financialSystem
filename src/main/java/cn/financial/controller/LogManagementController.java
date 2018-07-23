package cn.financial.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.LogManagement;
import cn.financial.model.response.DataModulesResult;
import cn.financial.model.response.LogManagerInfo;
import cn.financial.model.response.LogManagerResult;
import cn.financial.service.LogManagementService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户接口调用日志记录")
@Controller
@RequestMapping("/logManagement")
public class LogManagementController {

	protected Logger logger = LoggerFactory.getLogger(LogManagementController.class);
	
	@Autowired
	private LogManagementService logManagementService;
	
	@PostMapping("/queryLogManagers")
	@ApiOperation(value = "查询日志列表",notes = "查询模板列表",response=LogManagerResult.class)
	@ApiImplicitParams({
		  @ApiImplicitParam(name="userName",value="用户名称",dataType="string", paramType = "query",required=false),
		  @ApiImplicitParam(name="logCode",value="日志返回值编码记录",dataType="int", paramType = "query",required=false),
		  @ApiImplicitParam(name="page",value="当前页码",dataType="int", paramType = "query",required=false),
		  @ApiImplicitParam(name="pageSize",value="每页数据长度",dataType="int", paramType = "query",required=false)
		  })
    @ResponseBody
	public LogManagerResult queryLogManagers(String userName,String logCode,Integer page,Integer pageSize){
		LogManagerResult logManagerResult=new LogManagerResult();
		try {
			
			LogManagerInfo lonManagement=logManagementService.listLogManagement(userName, logCode, page, pageSize);
			logManagerResult.setData(lonManagement);
			ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY,logManagerResult);
		} catch (Exception e) {
			logger.error("日志列表查询错误",e);
			ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE,logManagerResult);
		}
		return logManagerResult;
	}
}
