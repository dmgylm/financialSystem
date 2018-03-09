package cn.financial.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.financial.model.Statement;

/**
 * 损益表Service
 * @author lmn
 *
 */
public interface StatementService {
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
    Integer updateStatement(String id,String oId,String info,Date createTime,Date updateTime,
            String typeId,String uId,Integer year,Integer month,Integer status);  
   
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
