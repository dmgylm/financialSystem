package cn.financial.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.BusinessDataBuild;
import cn.financial.model.response.ListBusinessDataBuild;
import cn.financial.service.impl.BusinessDataBuildServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "损益生成状态记录接口")
@Controller
@RequestMapping("/businessDataBuild")
public class BusinessDataBuildController {
	
	@Autowired
	private BusinessDataBuildServiceImpl bImpl;
	
	@ResponseBody
	@RequestMapping(value = "/listBDB", method = RequestMethod.POST)
	@ApiOperation(value="查询生成记录",notes="根据id查询生成记录",response = ListBusinessDataBuild.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name="status",value="生成状态(0:未生成,1:已生成)",dataType="int",paramType="query")})
	public ListBusinessDataBuild listBusinessDataBuild(Integer status) {
		
		ListBusinessDataBuild result = new ListBusinessDataBuild();
		
		try {
			
			List<BusinessDataBuild> list = new ArrayList<BusinessDataBuild>();
			list = bImpl.listBusinessDataBuild(status);
			
			result.setData(list);
            ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
		}catch (Exception e) {
			ElementXMLUtils.returnValue((ElementConfig.RUN_FAILURE),result);
		}
		
		return result;
	}
	
}
