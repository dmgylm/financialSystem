package cn.financial.quartz;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class AccountQuartzListener implements ServletContextListener {
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	private static WebApplicationContext springContext;
	
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
		if ( springContext != null ) {
						try {
								QuartzManager.addJob(QuartzManager.getsched(), "预算表生成消息提醒任务定时器", QuartzBudget.class, "* * * 1 1 ?",null);//每年一月份一号生成预算表
								QuartzManager.addJob(QuartzManager.getsched(), "损益表生成消息提醒任务定时器", QuartzJob.class, "* * * 11 * ?",null);//每个月11号生成损益表
								QuartzManager.addJob(QuartzManager.getsched(), "系统停止录入数据截止日期提醒任务定时器", QuartzStatement.class, "0 0 0 5 * ?",null);//每个月5号生成系统关账提醒
						        QuartzManager.addJob(QuartzManager.getsched(), "密码有效时间到期提醒任务定时器", QuartzPwdTime.class, "0 0 9 * * ?" , null );
						        QuartzManager.addJob(QuartzManager.getsched(), "损益生成记录表任务定时器", QuartzBusinessData.class, "30 15 9 * * ?" , null );
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
	}else {
		System.out.println("获取应用程序上下文失败!");
		return;
	}
  }
}