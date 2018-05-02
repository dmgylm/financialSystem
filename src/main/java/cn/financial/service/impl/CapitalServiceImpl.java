package cn.financial.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.CapitalDao;
import cn.financial.model.Capital;
import cn.financial.service.CapitalService;

/**
 * 资金表ServiceImpl
 * @author lmn
 *
 */
@Service("CapitalServiceImpl")
public class CapitalServiceImpl implements CapitalService{
    @Autowired
    private CapitalDao capitalDao;

    /**
     * 新增资金表数据
     */
    @Override
    public Integer insertCapital(Capital capital) {
        // TODO Auto-generated method stub
        return capitalDao.insertCapital(capital);
    }

    /**
     * 修改资金表数据
     */
    @Override
    public Integer updateCapital(Capital capital) {
        // TODO Auto-generated method stub
        return capitalDao.updateCapital(capital);
    }

  /*  *//**
     * 查询所有的资金表数据
     *//*
    @Override
    public List<Capital> getAllCapital() {
        // TODO Auto-generated method stub
        return capitalDao.getAllCapital();
    }*/

    /**
     * 根据id查询资金表数据
     */
    @Override
    public Capital selectCapitalById(String id) {
        // TODO Auto-generated method stub
        return capitalDao.selectCapitalById(id);
    }

    /**
     * 根据条件资金表数据
     */
    @Override
    public List<Capital> listCapitalBy(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return capitalDao.listCapitalBy(map);
    }

    /**
     * 修改资金表数据 
     */
    @Override
    public Integer deleteCapital(String id) {
        // TODO Auto-generated method stub
        return capitalDao.deleteCapital(id);
    }

    
 
}
 