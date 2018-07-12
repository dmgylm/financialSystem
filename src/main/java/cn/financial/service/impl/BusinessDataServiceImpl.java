package cn.financial.service.impl;

import java.util.HashMap;
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
	public Integer updateBusinessData(BusinessData statement) {
		return businessDataDao.updateBusinessData(statement);
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
	    Map<Object, Object> resultMap=new HashMap<>();
        String year=map.get("year").toString();
        String month=map.get("month").toString();
        String sId=map.get("sId").toString();
        Integer pageMap=Integer.parseInt(map.get("page").toString());
        Integer pageSizeMap=Integer.parseInt(map.get("pageSize").toString());
        Integer pageSize=10;
        if(map.get("pageSize").toString()!=null&&!map.get("pageSize").toString().equals("")){
            pageSize=pageSizeMap;
            resultMap.put("pageSize",pageSize);
        }else{
            resultMap.put("pageSize",pageSize);
        }
        Integer start=0;
        if(map.get("page").toString()!=null && !map.get("page").toString().equals("")){
            start=pageSizeMap * (pageMap - 1);
            resultMap.put("start",start);
        }else{
            resultMap.put("start",start);
        }
        if(year!=null && !year.equals("")){
           resultMap.put("year", year);
        }
        if(month!=null && !month.equals("")){
            resultMap.put("month", month);
         }
        if(sId!=null && !sId.equals("")){
            resultMap.put("sId", sId);
         }
        resultMap.put("typeId", map.get("typeId"));
		return businessDataDao.listBusinessDataBy(resultMap);
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
        Map<Object, Object> resultMap=new HashMap<>();
        String year=map.get("year").toString();
        String month=map.get("month").toString();
        String sId=map.get("sId").toString();
        if(year!=null && !year.equals("")){
           resultMap.put("year", year);
        }
        if(month!=null && !month.equals("")){
            resultMap.put("month", month);
         }
        if(sId!=null && !sId.equals("")){
            resultMap.put("sId", sId);
         }
        resultMap.put("typeId", map.get("typeId"));
        return businessDataDao.businessDataExport(resultMap);
    }
    @Override
	public List<BusinessData> getBusinessAllBySomeOne(Map<Object, Object> map) {
		return businessDataDao.getBusinessAllBySomeOne(map);
	}
}
