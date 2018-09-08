package cn.financial.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.BusinessDataDao;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.DataModule;
import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.UserOrganization;
import cn.financial.quartz.QuartzBudget;
import cn.financial.service.BusinessDataService;
import cn.financial.service.MessageService;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.UuidUtil;

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
	@Autowired
	BusinessDataServiceImpl businessDataService;
	@Autowired
	MessageService messageService;
	@Autowired
	BusinessDataInfoServiceImpl businessDataInfoService;
	@Autowired
	OrganizationServiceImpl organizationService;
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
	 * 修改损益状态
	 */
	@Override
	public Integer updateBusinessDataDelStatus(BusinessData businessData) {
		return businessDataDao.updateBusinessDataDelStatus(businessData);
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
    /**
	 * 
	 * @param orgDep 部门对象
	 * @param year  当前年份
	 * @param month 当前月份
	 * @param dm  数据模板对象
	 * @param logger 异常类
	 * @param businessDataService  
	 * @param businessDataInfoService
	 * @param organizationService
	 */
    @Override
	public void createBusinessData(Organization orgDep,int year,DataModule dm) {
		Organization org = organizationService.getCompanyNameBySon(orgDep.getId());// 获取对应部门的公司
		if (org != null) {
			BusinessData statement = new BusinessData();
			String sid = UuidUtil.getUUID();
			statement.setId(sid);
			statement.setoId(org.getId());// 分公司id
			statement.setTypeId(orgDep.getId());// 部门（根据部门id查分公司id）
			statement.setYear(year);
			statement.setMonth(null);
			statement.setStatus(3);// 提交状态（0 待提交   1待修改  2已提交  3新增 4 退回修改）
			statement.setDelStatus(1);
			statement.setsId(2);// 1表示损益表 2表示预算表
			statement.setVersion(1);
			statement.setDataModuleId(dm.getId());// 数据模板id
			statement.setUpdateTime(new Date());
			Integer flag = businessDataService.insertBusinessData(statement);
			if (flag != 1) {
				System.out.println("预算主表数据新增失败");
			} else {
				BusinessDataInfo sdi = new BusinessDataInfo();
				sdi.setId(UuidUtil.getUUID());
				sdi.setBusinessDataId(sid);
				sdi.setInfo(JsonConvertProcess.simplifyJson(dm.getModuleData()).toString());
				Integer flagt = businessDataInfoService.insertBusinessDataInfo(sdi);
				if (flagt != 1) {
					System.out.println("预算从表数据新增失败");
				}
			}
		}
	}
     /**
      * 根据组织id查询
      */
	@Override
	public BusinessData selectBusinessDataByType(String id) {
		return businessDataDao.selectBusinessDataByType(id);
	}

    @Override
    public List<BusinessData> businessDataYear(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return businessDataDao.businessDataYear(map);
    }

	@Override
	public List<BusinessData> listBusinessDataByIdAndDateList(
			Map<Object, Object> map) {
		return businessDataDao.listBusinessDataByIdAndDateList(map);
	}

	@Override
	public void createBunsinessDataMessage(int year,  Organization orgCompany,Organization orgDep) {
			Message message = new Message();
			message.setId(UuidUtil.getUUID());
			message.setStatus(0);
			message.setTheme(1);
			message.setContent(year + "年"  + orgCompany.getOrgName()+orgDep.getOrgName()+ "预算表已生成");
			message.setoId(orgCompany.getId());
			message.setIsTag(0);
			message.setsName("系统默认");
			Integer i1 = messageService.saveMessage(message);
			if (i1 != 1) {
				System.out.println("预算报表发送消息失败");
		}		
	}
	
	 /**
     * 判断权限数据
     * @param list
     * @return
     */
	@Cacheable(value="userOrganizationValue",key="'userOrganization_'+#id")
    public Boolean isImport(Integer orgType,String id) {
	    boolean isImport = true;//是否可编辑
	          if(orgType==4){
	             isImport =false;
	          }
	          if(orgType==1){
	           Organization  organization=organizationService.getCompanyNameBySon(id);
	           if(organization==null){//如果没有查到公司级别的则是不能编辑
	            isImport =false;
	           }
	         }
	      return isImport;
    }
}
