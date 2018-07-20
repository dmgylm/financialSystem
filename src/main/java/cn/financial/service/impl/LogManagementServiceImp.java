package cn.financial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.financial.dao.LogManagementDao;
import cn.financial.model.DataModule;
import cn.financial.model.LogManagement;
import cn.financial.model.response.LogManagerInfo;
import cn.financial.service.LogManagementService;

@Service("LogManagementServiceImp")
public class LogManagementServiceImp implements LogManagementService{

	@Autowired
	private LogManagementDao logManagementDao;
	
	/**
     * 查询日志列表
     * @return
     */
    public LogManagerInfo listLogManagement(String userName,String logCode,Integer page,Integer pageSize) {
    	LogManagerInfo logManagerInfo=new LogManagerInfo();
    	Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("userName", userName);
		map.put("logCode", logCode);
		
		page = page == null?1:page;
	    pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(page, pageSize);
		
		List<LogManagement> logManagements=logManagementDao.listLogManagement(map);
		PageInfo<LogManagement> pageInfo = new PageInfo<LogManagement>(logManagements);
		
		logManagerInfo.setLogManagements(logManagements);
		logManagerInfo.setTotal(pageInfo.getTotal());
		return logManagerInfo;
	}

    
    /**
     * 新增日志
     * @param dataModule
     * @return
     */
    public Integer insertLogManagement(LogManagement logManagement) {
		return logManagementDao.insertLogManagement(logManagement);
	}
}
