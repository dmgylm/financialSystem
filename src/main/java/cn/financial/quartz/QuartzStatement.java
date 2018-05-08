package cn.financial.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.financial.model.Statement;
import cn.financial.service.StatementService;
import net.sf.json.JSONObject;

public class QuartzStatement implements Job{
    
    private StatementService statementService;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
	    statementService = (StatementService) AccountQuartzListener.getSpringContext().getBean("StatementServiceImpl");
	    JobDetail detail = arg0.getJobDetail();
        JSONObject object = (JSONObject) detail.getJobDataMap().get("statement");
		try {
            Statement statement = new Statement();
            statement.setId(object.getString("id"));
            statement.setoId(object.getString("oId"));//分公司id
            statement.setTypeId(object.getString("typeId"));//部门（根据部门id查分公司id）
            statement.setYear(object.getInt("year"));
            statement.setMonth(object.getInt("month"));
            statement.setCreateTime(new Date());
            statement.setStatus(object.getInt("status"));//提交状态（0 待提交   1已提交  2新增）
            statement.setDelStatus(object.getInt("delStatus"));
            Integer flag = statementService.insertStatement(statement);
            if (flag == 1) {
                System.out.println("损益报表数据新增成功");
            } else {
                System.out.println("损益报表数据新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

