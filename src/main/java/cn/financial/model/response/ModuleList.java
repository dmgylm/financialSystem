package cn.financial.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="ModuleList对象",description="配置模板列表对象")
public class ModuleList {
	
	@ApiModelProperty(value="模板id",name="id",example="")
	private String id;

	@ApiModelProperty(value="模板名称",name="moduleName",example="")
	private String moduleName;
	
    @ApiModelProperty(value="模板版本号",name="versionNumber",example="")
	private Integer versionNumber;
    
    @ApiModelProperty(value="模板状态（0：无效；1：有效）",name="statue",example="")
	private Integer statue;
    
    @ApiModelProperty(value="报表类型(PROFIT_LOSS:损益 ；TAX：税金 ；ASSESSMENT：考核 ； BUDGET：预算 ； SUMMARY_PROFIT_LOSS：损益简易汇总) ",name="reportType",example="")
    private String reportType;
    
    @ApiModelProperty(value="业务板块",name="businessType",example="")
    private String businessType;
    
    @ApiModelProperty(value="创建时间",name="createTime",example="")
	private String createTime; // 创建时间
    
    @ApiModelProperty(value="创建人",name="founder",example="")
	private String founder; //创建人

    
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

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getStatue() {
		return statue;
	}

	public void setStatue(Integer statue) {
		this.statue = statue;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
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
