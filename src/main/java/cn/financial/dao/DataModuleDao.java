package cn.financial.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.financial.model.DataModule;

public interface DataModuleDao {

	/**
     * 查询所有用户/多条件查询用户列表
     * @return
     */
    List<DataModule> listDataModule(Map<Object, Object> map);
    
    /**
     * 查询所有用户/多条件查询用户列表(简单列表)
     * @return
     */
    List<DataModule> queryDataModule(@Param("module") DataModule module,
    		@Param("startPage") Integer startPage,@Param("pageSize") Integer pageSize);
	
    List<DataModule> queryDataModules(Map<Object, Object> map);
    /**
     * 新增数据模块
     * @param dataModule
     * @return
     */
    Integer insertDataModule(DataModule dataModule);
    
    /**
     * 修改模板状态
     * @param dataModuleId
     * @return
     */
    Integer updateDataModuleState(String dataModuleId);

    /**
     * 根据参数获取单个数据模板
     * @param dataModuleId
     * @return
     */
	DataModule getDataModule(Map<String,Object> params);
    
    
}
