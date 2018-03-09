package cn.financial.service;

import java.util.List;
import java.util.Map;
import cn.financial.model.Budget;

/**
 * 预算Service
 * @author lmn
 *
 */
public interface BudgetService {
    /**
     * 新增预算表数据
     * @param budget
     * @return
     */
    Integer insertBudget(Budget budget);
    
    /**
     * 修改预算表数据
     * @param budget
     * @return
     */
    Integer updateBudget(Budget budget);  
   
    /**
     * 查询预算表所有的数据
     * @return
     */
    List<Budget> getAllBudget();
     
    /**
     * 根据id查询预算表数据
     * @param id
     * @return
     */
    Budget selectBudgetById(String id);
     
    /**
     * 根据条件查询预算数据
     * @param map
     * @return
     */
    List<Budget> listBudgetBy(Map<Object, Object> map);
 
}