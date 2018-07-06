package cn.financial.quartz;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.DataModule;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.JsonConvertProcess;
import cn.financial.util.UuidUtil;

/**
 * 预算表消息生成提醒任务
 * 
 * @author admin
 *
 */
@Component
public class QuartzBudget implements Job {
	private MessageServiceImpl messageService;
	private OrganizationServiceImpl organizationService;
	private BusinessDataServiceImpl businessDataService;
	protected Logger logger = LoggerFactory.getLogger(QuartzBudget.class);
	private DataModuleServiceImpl dataModuleServiceImpl;
	private BusinessDataInfoServiceImpl businessDataInfoService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long start = System.currentTimeMillis();
		messageService = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		organizationService = (OrganizationServiceImpl) AccountQuartzListener.getSpringContext()
				.getBean("OrganizationServiceImpl");
		businessDataService = (BusinessDataServiceImpl) AccountQuartzListener.getSpringContext()
				.getBean("BusinessDataServiceImpl");
		dataModuleServiceImpl = (DataModuleServiceImpl) AccountQuartzListener.getSpringContext()
				.getBean("DataModuleServiceImpl");
		businessDataInfoService = (BusinessDataInfoServiceImpl) AccountQuartzListener.getSpringContext()
				.getBean("BusinessDataInfoServiceImpl");
		System.out.println("启动业务表定时添加任务");
		int sum=0;
		int zsum=0;
		String reportType = ""; // reportType，数据模板报表类型。DataModule.REPORT_TYPE_BUDG预算，DataModule.REPORT_TYPE_PROFIT_LOSS损益
		String msgType = ""; // 消息报表类型损益还是预算
		int bunType = 0; // 业务表类型。 1损益2预算
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);// 获取当前系统时间几号
		day=1;
		if (day == 1) {// 预算
			reportType = DataModule.REPORT_TYPE_BUDGET;
			msgType = "预算";
			bunType = 2;
		} else if (day == 11) {
			reportType = DataModule.REPORT_TYPE_PROFIT_LOSS;
			msgType = "损益";
			bunType = 1;
		}
		List<Organization> orgDep = organizationService.getDep();// 获取所有部门
		List<Organization> orgCompany = organizationService.getCompany();// 获取所有公司
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		try {
			System.out.println("部门条数+"+orgDep.size());
			
				for (int i = 0; i < orgDep.size(); i++) { // 获取所有部门
					DataModule dm=dataModuleServiceImpl.getDataModule(reportType,orgDep.get(i).getOrgPlateId());
					if (dm != null) {
						Organization org = organizationService.getCompanyNameBySon(orgDep.get(i).getId());// 获取对应部门的公司
						if (org != null) {
							BusinessData statement = new BusinessData();
							String sid = UuidUtil.getUUID();
							statement.setId(sid);
							statement.setoId(org.getId());// 分公司id
							statement.setTypeId(orgDep.get(i).getId());// 部门（根据部门id查分公司id）
							statement.setYear(year);
							statement.setMonth(month);
							statement.setStatus(2);// 提交状态（0 待提交 1已提交 2新增）
							statement.setDelStatus(1);
							statement.setsId(bunType);// 1表示损益表 2表示预算表
							statement.setDataModuleId(dm.getId());// 数据模板id
							Integer flag = businessDataService.insertBusinessData(statement);
							sum++;
							if (flag != 1) {
								logger.error(msgType + "主表数据新增失败");
							} else {
								BusinessDataInfo sdi = new BusinessDataInfo();
								sdi.setId(UuidUtil.getUUID());
								sdi.setBusinessDataId(sid);
								sdi.setInfo(JsonConvertProcess.simplifyJson(dm.getModuleData()).toString());
								Integer flagt = businessDataInfoService.insertBusinessDataInfo(sdi);
								zsum++;
								if (flagt != 1) {
									logger.error(msgType + "从表数据新增失败");
								}
							}
						}
					}
				}
				for (int i = 0; i < orgCompany.size(); i++) {
					Message message = new Message();
					message.setId(UuidUtil.getUUID());
					message.setStatus(0);
					message.setTheme(1);
					message.setContent(year + "年" + month + "月" + orgCompany.get(i).getOrgName() + msgType + "表已生成");
					message.setoId(orgCompany.get(i).getId());
					message.setIsTag(0);
					message.setsName("系统默认");
					Integer i1 = messageService.saveMessage(message);
					if (i1 != 1) {
						logger.error(msgType + "报表发送消息失败");
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
