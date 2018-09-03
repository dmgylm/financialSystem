package cn.financial.service;


import java.util.List;
import java.util.Map;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;

/**
 * 损益表 Dao
 * @author Lmn
 *
 */
public interface BusinessDataInfoService {
    
    /**
     * 新增损益从表数据
     * @param businessData
     * @return 
     */
    Integer insertBusinessDataInfo(BusinessDataInfo businessData);
   
    
    /**
     * 删除损益从表数据 （status=0）
     * @param businessData
     * @return
     */
    Integer deleteBusinessDataInfo(String id);
    
    /**
     * 修改损益从表数据
     * @param businessData
     * @return
     */
    Integer updateBusinessDataInfo(BusinessDataInfo businessData);  
    /**
     * 修改损益从表状态
     * @param businessData
     * @return
     */
    Integer updateBusinessDataInfoDelStatus(BusinessDataInfo businessData);  
   
    /**
     * 查询损益从表所有的数据
     * @return
     */
    List<BusinessData> getAll(Map<Object, Object> map);
     
    /**
     * 根据id查询损益表数据
     * @param id
     * @return
     */
    BusinessDataInfo selectBusinessDataById(String id);
     

}
