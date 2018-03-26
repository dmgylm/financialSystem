package cn.financial.quartz;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.UserRole;
import cn.financial.service.OrganizationService;
import cn.financial.service.impl.UserRoleServiceImpl;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;


public class AccountQuartzListener implements ServletContextListener {
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	private static WebApplicationContext springContext;
	
	private OrganizationService organizationService;
	
	private UserRoleServiceImpl userRoleServiceImpl;
	
	public AccountQuartzListener() {
		super();
	}
	
	public static WebApplicationContext getSpringContext() {
		return springContext;
	}
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		
		System.out.println("@@@@@@@@@服务开启了");
		
		springContext = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());
		
		organizationService = (OrganizationService) springContext
				.getBean("OrganizationServiceImpl");
		
		userRoleServiceImpl = (UserRoleServiceImpl) springContext
				.getBean("UserRoleServiceImpl");
		
		if ( springContext != null ) {
			
			List<Organization> orglist = organizationService.listOrganizationBy(new HashMap<Object,Object>());
			
			List<UserRole> rolelist = userRoleServiceImpl.listUserRole(null);
			
			Map<Object, Object> map;
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
			
			for( int i=0;i<orglist.size();i++ ) {
				
				map = new HashMap<Object, Object>();
				map.put("id", orglist.get(i).getId());
				
				if( !organizationService.hasOrganizationSon(map) ) {
					
					if( month+1 == 3 ) {
						
						Message message = new Message();
						message.setId(UuidUtil.getUUID());
						message.setStatus(0);
						message.setTheme(1);
						message.setContent(year+"年1月"+orglist.get(i).getOrgName()+orglist.get(i).getOrgName()+"预算报表已生成");
						message.setoId(orglist.get(i).getId());
						message.setIsTag(0);
						message.setsName("系统");
						
						String job_name = "预算表单消息提示定时器"+i ;
						
						try {
								QuartzManager.addJob(QuartzManager.getsched(), job_name, QuartzJob.class, "0 0 0 1 1 ?" , JSONObject
										.fromObject(message));
						} catch (SchedulerException e) {
								e.printStackTrace();
						}
					}
					
					Message message = new Message();
					message.setId(UuidUtil.getUUID());
					message.setStatus(0);
					message.setTheme(1);
					message.setContent(year+"年"+month+"月"+orglist.get(i).getOrgName()+orglist.get(i).getOrgName()+"损益报表已生成");
					message.setoId(orglist.get(i).getId());
					message.setIsTag(0);
					message.setsName("系统");
					
					String job_name = "损益表单消息提示定时器"+i ; 
					
					try {
							QuartzManager.addJob(QuartzManager.getsched(), job_name, QuartzJob.class, "0 0 0 11 * ? *" , JSONObject
									.fromObject(message));
					} catch (SchedulerException e) {
							e.printStackTrace();
					}
				 }
			}
			for( int i =0;i<rolelist.size();i++ ) {
				
				if( rolelist.get(i).getRoleName().equals("制单员") ) {
					
					Message message = new Message();
					message.setId(UuidUtil.getUUID());
					message.setStatus(0);
					message.setTheme(1);
					message.setContent(" 每月10号为系统关账日期，请提前做好相关工作");
					message.setuId(rolelist.get(i).getuId());
					message.setIsTag(0);
					message.setsName("系统");
					
					String job_name = "关账消息提示定时器"+i ; 
					
					try {
							QuartzManager.addJob(QuartzManager.getsched(), job_name, QuartzJob.class, "0 0 0 5 * ? *" , JSONObject
									.fromObject(message));
					} catch (SchedulerException e) {
							e.printStackTrace();
					}
				}
			}
	}else {
		System.out.println("获取应用程序上下文失败!");
		return;
	}
  }
}