package cn.financial.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.StatementDao;
import cn.financial.model.Statement;
import cn.financial.service.StatementService;

/**
 * 损益表 ServiceImpl
 * @author lmn
 *
 */
@Service("StatementServiceImpl")
public class StatementServiceImpl implements StatementService{
    
            
            @Autowired
            private StatementDao statementDao;

            /**
             * 新增损益数据
             */
            @Override
            public Integer insertStatement(Statement statement) {
                // TODO Auto-generated method stub
                return statementDao.insertStatement(statement);
            }

            /**
             * 修改损益数据
             */
            public Integer updateStatement(Statement statement) {
                return statementDao.updateStatement(statement);
            }

            /**
             * 查询所有的损益数据
             */
            @Override
            public List<Statement> getAll() {
                // TODO Auto-generated method stub
                return statementDao.getAll();
            }

            /**
             * 根据id查询损益数据
             */
            @Override
            public Statement selectStatementById(String id) {
                // TODO Auto-generated method stub
                return statementDao.selectStatementById(id);
            }

            /**
             * 根据条件查询损益数据
             */
            @Override
            public List<Statement> listStatementBy(Map<Object, Object> map) {
                // TODO Auto-generated method stub
                return statementDao.listStatementBy(map);
            }

            /**
             * 删除损益数据
             */
            @Override
            public Integer deleteStatement(String id) {
                // TODO Auto-generated method stub
                return statementDao.deleteStatement(id);
            }
            
}
 