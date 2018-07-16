package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *组织架构移动信息记录实体类 
 * @author C.s
 */
public class OrganizationMove {
	
	private String id;			//组织架构移动记录表id
	
	private String his_Id;		//组织架构移动前的id
	
	private String new_Id;		//组织架构移动后的新id
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;	//创建时间
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;	//修改时间
	
	private String modifier;	//修改人
	
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
