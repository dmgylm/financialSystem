package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.BusinessDataDao;
import cn.financial.model.BusinessData;
import cn.financial.service.BusinessDataService;

/**
 * 损益表 ServiceImpl
 * @author lmn
 *
 */
@Service("StatementServiceImpl")
public class BusinessDataServiceImpl implements BusinessDataService{
    
            
            @Autowired
            private BusinessDataDao businessData;

            /**
             * 新增损益数据
             */
            @Override
            public Integer insertStatement(BusinessData statement) {
                // TODO Auto-generated method stub
                return businessData.insertStatement(statement);
            }

            /**
             * 修改损益数据
             */
            public Integer updateStatement(BusinessData statement) {
                return businessData.updateStatement(statement);
            }

            /**
             * 查询所有的损益数据
             */
            @Override
            public List<BusinessData> getAll() {
                // TODO Auto-generated method stub
                return businessData.getAll();
            }

            /**
             * 根据id查询损益数据
             */
            @Override
            public BusinessData selectStatementById(String id) {
                // TODO Auto-generated method stub
                return businessData.selectStatementById(id);
            }

            /**
             * 根据条件查询损益数据
             */
            @Override
            public List<BusinessData> listStatementBy(Map<Object, Object> map) {
                // TODO Auto-generated method stub
                return businessData.listStatementBy(map);
            }

            /**
             * 删除损益数据
             */
            @Override
            public Integer deleteStatement(String id) {
                // TODO Auto-generated method stub
                return businessData.deleteStatement(id);
            }
            
}
 