package cn.financial.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataBuild;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.DataModule;
import cn.financial.service.BusinessDataService;
import cn.financial.service.impl.BusinessDataBuildServiceImpl;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.UuidUtil;
/**
 * 损益表消息生成提醒任务
 * @author admin
 *
 */
public class QuartzJob implements Job{
	
	private MessageServiceImpl messageService;
	private OrganizationServiceImpl organizationService;
	private BusinessDataServiceImpl businessDataService;
	private DataModuleServiceImpl dataModuleServiceImpl;
	private BusinessDataInfoServiceImpl businessDataInfoService;
	private BusinessDataBuildServiceImpl buildService;//生成状态 
    
    protected Logger logger = LoggerFactory.getLogger(QuartzJob.class);
    
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		long start = System.currentTimeMillis();
		messageService = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		organizationService = (OrganizationServiceImpl) AccountQuartzListener.getSpringContext().getBean("OrganizationServiceImpl");
		businessDataService = (BusinessDataServiceImpl) AccountQuartzListener.getSpringContext().getBean("BusinessDataServiceImpl");
		dataModuleServiceImpl = (DataModuleServiceImpl) AccountQuartzListener.getSpringContext().getBean("DataModuleServiceImpl");
		businessDataInfoService = (BusinessDataInfoServiceImpl) AccountQuartzListener.getSpringContext().getBean("BusinessDataInfoServiceImpl");
		buildService = (BusinessDataBuildServiceImpl) AccountQuartzListener.getSpringContext().getBean("BusinessDataBuildServiceImpl");
		
