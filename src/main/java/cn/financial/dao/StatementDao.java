package cn.financial.dao;

import java.util.List;
import java.util.Map;
import cn.financial.model.Statement;

public interface StatementDao {
        
           //新增
           Integer insertStatement(Statement statement);
           
           //改  
           Integer updateStatement(Statement statement);  
          
           //查询损益表所有数据
           List<Statement> getAll();
            
            //根据id查询数据
           Statement selectStatementById(String id);
            
           //根据调价查询
           List<Statement> listStatementBy(Map<Object, Object> map);
     
}
