package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.DataModule;

public interface DataModuleDao {

	/**
     * 查询所有用户/多条件查询用户列表
     * @return
     */
    List<DataModule> listDataModule(Map<Object, Object> map);
	
    /**
     * 新增数据模块
     * @param dataModule
     * @return
     */
    Integer insertDataModule(DataModule dataModule);
    
    /**
     * 删除数据模板（即修改模板状态）
     * @param dataModuleId
     * @return
     */
    Integer deleteDataModule(String dataModuleId);
    
    
}
