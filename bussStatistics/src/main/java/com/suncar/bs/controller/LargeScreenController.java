package com.suncar.bs.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suncar.bs.services.DistributionService;
import com.suncar.common.util.FileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/largescreen")
public class LargeScreenController {
	
	@Autowired
	private DistributionService distributionService;
	
	@RequestMapping("largesc")
	@ResponseBody
	public Map<String, Object> largesc(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		String path = this.getClass().getClassLoader().getResource("data/voice.json").getPath();
		File file = new File(path);
		String readFile = FileUtil.readFile(file);
		JSONArray jsonArray = new JSONArray();
		jsonArray = JSONArray.fromObject(readFile);
		Integer id=Integer.parseInt(request.getParameter("id"));
		for (int i=0;i<jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer num=jsonObject.getInt("id");
			if(id==num) {
				String  objs=jsonArray.getJSONObject(i).getJSONArray("resourceJsonArray").toString();
				try {
					distributionService.shuju(objs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return dataMap;
	}
		
}
