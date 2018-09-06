package cn.financial.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import cn.financial.model.BusinessData;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;

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
     * 修改业务表状态
     * @param businessData
     * @return
     */
    Integer updateBusinessDataDelStatus(BusinessData businessData);
   
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
	
	
	List<BusinessData> listBusinessDataByIdAndDateList(Map<Object, Object> map);   
	
	/**
	 * 生成预算空白模板
	 * @param orgDep 部门对象
	 * @param year  当前年份
	 * @param month 当前月份
	 * @param dm  数据模板对象
	 * @param logger 日志类
	 * @param businessDataService  
	 * @param businessDataInfoService
	 * @param organizationService
	 */
	void createBusinessData(Organization orgDep,int year,DataModule dm) ;
  /**
   * 生产预算空白模板时往消息中心发消息
   * @param year 年份
   * @param logger 日志类
   * @param orgCompany 公司
   * @param orgDep 部门
   * @param messageService
   */
	void createBunsinessDataMessage(int year,Organization orgCompany,Organization orgDep);
	
	/**
     * 根据组织查询业务表数据
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
