package cn.financial.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="DataModule对象",description="配置模板对象")
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

    @ApiModelProperty(value="模板id",name="id",example="")
	private String id;
	
    @ApiModelProperty(value="",name="moduleKey",example="")
	private String moduleKey;
	
    @ApiModelProperty(value="模板名称",name="moduleName",example="")
	private String moduleName;
	
    @ApiModelProperty(value="模板版本号",name="versionNumber",example="")
	private String versionNumber;
	
    @ApiModelProperty(value="报表类型",name="reportType",example="")
	private String reportType;//报表类型

    @ApiModelProperty(value="业务板块",name="businessType",example="")
	private String businessType;//业务板块

    @ApiModelProperty(value="模板数据",name="moduleData",example="")
	private String moduleData;//模板数据
	
    @ApiModelProperty(value="创建人",name="founder",example="")
	private String founder; //创建人
	
    @ApiModelProperty(value="模板状态（0：无效；1：有效）",name="statue",example="")
	private Integer statue;
	
    /*@ApiModelProperty(value="起效日期",name="startTime",example="")
	private String startTime; //起效日期
*/	
    @ApiModelProperty(value="创建时间",name="createTime",example="")
	private String createTime; // 创建时间

    @ApiModelProperty(value="更新时间",name="updateTime",example="")
    private String updateTime; // 更新时间
    
    @ApiModelProperty(value="模板数据转换成的html",name="dataHtml",example="")
    private String dataHtml; //模板数据转换成的html
    
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

	public String getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
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

	/*public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}*/

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

	public String getDataHtml() {
		return dataHtml;
	}

	public void setDataHtml(String dataHtml) {
		this.dataHtml = dataHtml;
	}
    
    
}
