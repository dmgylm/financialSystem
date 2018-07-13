package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.exception.FormulaAnalysisException;
import cn.financial.model.DataModule;

public interface DataModuleService {

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
    Integer updateDataModule(String dataModuleId);

    /**
     * 根据ID获取数据模板
     * @param dataModuleId
     * @return
     */
	DataModule getDataModule(String dataModuleId);

	/**
	 * 根据报表类型及业务板块获取数据模板
	 * @param reportType 报表类型
	 * @param businessType 业务板块
	 * @return
	 */
	DataModule getDataModule(String reportType, String businessType);

	/**
	 * 编辑数据模板
	 * @param reportType 报表类型
	 * @param businessType 业务板块
	 * @param html html代码
	 * @param firstRowNum 模块列号
	 * @param secondRowNum 科目列号
	 * @param firstColNum 
	 * @param secondColNum
	 * @throws FormulaAnalysisException
	 */
	void editDataModule(String reportType, String businessType, String html) throws FormulaAnalysisException;

}
