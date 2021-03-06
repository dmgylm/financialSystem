package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.Capital;

/**
 * 资金Service
 * @author lmn
 *
 */
public interface CapitalService {
    
     
    
    /**
     * 新增资金表数据
     * @param capital
     * @return
     */
    Integer insertCapital(Capital capital);
    
    Integer batchInsertCapital(List<Capital> listCapital);
    
    /**
     * 删除资金数据 （status=0）
     * @param statement
     * @return
     */
    Integer deleteCapital(String id);
    
    /**
     * 修改资金表数据
     * @param capital
     * @return
     */
    Integer updateCapital(Capital capital);  
   
  /*  *//**
     * 查询资金表所有的数据
     * @return
     *//*
    List<Capital> getAllCapital();*/
     
    /**
     * 根据id查询资金表数据
     * @param id
     * @return
     */
    Capital selectCapitalById(String id);
     
    /**
     * 根据条件查询资金表数据
     * @param map
     * @return
     */
    List<Capital> listCapitalBy(Map<Object, Object> map);
    
    
    /**
     * 根据条件导出资金表数据
     * @param map
     * @return
     */
    List<Capital> capitalExport(Map<Object, Object> map);
    
    /**
     * 导出所有的查询出的数据
     * @param map
     * @return
     */
    List<Capital> getAllCapital(Map<Object, Object> map);
    /**
     * 查询组织架构的公司
     * @param map
     * @return
     */
    List<Capital> capitalCompany(Map<Object, Object> map);
}
