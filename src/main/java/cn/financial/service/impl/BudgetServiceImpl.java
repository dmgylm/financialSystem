package cn.financial.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.BudgetDao;
import cn.financial.model.Budget;
import cn.financial.service.BudgetService;

/**
 * 预算表的ServiceImpl
 * @author lmn
 *
 */

@Service("BudgetServiceImpl")
public class BudgetServiceImpl implements BudgetService{
    @Autowired
    private BudgetDao budgetDao;

    /**
     * 新增预算表的数据
     */
    @Override
    public Integer insertBudget(Budget budget) {
        // TODO Auto-generated method stub
        return budgetDao.insertBudget(budget);
    }

    /**
     * 修改预算表的数据
     */
    @Override
    public Integer updateBudget(Budget budget) {
        // TODO Auto-generated method stub
        return budgetDao.updateBudget(budget);
    }

    /**
     * 查询所有的预算表的数据
     */
    @Override
    public List<Budget> getAllBudget() {
        // TODO Auto-generated method stub
        return budgetDao.getAllBudget();
    }

    /**
     * 根据id查询预算表的数据
     */
    @Override
    public Budget selectBudgetById(String id) {
        // TODO Auto-generated method stub
        return budgetDao.selectBudgetById(id);
    }

    /**
     * 根据条件查询预算表的数据
     */
    @Override
    public List<Budget> listBudgetBy(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return budgetDao.listBudgetBy(map);
    }

    /**
     * 删除预算表的数据
     */
    @Override
    public Integer deleteBudget(String id) {
        // TODO Auto-generated method stub
        return budgetDao.deleteBudget(id);
    }

   
 
}
 