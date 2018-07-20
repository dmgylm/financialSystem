package cn.financial.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value="user对象",description="用户对象user")
public class User implements Serializable{
    
    private static final long serialVersionUID = 1L; 
    
    public static final String INITIALCIPHER = "Welcome1";//初始密码
    
    /*密码校验规则：
     * 由6-15位字符组成，组成内容必须包含（但不仅限于）：
     * 至少6个字符（最多15个字符）、大写与小写字母、至少一个数字，支持特殊符号，但不支持空格
     * */
    public static final String REGEX = "(?!(^[A-Za-z]*$))(?!(^[0-9]*$))(?=(^.*[\\d].*$))(?=(^.*[a-z].*$))(?=(^.*[A-Z].*$))(?!(^.*[\\s].*$))^[0-9A-Za-z\\x21-\\x7e]{6,15}$";

    @ApiModelProperty(value="用户id",name="id")
    private String id; // 用户表id
    @ApiModelProperty(value="用户名",name="name")
    private String name; // 用户名
    @ApiModelProperty(value="用户名",name="realName")
    private String realName;//真实姓名
    @ApiModelProperty(value="密码",name="pwd")
    private String pwd; // 密码
    @ApiModelProperty(value="工号",name="jobNumber")
    private String jobNumber;//工号   
    @ApiModelProperty(value="状态0表示离职1表示在职",name="status")
    private Integer status;//状态 
    @ApiModelProperty(value="创建时间",name="createTime")
    private String createTime; // 创建时间
    @ApiModelProperty(value="更新时间",name="updateTime")
    private String updateTime; // 更新时间
    @ApiModelProperty(value="密码到期时间",name="expreTime")
    private String expreTime;//密码到期时间
    @ApiModelProperty(value="uuid随机数生成盐",name="salt")
    private String salt;  //uuid随机数生成
    
    
    @ApiModelProperty(value="用户关联组织架构信息1表示已匹配0表示未匹配",name="jsonOrg")
    private Integer orgFlag;
    @ApiModelProperty(value="用户关联角色信息1表示已匹配0表示未匹配",name="jsonRole")
    private Integer roleFlag;
    
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

    public Integer getOrgFlag() {
        return orgFlag;
    }

    public void setOrgFlag(Integer orgFlag) {
        this.orgFlag = orgFlag;
    }

    public Integer getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(Integer roleFlag) {
        this.roleFlag = roleFlag;
    }
	
}
