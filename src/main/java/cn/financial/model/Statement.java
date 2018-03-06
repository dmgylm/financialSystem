package cn.financial.model;

import java.util.Date;

public class Statement {
	
	 private int id;   //损益表id
	 
	 private String oId; //组织id
	 
	 private String info;  //内容
	 
	 private Date createTime; //创建时间
	 
	 private Date updateTime; //更新时间
	 
	 private String typeId;    //版块Id(来源于组织结构表）
	 
	 private String  uId;    //提交人id
	
     private int year;   //年份
	 
	 private int month; //月份

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getoId() {
		return oId;
	}

	public void setoId(String oId) {
		this.oId = oId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	 
}
