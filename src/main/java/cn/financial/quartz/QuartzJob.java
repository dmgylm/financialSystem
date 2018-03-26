package cn.financial.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.financial.model.Message;
import cn.financial.service.impl.MessageServiceImpl;
import net.sf.json.JSONObject;

public class QuartzJob implements Job{
	
	private MessageServiceImpl messageService;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		messageService = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		
		JobDetail detail = arg0.getJobDetail();
		JSONObject object = (JSONObject) detail.getJobDataMap().get("message");
		
		Message message = new Message();
		message.setId(object.getString("id"));
		message.setStatus(object.getInt("status"));
		message.setContent(object.getString("content"));
		message.setTheme(object.getInt("theme"));
		message.setIsTag(object.getInt("isTag"));
		if(object.getString("oId")!=null) {
			message.setoId(object.getString("oId"));
		}
		if(object.getString("uId")!=null) {
			message.setuId(object.getString("uId"));
		}
		message.setsName(object.getString("sName"));
		System.out.println(message.getContent()+"content");
		Integer i = messageService.saveMessage(message);
		if (i == 1) {
			System.out.println("发送消息成功！");
		}
	}
}

