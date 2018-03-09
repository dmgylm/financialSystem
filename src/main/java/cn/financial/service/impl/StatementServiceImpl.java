package cn.financial.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.StatementDao;
import cn.financial.model.Statement;
import cn.financial.service.StatementService;

@Service("StatementServiceImpl")
public class StatementServiceImpl implements StatementService{
    
            
            @Autowired
            private StatementDao statementDao;

            @Override
            public Integer insertStatement(Statement statement) {
                // TODO Auto-generated method stub
                return statementDao.insertStatement(statement);
            }

            @Override
            public Integer updateStatement(Statement statement) {
                // TODO Auto-generated method stub
                return statementDao.updateStatement(statement);
            }

            @Override
            public List<Statement> getAll() {
                // TODO Auto-generated method stub
                return statementDao.getAll();
            }

            @Override
            public Statement selectStatementById(String id) {
                // TODO Auto-generated method stub
                return statementDao.selectStatementById(id);
            }

            @Override
            public List<Statement> listStatementBy(Map<Object, Object> map) {
                // TODO Auto-generated method stub
                return statementDao.listStatementBy(map);
            }
            
}
 