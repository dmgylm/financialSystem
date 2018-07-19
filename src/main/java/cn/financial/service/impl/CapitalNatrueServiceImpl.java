package cn.financial.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.CapitalNatrueDao;
import cn.financial.model.CapitalNatrue;
import cn.financial.service.CapitalNatrueService;

/**
 * 账户性质项目分类ServiceImpl
 * @author lmn
 *
 */
@Service("CapitalNatrueServiceImpl")
public class CapitalNatrueServiceImpl implements CapitalNatrueService{
    
    @Autowired
    private CapitalNatrueDao capitalNatrueDao;

    @Override
    public List<CapitalNatrue> listCapitalNatrue(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return capitalNatrueDao.listCapitalNatrue(map);
    }
   

}
 