package cn.financial.dao;

import java.util.List;
import java.util.Map;
import cn.financial.model.BusinessData;

/**
 * 损益表 Dao
 * @author Lmn
 *
 */
public interface BusinessDataDao {
    
    /**
     * 新增损益表数据
     * @param businessData
     * @return 
     */
    Integer insertBusinessData(BusinessData businessData);
    
    /**
     * 删除损益数据 （status=0）
     * @param businessData
     * @return
     */
    Integer deleteBusinessData(String id);
    
    /**
     * 修改损益表数据
     * @param businessData
     * @return
     */
    Integer updateBusinessData(BusinessData businessData);  
   
    /**
     * 查询损益表所有的数据
     * @return
     */
    List<BusinessData> getAll();
     
    /**
     * 根据id查询损益表数据
     * @param id
     * @return
     */
    BusinessData selectBusinessDataById(String id);
     
    /**
     * 根据条件查询损益表数据
     * @param map
     * @return
     */
    List<BusinessData> listBusinessDataBy(Map<Object, Object> map);       
               
}