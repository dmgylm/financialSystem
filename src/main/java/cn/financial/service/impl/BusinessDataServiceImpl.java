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
 * 
 * @author lmn
 *
 */
@Service("BusinessDataServiceImpl")
public class BusinessDataServiceImpl implements BusinessDataService {

	@Autowired
	private BusinessDataDao businessDataDao;

	/**
	 * 新增损益数据
	 */
	@Override
	public Integer insertBusinessData(BusinessData businessData) {
		return businessDataDao.insertBusinessData(businessData);
	}

	/**
	 * 修改损益数据
	 */
	@Override
	public Integer updateBusinessData(BusinessData businessData) {
		return businessDataDao.updateBusinessData(businessData);
	}

	/**
	 * 查询所有的损益数据
	 */
	@Override
	public List<BusinessData> getAll() {
		return businessDataDao.getAll();
	}

	/**
	 * 根据id查询损益数据
	 */
	@Override
	public BusinessData selectBusinessDataById(String id) {
		return businessDataDao.selectBusinessDataById(id);
	}

	/**
	 * 根据条件查询损益数据
	 */
	@Override
	public List<BusinessData> listBusinessDataBy(Map<Object, Object> map) {
		return businessDataDao.listBusinessDataBy(map);
	}

	/**
	 * 删除损益数据
	 */
	@Override
	public Integer deleteBusinessData(String id) {
		return businessDataDao.deleteBusinessData(id);
	}
	
	/**
	 * 根据时间id条件查询损益数据
	 */
	@Override
	public List<BusinessData> listBusinessDataByIdAndDate(Map<Object, Object> map) {
		return businessDataDao.listBusinessDataByIdAndDate(map);
	}

    @Override
    public List<BusinessData> businessDataExport(Map<Object, Object> map) {
       
        return businessDataDao.businessDataExport(map);
    }
    @Override
	public List<BusinessData> getBusinessAllBySomeOne(Map<Object, Object> map) {
		return businessDataDao.getBusinessAllBySomeOne(map);
	}
}
