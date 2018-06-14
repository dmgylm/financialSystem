package cn.financial.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.Organization;
import cn.financial.service.OrganizationService;


@Controller
/**
 * @author hsl
 */
public class ArchitectureController {
	@Autowired
	private OrganizationService service;
	/**
	 *   组织架构节点
	 * @param request
	 * @param response
	 * @return   
	 */
	@RequestMapping(value="organizationode")
	@ResponseBody
	public JSONObject organizatioNode(HttpServletRequest request,HttpServletResponse response){
		 String id=request.getParameter("id");
		 String[] ids = id.split(",");//分割逗号
		 List<String> listid=Arrays.asList(); 
		 listid.addAll(Arrays.asList(ids));
		 List<Organization> list = service.listOrganization(listid);
		 List<String> listmap=new ArrayList<String>();
    	  for (Organization organization : list) {
    		   String his_permission = organization.getHis_permission();
    		   String[] hps = his_permission.split(",");//分割逗号
               listmap.addAll(Arrays.asList(hps));//所有的his_permission存到listmap当中
             } 
     	 JSONObject obj=new JSONObject();
     	  //查询对应的节点的数据
    	 List<Organization> listshow = service.listOrganizationcode(listmap);
    	 JSONArray json=JSONArray.fromObject(listshow);   
    	  try {
    		  obj.put("rows",json);
       	      obj.put("resultCode",200);
		    } catch (Exception e) {
              obj.put("resultCode",500);
	     	  obj.put("resultDesc","服务器异常");
		    }
    	    return obj;
	}
   
}