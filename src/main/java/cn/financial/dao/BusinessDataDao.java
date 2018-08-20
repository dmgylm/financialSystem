package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.BusinessData;

/**
 * 业务表 Dao
 * @author Lmn
 *
 */
public interface BusinessDataDao {
    
    /**
     * 新增业务表数据
     * @param businessData
     * @return 
     */
    Integer insertBusinessData(BusinessData businessData);
    
    /**
     * 删除业务数据 （status=0）
     * @param businessData
     * @return
     */
    Integer deleteBusinessData(String id);
    
    /**
     * 修改业务表数据
     * @param businessData
     * @return
     */
    Integer updateBusinessData(BusinessData businessData);  
   
    /**
     * 查询业务表所有的数据
     * @return
     */
    List<BusinessData> getAll();
     
    /**
     * 根据id查询业务表数据
     * @param id
     * @return
     */
    BusinessData selectBusinessDataById(String id);
     
    /**
     * 分页，根据条件查询业务表数据
     * @param map
     * @return
     */
    List<BusinessData> listBusinessDataBy(Map<Object, Object> map);    
    /**
     * 不分页，根据条件查询业务表数据
     * @param map
     * @return
     */
    List<BusinessData> getBusinessAllBySomeOne(Map<Object, Object> map);  
    
    /**
     * 根据条件导出数据
     * @param map
     * @return
     */
    List<BusinessData> businessDataExport(Map<Object, Object> map);
    
    /**
     * 根据时间id等条件查询业务表数据
     * @param map
     * @return
     */
    List<BusinessData> listBusinessDataByIdAndDate(Map<Object, Object> map);
    /**
     * 根据组织id查询业务表数据
     * @param id
     * @return
     */
	BusinessData selectBusinessDataByType(String id);     
	
	/**
     * 查询月份输入框
     * @param map
     * @return
     */
    List<BusinessData> businessDataYear(Map<Object, Object> map);
               
}
