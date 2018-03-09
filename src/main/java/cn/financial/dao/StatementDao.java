package cn.financial.dao;

import java.util.List;
import java.util.Map;
import cn.financial.model.Statement;

/**
 * 损益表 Dao
 * @author Lmn
 *
 */
public interface StatementDao {
    
    /**
     * 新增损益表数据
     * @param statement
     * @return 
     */
    Integer insertStatement(Statement statement);
    
    /**
     * 删除损益数据 （status=0）
     * @param statement
     * @return
     */
    Integer deleteStatement(String id);
    
    /**
     * 修改损益表数据
     * @param statement
     * @return
     */
    Integer updateStatement(Statement statement);  
   
    /**
     * 查询损益表所有的数据
     * @return
     */
    List<Statement> getAll();
     
    /**
     * 根据id查询损益表数据
     * @param id
     * @return
     */
    Statement selectStatementById(String id);
     
    /**
     * 根据条件查询损益表数据
     * @param map
     * @return
     */
    List<Statement> listStatementBy(Map<Object, Object> map);       
               
}
