package cn.financial.quartz;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.financial.util.PropertiesUtils;
import cn.financial.util.SiteConst;


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
			
						String dayTime1 = SiteConst.DAY_TIME.substring(0,SiteConst.DAY_TIME.indexOf("-")).trim();
						Integer dayTime = Integer.valueOf(dayTime1) - 1;
						try {
								QuartzManager.addJob(QuartzManager.getsched(), "预算表生成消息提醒任务定时器", QuartzBudget.class, "0 0 0 15 12 ?",null);//每年12月份15号生成预算表
								/*QuartzManager.addJob(QuartzManager.getsched(), "损益表生成消息提醒任务定时器", QuartzJob.class, "0 3 0 "+SiteConst.DAY_TIME+" * ?",null);//每个月1号零点3分生成损益表*/	
								QuartzManager.addJob(QuartzManager.getsched(), "损益表生成消息提醒任务定时器", QuartzJob.class, "0 3 0 1 * ?",null);//每个月1号零点3分生成损益表
								/*QuartzManager.addJob(QuartzManager.getsched(), "系统停止录入数据截止日期提醒任务定时器", QuartzStatement.class, "0 0 2 5 * ?",null);//每个月5号凌晨2点生成系统关账提醒*/	
					        	QuartzManager.addJob(QuartzManager.getsched(), "密码有效时间到期提醒任务定时器", QuartzPwdTime.class, "0 0 1 * * ?" , null );//每天凌晨1点验证密码有效时间是否到期
						        QuartzManager.addJob(QuartzManager.getsched(), "损益生成记录表任务定时器", QuartzBusinessData.class, "0 0 0 1 * ?" , null );//每月1号凌晨0点生成损益生成记录表
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
	}else {
		System.out.println("获取应用程序上下文失败!");
		return;
	}
  }
}