		System.out.println("启动业务表定时添加任务");
		int sum=0;
		int zsum=0;
		String reportType = DataModule.REPORT_TYPE_PROFIT_LOSS; // reportType，数据模板报表类型。DataModule.REPORT_TYPE_BUDG预算，DataModule.REPORT_TYPE_PROFIT_LOSS损益
		String msgType = "损益"; // 消息报表类型损益还是预算
		int bunType = 1; // 业务表类型。 1损益2预算
		//List<Organization> orgDep = organizationService.getDep();// 获取所有部门
		List<Organization> orgCompany = organizationService.getCompany();// 获取所有公司
		List<BusinessDataBuild> listCreateBd=buildService.listBusinessDataBuild(0);//获取未生成的损益
		List<Map<String,Object>> saveOrg=new ArrayList<>();//保存同公司部门
		int year =Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);//本月生成上月的损益
		try {
			
			
			//System.out.println("部门条数+"+orgDep.size());
			
				for (int i = 0; i < listCreateBd.size(); i++) { // 获取所有部门
					DataModule dm=dataModuleServiceImpl.getDataModule(reportType,listCreateBd.get(i).getOrgPlate());
					if (dm != null) {
						Organization org = organizationService.getCompanyNameBySon(listCreateBd.get(i).getId());// 获取对应部门的公司
						if (org != null) {
							Map<Object,Object>map=new HashMap<>();
							map.put("typeId", listCreateBd.get(i).getId());//部门id
							map.put("year", year);//年份6
							map.put("sId", 2);//预算
							map.put("status", 2);//提交状态
							List<BusinessData> bus=businessDataService.getBusinessAllBySomeOne(map);//获取业务主表对应对应部门的预算表
							if(bus.size()>0) {
								BusinessDataInfo businessDataInfo=businessDataInfoService.selectBusinessDataById(bus.get(0).getId());//根据业务主表id获取子表内容
								BusinessData statement = new BusinessData();
								String sid = UuidUtil.getUUID();
								statement.setId(sid);
								statement.setoId(org.getId());// 分公司id
								statement.setTypeId(listCreateBd.get(i).getId());// 部门（根据部门id查分公司id）
								statement.setYear(year);
								statement.setMonth(month);
								statement.setStatus(3);// 提交状态（0 待提交   1待修改  2已提交  3新增 4 退回修改）
								statement.setDelStatus(1);
								statement.setsId(bunType);// 1表示损益表 2表示预算表
								statement.setVersion(1);
								statement.setUpdateTime(new Date());
								statement.setDataModuleId(dm.getId());// 数据模板id
								Integer flag = businessDataService.insertBusinessData(statement);
								sum++;
								if (flag != 1) {
									logger.error(msgType + "主表数据新增失败");
								} else {
									
									JSONObject dataMjo=JSONObject.parseObject(dm.getModuleData());//获取损益表数据模板
									JSONObject bInfo=JSONObject.parseObject(businessDataInfo.getInfo());
									JSONObject joAllBudget=bInfo.getJSONObject(String.valueOf(month));//获取预算模板
									JSONObject newBudgetHtml=new JSONObject();//与预算合并后生成的损益原始模板
									
									for (Iterator<String> iterIncome = dataMjo.keySet().iterator();iterIncome.hasNext();) {//获得损益模板最外层key
										String keyIncome = iterIncome.next();//损益模板key
										
										
										if(null!=keyIncome&& !keyIncome.equals("")) {//判断预算有没有相同的模板key
											JSONObject keyBudget=null;
											if(joAllBudget!=null) {
												keyBudget=(JSONObject) joAllBudget.get(keyIncome);//预算表模板key
											}
											Object objIncome = dataMjo.get(keyIncome);//获取损益模板key的值
											JSONArray jsonIncome=JSONArray.parseArray(objIncome.toString());//获取损益模板科目
											JSONArray jsoA=new JSONArray();
											//JSONObject obj=new JSONObject();
											
											for (int j = 0; j < jsonIncome.size(); j++) {
												
												//obj = new JSONObject();
												 JSONObject jo=(JSONObject) jsonIncome.get(j);
												 
												if(Integer.parseInt(jo.getString("type"))==4) {
													String keyG=jo.getString("key").replace("benyueyusuan", month+"yue");
													if(keyBudget!=null ) {
														if(keyBudget.containsKey(keyG)) {//判断是否有预算科目值
															int  joBudgetOne=keyBudget.getIntValue(keyG);//获取预算科目key的值
															jo.put("value", joBudgetOne);	
														}
													}
													
												}
												jsoA.add(jo);
											}
											newBudgetHtml.put(keyIncome, jsoA);
										}
								}
									BusinessDataInfo sdi = new BusinessDataInfo();
									sdi.setId(UuidUtil.getUUID());
									sdi.setBusinessDataId(sid);
									sdi.setInfo(JsonConvertProcess.simplifyJson(newBudgetHtml).toString());
									Integer flagt = businessDataInfoService.insertBusinessDataInfo(sdi);
									zsum++;
									if (flagt == 1) {
										Map<String ,Object> checkOrg=new HashMap<>();//保存同公司的部門
										checkOrg.put("orgDepName", listCreateBd.get(i).getOrgName());
										checkOrg.put("companyName", org.getOrgName());
										saveOrg.add(checkOrg);//保存所有部门和公司
										
										Map<Object, Object>  updateBuild=new HashMap<Object,Object>();
										updateBuild.put("id", listCreateBd.get(i).getId());//新增子表的時候更改生成损益表的状态
										updateBuild.put("status", 1);
										int rows=buildService.updateBusinessDataBuild(updateBuild);
										
									}else {
										logger.error(msgType + "从表数据新增失败");
									}
								}
							}
						}
					}
				}
				//将同一公司的部门进行合并
				Map<String, Object> result1 = new HashMap<String, Object>();
    			for(Map<String, Object> map : saveOrg){
    				String id = map.get("companyName").toString();
    				String value = map.get("orgDepName").toString();
    				if(result1.containsKey(id)){
    					String temp = result1.get(id).toString();
    					value +=","+ temp;
    				}
    				result1.put(id, value);
    			}
				for (int i = 0; i < orgCompany.size(); i++) {
					if(result1.containsKey(orgCompany.get(i).getOrgName())) {
						Message message = new Message();
						message.setId(UuidUtil.getUUID());
						message.setStatus(0);
						message.setTheme(1);
						message.setContent(year + "年" + month + "月" + orgCompany.get(i).getOrgName() +result1.get(orgCompany.get(i).getOrgName())+ msgType + "表已生成");
						message.setoId(orgCompany.get(i).getId());
						message.setIsTag(0);
						message.setsName("系统默认");
						Integer i1 = messageService.saveMessage(message);
						if (i1 != 1) {
							logger.error(msgType + "报表发送消息失败");
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(sum);
		System.out.println(zsum);
		System.out.println("时间花费~~~~");
		System.out.println(end - start);
	}
}

