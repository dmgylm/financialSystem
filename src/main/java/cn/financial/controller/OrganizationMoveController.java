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

import cn.financial.model.OrganizationMove;
import cn.financial.service.OrganizationMoveService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;

@Controller
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
	@RequestMapping(value = "/listBy", method = RequestMethod.POST)
	public Map<String, Object> listOrganizationMoveBy(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			Map<Object, Object> map = new HashMap<>();
            if (null != request.getParameter("orgkey") && !"".equals(request.getParameter("orgkey"))) {
                map.put("orgkey", new String(request.getParameter("orgkey").getBytes("ISO-8859-1"), "UTF-8"));
            }
			List<OrganizationMove> list = service.listOrganizationMoveBy(map);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("data", list);
		} catch (Exception e) {
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            this.logger.error(e.getMessage(), e);
		}
		
		return dataMap;
	}

}
