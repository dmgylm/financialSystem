package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.BusinessDataDao;
import cn.financial.dao.BusinessDataInfoDao;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.service.BusinessDataInfoService;

/**
 * 损益表Service
 * @author lmn
 *
 */
@Service("BusinessDataInfoServiceImpl")
public class BusinessDataInfoServiceImpl implements BusinessDataInfoService{
	@Autowired
	private BusinessDataInfoDao businessDataInfoDao;
	@Override
	public Integer insertBusinessDataInfo(BusinessDataInfo businessData) {
		return businessDataInfoDao.insertBusinessDataInfo(businessData);
	}

	@Override
	public Integer deleteBusinessDataInfo(String id) {
		return businessDataInfoDao.deleteBusinessDataInfo(id);
	}

	@Override
	public Integer updateBusinessDataInfo(BusinessDataInfo businessData) {
		return businessDataInfoDao.updateBusinessDataInfo(businessData);
	}

	@Override
	public List<BusinessData> getAll(Map<Object, Object> map) {
		return businessDataInfoDao.getAll(map);
	}

	@Override
	public BusinessDataInfo selectBusinessDataById(String id) {
		return businessDataInfoDao.selectBusinessDataById(id);
	}
}
