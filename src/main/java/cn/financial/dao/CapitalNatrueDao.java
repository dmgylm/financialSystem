package cn.financial.dao;

import java.util.List;
import java.util.Map;
import cn.financial.model.CapitalNatrue;

/**
 * 账户性质项目分类表Dao
 * @author lmn
 *
 */
public interface CapitalNatrueDao {
   
    /**
     * 根据条件查询账户性质项目分类表数据
     * @param map
     * @return
     */
    List<CapitalNatrue> listCapitalNatrue();
    
}
