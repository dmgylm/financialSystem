package cn.financial.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.NetWork;
import cn.financial.model.Orderate;
import cn.financial.service.impl.MongoDBServiceImpl;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("/mongoDB")
public class MongoDBController {
	
	private Logger logger = Logger.getLogger(MongoDBController.class);

	
	@Autowired
	private MongoDBServiceImpl mongoDBService;
	
	
	
	@RequestMapping(value="queryDatas")
	@ResponseBody
	public Map<String, Object> queryNetWorks(String json){
		Map<String, Object> dataMap = null;
		try {
			dataMap=new HashMap<String, Object>();
			/*List<Orderate> Orderates=mongoDBService.findList();
			Orderate oo=mongoDBService.findById("0");
			dataMap.put("Orderate", Orderates);*/
			/*Orderate orderate=new Orderate();
			orderate.setSum("23333");
			mongoDBService.insertOrderate(orderate);*/
			JSONArray jsons=JSONArray.fromObject(json);
			NetWork netWork=new NetWork();
			netWork.setValue(jsons);
			mongoDBService.insertTests(netWork);
			
			dataMap.put("resultCode", "200");
			dataMap.put("resultDesc", "成功！！");
		} catch (Exception e) {
			dataMap=new HashMap<String, Object>();
			dataMap.put("resultCode", "500");
			dataMap.put("resultDesc", "服务器异常！！");
			logger.error("AppAnalysis error:", e);
		}
//		System.out.println(JSONArray.fromObject(dataMap).toString());
		return dataMap;
	}
}
