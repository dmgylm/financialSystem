package cn.financial.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.financial.model.Budget;
import cn.financial.service.BudgetService;
import net.sf.json.JSONObject;

public class QuartzBudget implements Job{
    
    private BudgetService budgetService;
    
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
	    budgetService = (BudgetService) AccountQuartzListener.getSpringContext().getBean("BudgetServiceImpl");
	    JobDetail detail = arg0.getJobDetail();
        JSONObject object = (JSONObject) detail.getJobDataMap().get("budget");
	    try {
            Budget budget = new Budget();
            budget.setId(object.getString("id"));
            budget.setoId(object.getString("oId"));//分公司id
            budget.setTypeId(object.getString("typeId"));//部门（根据部门id查分公司id）
            budget.setCreateTime(new Date());
            budget.setStatus(object.getInt("status"));//提交状态（0 待提交   1已提交  2新增）
            budget.setDelStatus(object.getInt("delStatus"));
            budget.setYear(object.getInt("year"));
            budget.setMonth(object.getInt("month"));
            Integer flag = budgetService.insertBudget(budget);
            if (flag == 1) {
               System.out.println("预算报表数据新增成功");
            } else {
                System.out.println("预算报表数据新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

