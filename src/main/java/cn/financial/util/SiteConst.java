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
}
