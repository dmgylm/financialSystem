package cn.financial.quartz;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import cn.financial.model.Message;
import cn.financial.model.User;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.UuidUtil;

/**
 *	密码有效时间到期提醒
 */
public class QuartzPwdTime implements Job {
	
	private UserServiceImpl userServiceImpl;
	
	private MessageServiceImpl messageServiceImpl;
	
	protected Logger logger = LoggerFactory.getLogger(QuartzJob.class);
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		userServiceImpl = (UserServiceImpl) AccountQuartzListener.getSpringContext().getBean("UserServiceImpl");
		
		messageServiceImpl = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		
		try {
			Map<Object, Object> map = new HashMap<>();
//			map.put("pageSize", 10000);
//			map.put("start", 0);
			List<User> list = userServiceImpl.listUser(map);
			for (int i = 0; i < list.size(); i++) {
				String expreTime = list.get(i).getExpreTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(expreTime, new ParsePosition(0));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, -5);
				Date date1 = calendar.getTime();
				String pwdtime = sdf.format(date1);//密码有效期前五天的时间
				String nowtime = sdf.format(new Date());//获取当日的时间
				if (pwdtime.equals(nowtime)) {
					//	        	System.out.println("登陆密码即将在五天后失效,请尽快更改！");
					Message message = new Message();
					message.setId(UuidUtil.getUUID());
					message.setStatus(0);
					message.setTheme(1);
					message.setContent("登陆密码即将在五天后失效,请尽快更改！");
					message.setuId(list.get(i).getId());
					message.setIsTag(0);
					message.setsName("系统默认");
					Integer i1 = messageServiceImpl.saveMessage(message);
					if (i1 != 1) {
						logger.error("发送消息失败");
					}

				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时任务发生错误");
		}
		
	}

}
