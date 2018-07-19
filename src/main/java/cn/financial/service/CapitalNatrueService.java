package cn.financial.service;

import java.util.List;
import java.util.Map;

import cn.financial.model.CapitalNatrue;

/**
 * 账户性质项目分类Service
 * @author lmn
 *
 */
public interface CapitalNatrueService {
    
     
    /**
     * 根据条件查询账户性质项目分类表数据
     * @param map
     * @return
     */
    List<CapitalNatrue> listCapitalNatrue(Map<Object, Object> map);
  
}
