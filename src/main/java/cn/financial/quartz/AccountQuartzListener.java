package cn.financial.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.Statement;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.OrganizationService;
import cn.financial.service.StatementService;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.service.impl.StatementServiceImpl;
import cn.financial.service.impl.UserRoleServiceImpl;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;


public class AccountQuartzListener implements ServletContextListener {
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	private static WebApplicationContext springContext;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private StatementService statementService;
	@Autowired
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
				    Organization rog = organizationService.getCompanyNameBySon(orglist.get(i).getId());
					if( month+1 == 1 ) {
						
						Message message = new Message();
						message.setId(UuidUtil.getUUID());
						message.setStatus(0);
						message.setTheme(1);
						message.setContent(year+"年1月"+rog.getOrgName()+orglist.get(i).getOrgName()+"预算报表已生成");
						message.setoId(orglist.get(i).getId());
						message.setIsTag(0);
						message.setsName("系统");
						
						String job_name = "预算表单消息提示定时器"+i ;
						
						try {
								QuartzManager.addJob(QuartzManager.getsched(), job_name, QuartzJob.class, "0 0 0 1 1 ? *" , JSONObject
										.fromObject(message));
								
						} catch (SchedulerException e) {
								e.printStackTrace();
						}
					}
					
					Message message = new Message();
					message.setId(UuidUtil.getUUID());
					message.setStatus(0);
					message.setTheme(1);
					message.setContent(year+"年"+month+"月"+rog.getOrgName()+orglist.get(i).getOrgName()+"损益报表已生成");
					message.setoId(orglist.get(i).getId());
					message.setIsTag(0);
					message.setsName("系统");
					
					String job_name = "损益表单消息提示定时器"+i ; 
					
					try {
							QuartzManager.addJob(QuartzManager.getsched(), job_name, QuartzJob.class, "0 0/1 * * * ? *" , JSONObject
									.fromObject(message));
							System.out.println("orgId=============="+orglist.get(i).getId());
							Statement statement = new Statement();
							statement.setId(UuidUtil.getUUID());
							statement.setoId(rog.getOrgName());//分公司
							statement.setTypeId(orglist.get(i).getId());//部门（根据部门id查分公司id）
							statement.setYear(year);
							statement.setMonth(month);
			                statement.setCreateTime(new Date());
			                statement.setStatus(2);//提交状态（0 待提交   1已提交  2新增）
			                statement.setDelStatus(1);
			                statement.setuId("");
			                statement.setInfo("");
			                statement.setUpdateTime(null);
			                Integer flag = statementService.insertStatement(statement);
			                if (flag == 1) {
			                    System.out.println("损益报表数据新增成功");
			                } else {
			                    System.out.println("损益报表数据新增失败");
			                }
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