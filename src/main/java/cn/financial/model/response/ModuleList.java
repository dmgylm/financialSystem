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
	private String versionNumber;
    
    @ApiModelProperty(value="模板状态（0：无效；1：有效）",name="statue",example="")
	private Integer statue;
    
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

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
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
    
    
}
