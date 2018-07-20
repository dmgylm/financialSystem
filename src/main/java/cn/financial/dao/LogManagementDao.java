package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.LogManagement;

public interface LogManagementDao {
	
	/**
     * 查询日志列表
     * @return
     */
    List<LogManagement> listLogManagement(Map<Object, Object> map);

    
    /**
     * 新增数据模块
     * @param dataModule
     * @return
     */
    Integer insertLogManagement(LogManagement logManagement);
}
