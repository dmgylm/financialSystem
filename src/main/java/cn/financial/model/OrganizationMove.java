package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *组织架构移动信息记录实体类 
 * @author C.s
 */
@ApiModel(value="OrganizationMove对象",description="组织架构移动信息记录对象")
public class OrganizationMove {
	
	@ApiModelProperty(value="组织架构移动记录表id",name="id",example="")
	private String id;			//组织架构移动记录表id
	
	@ApiModelProperty(value="组织架构移动前的id",name="his_Id",example="")
	private String his_Id;		//组织架构移动前的id
	
	@ApiModelProperty(value="组织架构移动后的新id",name="new_Id",example="")
	private String new_Id;		//组织架构移动后的新id
	
	@ApiModelProperty(value="创建时间",name="createTime",example="")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;	//创建时间
	
	@ApiModelProperty(value="修改时间",name="updateTime",example="")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;	//修改时间
	
	@ApiModelProperty(value="修改人",name="modifier",example="")
	private String modifier;	//修改人
	@ApiModelProperty(value="移动后新节点的父节点id",name="newParent_Id",example="")
	private String newParent_Id;//移动后新节点的父节点id
	
	
	
	public String getNewParent_Id() {
		return newParent_Id;
	}

	public void setNewParent_Id(String newParent_Id) {
		this.newParent_Id = newParent_Id;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getHis_Id() {
		return his_Id;
	}
	
	public void setHis_Id(String his_Id) {
		this.his_Id = his_Id;
	}
	
	public String getNew_Id() {
		return new_Id;
	}
	
	public void setNew_Id(String new_Id) {
		this.new_Id = new_Id;
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
	
	public String getModifier() {
		return modifier;
	}
	
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

}
