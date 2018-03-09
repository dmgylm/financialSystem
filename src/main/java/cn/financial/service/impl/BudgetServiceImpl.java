package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.BudgetDao;
import cn.financial.dao.StatementDao;
import cn.financial.model.Budget;
import cn.financial.model.Statement;
import cn.financial.service.BudgetService;
import cn.financial.service.StatementService;


@Service("BudgetServiceImpl")
public class BudgetServiceImpl implements BudgetService{
    @Autowired
    private BudgetDao budgetDao;

    @Override
    public Integer insertBudget(Budget budget) {
        // TODO Auto-generated method stub
        return budgetDao.insertBudget(budget);
    }

    @Override
    public Integer updateBudget(Budget budget) {
        // TODO Auto-generated method stub
        return budgetDao.updateBudget(budget);
    }

    @Override
    public List<Budget> getAllBudget() {
        // TODO Auto-generated method stub
        return budgetDao.getAllBudget();
    }

    @Override
    public Budget selectBudgetById(String id) {
        // TODO Auto-generated method stub
        return budgetDao.selectBudgetById(id);
    }

    @Override
    public List<Budget> listBudgetBy(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return budgetDao.listBudgetBy(map);
    }

   
 
}
 