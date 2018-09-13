package cn.financial.util;

public interface FinanceConfig {
	//资金流水最大编辑次数
	public static final Integer  EDITORCOUNT_MAX=PropertiesUtils.getProperty("EDITORCOUNT_MAX")==null?10:Integer.parseInt(PropertiesUtils.getProperty("EDITORCOUNT_MAX"));

}
