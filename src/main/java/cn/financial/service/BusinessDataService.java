package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.BusinessData;

/**
 * 业务表Service
 * @author lmn
 *
 */
public interface BusinessDataService {
    /**
     * 新增业务表数据
     * @param businessData
     * @return 
     */
    Integer insertBusinessData(BusinessData businessData);
    
    /**
     * 删除业务数据 （status=0）
     * @param id
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
     * 根据条件查询业务表数据
     * @param map
     * @return
     */
    List<BusinessData> listBusinessDataBy(Map<Object, Object> map);
    
    /**
     * 分页，根据条件导出数据
     * @param map
     * @return
     */
    List<BusinessData> businessDataExport(Map<Object, Object> map);
    /**
     * 不分页，根据条件查询业务表数据
     * @param map
     * @return
     */
    List<BusinessData> getBusinessAllBySomeOne(Map<Object, Object> map);  

    /**
     * 根据时间id条件查询业务表数据
     * @param map
     * @return
     */
	List<BusinessData> listBusinessDataByIdAndDate(Map<Object, Object> map);  
}
