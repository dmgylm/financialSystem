package cn.financial.model;

import java.util.Date;
import java.util.List;

public class Organization {
	
    	 private String id;   //组织架构表id
    	 
    	 private String parentId; //父id
    	 
    	 private String orgName;  //组织架构名
    	 
    	 private Date createTime; //创建时间
    	 
    	 private Date updateTime; //更新时间
    	 
    	 private List<User> uId;        //提交人id
    	 
    	 
    
    	public String getId() {
    		return id;
    	}
    
    	public void setId(String id) {
    		this.id = id;
    	}
    
    	public String getParentId() {
    		return parentId;
    	}
    
    	public void setParentId(String parentId) {
    		this.parentId = parentId;
    	}
    
    	public String getOrgName() {
    		return orgName;
    	}
    
    	public void setOrgName(String orgName) {
    		this.orgName = orgName;
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
    
    	public String getuId() {
    		return uId;
    	}
    
    	public void setuId(String uId) {
    		this.uId = uId;
    	}
    
    	   
}
