package cn.financial.service;


import cn.financial.model.LogManagement;
import cn.financial.model.response.LogManagerInfo;

public interface LogManagementService {


	/**
     * 查询日志列表
     * @return
     */
	LogManagerInfo listLogManagement(String userName,String code,Integer page,Integer pageSize);

    
    /**
     * 新增数据模块
     * @param dataModule
     * @return
     */
    Integer insertLogManagement(LogManagement logManagement);
}
