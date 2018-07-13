package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.financial.dao.DataModuleDao;
import cn.financial.exception.FormulaAnalysisException;
import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.response.DataModuleResult;
import cn.financial.model.response.DataModulesResult;
import cn.financial.model.response.ModuleList;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.RedisCacheService;
import cn.financial.util.HtmlAnalysis;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.UuidUtil;

@Service("DataModuleServiceImpl")
public class DataModuleServiceImpl implements DataModuleService{

	@Autowired
	private DataModuleDao dataModuleDao;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 获取模板列表
	 */
	public List<DataModule> listDataModule(Map<Object, Object> map) {
		return dataModuleDao.listDataModule(map);
	}
	/**
     * 查询所有用户/多条件查询用户列表(简单列表)
     * @return
     */
	@Override
	public List<ModuleList> queryModuleList(DataModule module,
    		Integer startPage,Integer pageSize) {
		
		List<ModuleList> moduleLists=new ArrayList<ModuleList>();
		
		List<DataModule> dataModules=dataModuleDao.queryDataModule(module, startPage, pageSize);
		for(DataModule dataModule:dataModules){
			ModuleList moduleList=new ModuleList();
			moduleList.setId(dataModule.getId());
			moduleList.setCreateTime(dataModule.getCreateTime());
			moduleList.setFounder(dataModule.getFounder());
			moduleList.setModuleName(dataModule.getModuleName());
			moduleList.setStatue(dataModule.getStatue());
			moduleList.setVersionNumber(dataModule.getVersionNumber());
			
			moduleLists.add(moduleList);
			
		}
		
		return moduleLists;
	}
	
	public DataModuleResult queryModuleLists(Map<Object, Object> map,Integer page,Integer pageSize) {
		DataModuleResult result=new DataModuleResult();
		List<ModuleList> moduleLists=new ArrayList<ModuleList>();
		PageHelper.startPage(page, pageSize);
		List<DataModule> dataModules=dataModuleDao.queryDataModules(map);
		//用PageInfo对结果进行包装
	    PageInfo<DataModule> pageInfo = new PageInfo<DataModule>(dataModules);
	    System.out.println(pageInfo.getList());
	    System.out.println(pageInfo.getTotal());
	    System.out.println(pageInfo.getEndRow());
	    System.out.println(pageInfo.getSize());
		
		for(DataModule dataModule:dataModules){
			ModuleList moduleList=new ModuleList();
			moduleList.setId(dataModule.getId());
			moduleList.setCreateTime(dataModule.getCreateTime());
			moduleList.setFounder(dataModule.getFounder());
			moduleList.setModuleName(dataModule.getModuleName());
			moduleList.setStatue(dataModule.getStatue());
			moduleList.setVersionNumber(dataModule.getVersionNumber());
			
			moduleLists.add(moduleList);
			
		}
		result.setModuleLists(moduleLists);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	public Integer insertDataModule(DataModule dataModule) {
		return dataModuleDao.insertDataModule(dataModule);
	}

	public Integer updateDataModule(String dataModuleId) {
		return dataModuleDao.updateDataModuleState(dataModuleId);
	}
	
	public List<ModuleList> queryModuleList(Map<Object, Object> map){
		List<ModuleList> moduleLists=new ArrayList<ModuleList>();
		
		/*List<DataModule> dataModules=queryDataModule(map);
		for(DataModule dataModule:dataModules){
			ModuleList moduleList=new ModuleList();
			moduleList.setId(dataModule.getId());
			moduleList.setCreateTime(dataModule.getCreateTime());
			moduleList.setFounder(dataModule.getFounder());
			moduleList.setModuleName(dataModule.getModuleName());
			moduleList.setStatue(dataModule.getStatue());
			moduleList.setVersionNumber(dataModule.getVersionNumber());
			
			moduleLists.add(moduleList);
			
		}*/
		
		return moduleLists;
	}

	/**
	 * 根据模板ID获取模板数据
	 */
	@Cacheable(value="dataModule",key="'dataModule_'+#dataModuleId")
	public DataModule getDataModule(String dataModuleId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", dataModuleId);
		DataModule dataModule=dataModuleDao.getDataModule(params);
		HtmlGenerate hg = new HtmlGenerate();
		String html = hg.generateHtml(dataModule.getModuleData(),hg.HTML_TYPE_TEMPLATE);
		dataModule.setDataHtml(html);
		
		return dataModule;
	}

	/**
	 * 根据报表类型及业务类型获取最新模板数据
	 */
	@Cacheable(value="dataModule",key="'newestDataModule_'+#reportType+#businessType")
	public DataModule getDataModule(String reportType, String businessType) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("reportType", reportType);
		params.put("businessType", businessType);
		return dataModuleDao.getDataModule(params);
	}

	/**
	 * 编辑模板
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editDataModule(String reportType,
			String businessType, String html)
			throws FormulaAnalysisException {
		HtmlAnalysis htmlAnalysis = new HtmlAnalysis(html);
		String json = htmlAnalysis.analysis();
		DataModule dataModule = getDataModule(reportType,businessType);
		Integer versionNumber = 1;
		if(dataModule != null) {
			versionNumber = Integer.parseInt(dataModule.getVersionNumber());
			dataModule.setStatue(DataModule.STATUS_NOVALID);
			dataModuleDao.updateDataModuleState(dataModule.getId());
			versionNumber++;
		}
		
		DataModule bean = new DataModule();
		bean.setId(UuidUtil.getUUID());
		bean.setVersionNumber(String.valueOf(versionNumber));
		bean.setReportType(reportType);
		bean.setBusinessType(businessType);
		bean.setModuleData(json);
		bean.setStatue(DataModule.STATUS_CONSUMED);
		bean.setModuleName(getDataModuleName(reportType,businessType));
		dataModuleDao.insertDataModule(bean);
		//清除数据模板缓存
		redisCacheService.removeAll("dataModule");
	}

	/**
	 * 生成模板名称
	 * @param reportType
	 * @param businessType
	 * @return
	 */
	private String getDataModuleName(String reportType, String businessType) {
		Organization org = organizationService.getOrgaByKey(businessType);
		String reportTypeName = DataModule.getReprtTypeName(reportType);
		return org.getOrgName() + reportTypeName;
	}
	
	
	
	
}
