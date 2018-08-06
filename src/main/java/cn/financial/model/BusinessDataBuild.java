package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="BusinessDataBuild对象",description="损益表生成状态记录对象")
public class BusinessDataBuild {
	
	@ApiModelProperty(value="部门id",name="id",example="")
	private String id;//部门id
	
	@ApiModelProperty(value="生成状态",name="status",example="")
	private Integer status;//生成状态(0:未生成,1:已生成)
	
	@ApiModelProperty(value="板块",name="orgPlate",example="")
	private String orgPlate;//板块
	
	@ApiModelProperty(value="创建时间",name="createTime",example="")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date createTime; // 创建时间
	
	@ApiModelProperty(value="修改时间",name="updateTime",example="")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date updateTime; // 修改时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrgPlate() {
		return orgPlate;
	}

	public void setOrgPlate(String orgPlate) {
		this.orgPlate = orgPlate;
	}

}
