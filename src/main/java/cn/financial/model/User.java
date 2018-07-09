package cn.financial.model;

import java.io.Serializable;

public class User implements Serializable{
    
    private static final long serialVersionUID = 1L; 
    
    public static final String INITIALCIPHER = "Welcome1";//初始密码

    private String id; // 用户表id

    private String name; // 用户名
    
    private String realName;//真实姓名

    private String pwd; // 密码
    
    private String jobNumber;//工号   
    
    private Integer status;//状态 

    private String createTime; // 创建时间

    private String updateTime; // 更新时间
    
    private String expreTime;//密码到期时间

    private String salt;  //uuid随机数生成
    
    public User() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getExpreTime() {
        return expreTime;
    }

    public void setExpreTime(String expreTime) {
        this.expreTime = expreTime;
    }

    public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}
