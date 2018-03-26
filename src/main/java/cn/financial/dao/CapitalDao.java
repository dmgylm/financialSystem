package cn.financial.dao;

import java.util.List;
import java.util.Map;
import cn.financial.model.Capital;

/**
 * 资金表Dao
 * @author lmn
 *
 */
public interface CapitalDao {
   
    /**
     * 新增资金表数据
     * @param capital
     * @return
     */
    Integer insertCapital(Capital capital);
    
    /**
     * 删除资金表数据 （status=0）
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
   
 /*   *//**
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
    List<Capital> listCapitalBy(Capital capital);

}
