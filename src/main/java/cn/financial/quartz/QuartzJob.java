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

import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.BusinessData;
import cn.financial.service.BusinessDataService;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.UuidUtil;
/**
 * 损益表消息生成提醒任务
 * @author admin
 *
 */
public class QuartzJob implements Job{
	
	private MessageServiceImpl messageService;
	private OrganizationServiceImpl organizationService;
	private BusinessDataService statementService;
    protected Logger logger = LoggerFactory.getLogger(QuartzJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		messageService = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		organizationService = (OrganizationServiceImpl) AccountQuartzListener.getSpringContext().getBean("OrganizationServiceImpl");
		statementService = (BusinessDataService) AccountQuartzListener.getSpringContext().getBean("BusinessDataServiceImpl");
		List<Organization> orglist = organizationService.listOrganizationBy(new HashMap<Object,Object>());
		List<Organization> orgCompany=organizationService.getCompany();
		Map<Object, Object> map;
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		
		try {
			for (int i = 0; i < orgCompany.size(); i++) {
				Message message = new Message();
				message.setId(UuidUtil.getUUID());
				message.setStatus(0);
				message.setTheme(1);
				message.setContent(year+"年"+month+"月"+orgCompany.get(i).getOrgName()+"损益报表已生成");
				message.setoId(orgCompany.get(i).getId());
				message.setIsTag(0);
				message.setsName("系统");
				Integer i1 = messageService.saveMessage(message);
				if (i1!= 1) {
					logger.error("发送损益报表消息失败");
				}
			}
			
			for( int i=0;i<orglist.size();i++ ) {
				
				map = new HashMap<Object, Object>();
				map.put("id", orglist.get(i).getId());
				
				if( !organizationService.hasOrganizationSon(map) ) {
				    Organization rog = organizationService.getCompanyNameBySon(orglist.get(i).getId());
					BusinessData statement = new BusinessData();
			        statement.setId(UuidUtil.getUUID());
			        statement.setoId(rog.getId());//分公司id
			        statement.setTypeId(orglist.get(i).getId());//部门（根据部门id查分公司id）
			        statement.setYear(year);
			        statement.setMonth(month);
			        statement.setStatus(2);//提交状态（0 待提交   1已提交  2新增）
			        statement.setDelStatus(1);
			        statement.setsId(1);//1表示损益表   2表示预算表
			        Integer flag = statementService.insertBusinessData(statement);
			        if(flag!=1) {
			    	   logger.error("损益报表数据新增失败");
			       }
					
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时任务发生错误");
		}
	}
	
}

