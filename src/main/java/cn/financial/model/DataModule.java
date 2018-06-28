package cn.financial.model;

public class DataModule {

	public final static int STATUS_NOVALID = 0;	//无效
	
	public final static int STATUS_CONSUMED = 1;	//有效
	
	/**
	 * 损益
	 */
	public final static String REPORT_TYPE_PROFIT_LOSS = "PROFIT_LOSS";

	/**
	 * 税金
	 */
	public final static String REPORT_TYPE_TAX = "TAX";

	/**
	 * 考核
	 */
	public final static String REPORT_TYPE_ASSESSMENT = "ASSESSMENT";

	/**
	 * 预算
	 */
	public final static String REPORT_TYPE_BUDGET = "BUDGET";

	/**
	 * 损益简易汇总
	 */
	public final static String REPORT_TYPE_SUMMARY_PROFIT_LOSS = "SUMMARY_PROFIT_LOSS";

	private String id;
	
	private String moduleName;
	
	private String versionNumber;
	
	private String reportType;//报表类型

	private String businessType;//业务板块

	private String moduleData;//模板数据
	
	private String founder; //创建人
	
	private Integer statue;
	
	private String startTime; //起效日期
	
	private String createTime; // 创建时间

    private String updateTime; // 更新时间
    
    public static String getReprtTypeName(String reportType){
    	String reportTypeName = null;
    	switch (reportType) {
    	case REPORT_TYPE_PROFIT_LOSS:
    		reportTypeName = "损益";
    		break;
    	case REPORT_TYPE_TAX:
    		reportTypeName = "税金";
    		break;
    	case REPORT_TYPE_ASSESSMENT:
    		reportTypeName = "考核";
    		break;
    	case REPORT_TYPE_BUDGET:
    		reportTypeName = "预算";
    		break;

		default:
			break;
		}
    	return reportTypeName;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatue() {
		return statue;
	}

	public void setStatue(Integer statue) {
		this.statue = statue;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getModuleData() {
		return moduleData;
	}

	public void setModuleData(String moduleData) {
		this.moduleData = moduleData;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
    
    
}
