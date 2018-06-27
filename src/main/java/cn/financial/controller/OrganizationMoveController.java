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

import cn.financial.model.Organization;
import cn.financial.model.OrganizationMove;
import cn.financial.model.User;
import cn.financial.service.OrganizationMoveService;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.UuidUtil;

@Controller
@RequestMapping("/organizationMove")
public class OrganizationMoveController {
	
	@Autowired
	private OrganizationMoveService organizationMove;
	@Autowired
	private OrganizationServiceImpl serviceImpl;
	
	protected Logger logger = LoggerFactory.getLogger(OrganizationMoveController.class);
	private List<String> idList;
	private List<String> nidList;
	
	/**
	 * 新增
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions({ "organization:update", "organization:create" })
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, Object> saveOrganizationMove(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		Integer s = 0;
		String id = null;
        String parentOrgId = null;
		
		try {
			if (null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
                id = request.getParameter("id");
            }
            if (null != request.getParameter("parentId") && !"".equals(request.getParameter("parentId"))) {
                parentOrgId = request.getParameter("parentId");
            }
            User user = (User) request.getAttribute("user");
            String name = user.getName();
            String uid = user.getId();
            
            Map<Object, Object> map = new HashMap<>();
    		map.put("id", id);
    		List<Organization> list = serviceImpl.listOrganizationBy(map);
    		String orgkey = list.get(0).getOrgkey();//获得移动节点的orgkey
    		
    		//查询被移动节点的子节点的id
    		List<Organization> listSon = serviceImpl.listTreeByIdForSon(id);
    		idList = null;
    		if(listSon.get(0).getId() != id && !id.equals(listSon.get(0).getId())) {
    			for (int i=0; i<listSon.size(); i++) {
    				 idList.add(listSon.get(i).getId()); 
    			}
    		}
    		
    		serviceImpl.moveOrganization(uid, id, parentOrgId);
    		
    		Map<Object, Object> map1 = new HashMap<>();
    		map1.put("orgkey", orgkey);
    		List<Organization> list1 = serviceImpl.listOrganizationBy(map1);
    		String nid = list1.get(0).getId();//移动后的节点id
            
			OrganizationMove om = new OrganizationMove();
			String uuid = UuidUtil.getUUID();
			om.setId(uuid);
			om.setModifier(name);
			om.setHis_Id(id);
			om.setNew_Id(nid);
			s = organizationMove.saveOrganizationMove(om);
			//根据移动后的id查询其下子节点
			List<Organization> nlistSon = serviceImpl.listTreeByIdForSon(nid);
    		nidList = null;
    		if(nlistSon.get(0).getId() != id && !id.equals(nlistSon.get(0).getId())) {
    			for (int j=0; j<listSon.size(); j++) {
    				 nidList.add(nlistSon.get(j).getId()); 
    			}
    		}
			
			//增加子节点移动信息
    		for (int k=0; k<idList.size();k++) {
            	
                OrganizationMove som = new OrganizationMove();
                String suuid = UuidUtil.getUUID();
                som.setId(suuid);
                som.setHis_Id(idList.get(k));
                som.setNew_Id(nidList.get(k));
                som.setModifier(name);
                organizationMove.saveOrganizationMove(som);
			}
    		
			if (Integer.valueOf(1).equals(s)) {
			    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
			} else {
			    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
			}
		} catch (Exception e) {
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
		}
		
		return dataMap;
	}
	
	/**
	 * 查询
	 * @param request
	 * @param response
	 * @return
	 */
	/*public Map<String, Object> listOrganizationMoveBy(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		Map<Object, Object> map = new HashMap<>();
		try {
			
			
			List<OrganizationMove> list = organizationMove.listOrganizationMoveBy(map);
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("resultData", list);
		} catch (Exception e) {
			dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            this.logger.error(e.getMessage(), e);
		}
		
		return dataMap;
	}*/

}
