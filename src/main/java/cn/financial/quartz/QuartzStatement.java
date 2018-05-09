package cn.financial.quartz;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.financial.model.Message;
import cn.financial.model.Statement;
import cn.financial.model.UserRole;
import cn.financial.service.StatementService;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.service.impl.UserRoleServiceImpl;
import cn.financial.util.UuidUtil;
/**
 * 系统关账消息提醒任务
 * @author admin
 *
 */
public class QuartzStatement implements Job{
	private MessageServiceImpl messageService;
    private UserRoleServiceImpl userRoleService;
    protected Logger logger = LoggerFactory.getLogger(QuartzStatement.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		messageService = (MessageServiceImpl) AccountQuartzListener.getSpringContext().getBean("MessageServiceImpl");
		userRoleService = (UserRoleServiceImpl) AccountQuartzListener.getSpringContext().getBean("UserRoleServiceImpl");
        List<UserRole> rolelist = userRoleService.listUserRole(null);
		try {
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
					Integer i1 = messageService.saveMessage(message);
					if (i1 != 1) {
						logger.error("发送系统关账日期消息失败");
					}
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

