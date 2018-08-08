package cn.financial.util;


/**
 * @ClassName SiteConst 
 * @Description  常量
 * @author erle
 * @date 2014-6-16  
 * @version  1.0
 */
public abstract interface SiteConst {
	//文件保存路径
	public static final String  FILEURL=PropertiesUtils.getProperty("FILEURL");
    //资金流水模板文件保存路径
	public static final String  CAPITALEXPORT=PropertiesUtils.getProperty("CAPITALEXPORT");
	//损益表生成日(如:11),用于判断是否可以编辑当月预算
	public static final int PROFIT_LOSS_GENERATE_DAY = Integer.parseInt(PropertiesUtils.getProperty("PROFIT_LOSS_GENERATE_DAY"));
	//损益表生成定时器日期
	public static final String DAY_TIME = PropertiesUtils.getProperty("DAY_TIME");
	//开始时间
	public static final int MOVE_ORGANIZATION_START_TIME=Integer.parseInt(PropertiesUtils.getProperty("MOVE_ORGANIZATION_START_TIME"));
	//结束时间
	public static final int MOVE_ORGANIZATION_STOP_TIME=Integer.parseInt(PropertiesUtils.getProperty("MOVE_ORGANIZATION_STOP_TIME"));
}
