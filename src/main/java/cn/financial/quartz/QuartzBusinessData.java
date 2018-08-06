package cn.financial.quartz;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.financial.model.BusinessDataBuild;
import cn.financial.model.Organization;
import cn.financial.service.impl.BusinessDataBuildServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;

public class QuartzBusinessData implements Job{
	
	private OrganizationServiceImpl oImpl;
	
	private BusinessDataBuildServiceImpl buildServiceImpl;
	
	protected Logger logger = LoggerFactory.getLogger(QuartzBusinessData.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		long start = System.currentTimeMillis();
		
		buildServiceImpl = (BusinessDataBuildServiceImpl) AccountQuartzListener.getSpringContext().getBean("BusinessDataBuildServiceImpl");
		oImpl = (OrganizationServiceImpl) AccountQuartzListener.getSpringContext().getBean("OrganizationServiceImpl");
		
		System.out.println("启动损益状态生成记录任务~~~~");
		
		buildServiceImpl.deleteBusinessDataBuild();
		
		System.out.println("清空表完成~~~~");
		
		List<Organization> list = oImpl.getDep();
		
		for(int i=0; i<list.size(); i++) {
			
				BusinessDataBuild bdb = new BusinessDataBuild();
				bdb.setId(list.get(i).getId());
				bdb.setStatus(0);
				bdb.setOrgPlate(list.get(i).getOrgPlateId());
				bdb.setOrgName(list.get(i).getOrgName());
				buildServiceImpl.saveBusinessDataBuild(bdb);
			
		}
		
		System.out.println("新增表完成~~~~");
		
		long end = System.currentTimeMillis();
		
		System.out.println("时间花费~~~~");
		System.out.println(end - start);
				
	}
	
}
