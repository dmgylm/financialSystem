package cn.financial.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.financial.model.Budget;
import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.service.BudgetService;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.UuidUtil;
/**
 * 预算表消息生成提醒任务
 * @author admin
 *
 */
public class QuartzBudget implements Job{
	private MessageServiceImpl messageService;
	private OrganizationServiceImpl organizationService;
    private BudgetService budgetService;
    protected Logger logger = LoggerFactory.getLogger(QuartzBudget.class);
    
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		messageService = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		organizationService = (OrganizationServiceImpl) AccountQuartzListener.getSpringContext().getBean("OrganizationServiceImpl");
	    budgetService = (BudgetService) AccountQuartzListener.getSpringContext().getBean("BudgetServiceImpl");
	    List<Organization> orglist = organizationService.listOrganizationBy(new HashMap<Object,Object>());
		
		Map<Object, Object> map;
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
	    try {
	    	for( int i=0;i<orglist.size();i++ ) {
			map = new HashMap<Object, Object>();
			map.put("id", orglist.get(i).getId());
			if( !organizationService.hasOrganizationSon(map) ) {
				Organization rog = organizationService.getCompanyNameBySon(orglist.get(i).getId());
		    	Message message = new Message();
				message.setId(UuidUtil.getUUID());
				message.setStatus(0);
				message.setTheme(1);
				message.setContent(year+"年"+month+"月"+rog.getOrgName()+orglist.get(i).getOrgName()+"预算报表已生成");
				message.setoId(orglist.get(i).getId());
				message.setIsTag(0);
				message.setsName("系统");
				Integer i1 = messageService.saveMessage(message);
				if (i1 != 1) {
					logger.error("发送预算报表消息失败");
				}
				Budget budget = new Budget();
	            budget.setId(UuidUtil.getUUID());
	            budget.setoId(rog.getId());//分公司id
	            budget.setTypeId(orglist.get(i).getId());//部门（根据部门id查分公司id）
	            budget.setStatus(2);//提交状态（0 待提交   1已提交  2新增）
	            budget.setDelStatus(1);
	            budget.setYear(year);
	            budget.setMonth(month);
	            Integer flag = budgetService.insertBudget(budget);
	            if (flag != 1) {
	            	 logger.error("预算报表数据新增失败");
	            	}
		    	}
	    	}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

