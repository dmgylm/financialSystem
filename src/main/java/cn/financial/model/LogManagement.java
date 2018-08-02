package cn.financial.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="LogManagement对象",description="日志管理对象")
public class LogManagement {

	@ApiModelProperty(value="日志id",name="id",example="")
	private String id;
	
	@ApiModelProperty(value="用户名",name="userName",example="")
	private String userName;
	
	@ApiModelProperty(value="服务器的IP地址",name="localAddr",example="")
	private String localAddr;
	
	@ApiModelProperty(value="客户机的IP地址",name="remoteAddr",example="")
	private String remoteAddr;
	
	@ApiModelProperty(value="用户操作的url",name="workUrl",example="")
	private String workUrl;
	
	@ApiModelProperty(value="参数",name="params",example="")
	private String params;
	
	@ApiModelProperty(value="返回状态码",name="code",example="")
	private String logCode;
	
	@ApiModelProperty(value="操作记录",name="work",example="")
	private String work;
	
	@ApiModelProperty(value="操作方法",name="method",example="")
	private String method;
	
	@ApiModelProperty(value="创建时间",name="createTime",example="")
	private String createTime; // 创建时间

    @ApiModelProperty(value="更新时间",name="updateTime",example="")
    private String updateTime; // 更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
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

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getLogCode() {
		return logCode;
	}

	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}

	public String getWorkUrl() {
		return workUrl;
	}

	public void setWorkUrl(String workUrl) {
		this.workUrl = workUrl;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	
    
    
}
