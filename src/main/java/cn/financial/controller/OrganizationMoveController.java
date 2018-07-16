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

import cn.financial.model.OrganizationMove;
import cn.financial.model.response.ListOrgMove;
import cn.financial.service.OrganizationMoveService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(tags = "组织架构移动记录接口")
@RequestMapping("/organizationMove")
public class OrganizationMoveController {
	
	@Autowired
	private OrganizationMoveService service;
	
	protected Logger logger = LoggerFactory.getLogger(OrganizationMoveController.class);
	
	/**
	 * 根据orgkey查询
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listBy", method = RequestMethod.POST)
	@ApiOperation(value="查询组织架构移动记录",notes="根据orgkey查询相应的信息",response = ListOrgMove.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="orgkey",value="和模版的对接的唯一值",dataType="string", paramType = "query", required = true)})
	@ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
	    @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 252, message = "组织架构orgkey为空")})
	public ListOrgMove listOrganizationMoveBy(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		ListOrgMove result = new ListOrgMove();
		
		try {
			Map<Object, Object> map = new HashMap<>();
            map.put("orgkey", request.getParameter("orgkey"));
			List<OrganizationMove> list = service.listOrganizationMoveBy(map);
			if(list == null){
                ElementXMLUtils.returnValue(ElementConfig.ORGMOVE_ORGKEY_NULL,result);
                return result;
            }
			result.setData(list);
			ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
		} catch (Exception e) {
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            this.logger.error(e.getMessage(), e);
		}
		
		return result;
	}

}
