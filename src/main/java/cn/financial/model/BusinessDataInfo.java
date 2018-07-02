package cn.financial.model;
/**
 * 业务从表
 * @author admin
 *
 */
public class BusinessDataInfo {
	private String id;
	private String businessDataId; //主表id
	private String info;//内容
	private String createTime; //创建时间
    private String updateTime;//修改时间
    private String uId;//提交人
    
    
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
	
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessDataId() {
		return businessDataId;
	}
	public void setBusinessDataId(String businessDataId) {
		this.businessDataId = businessDataId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	

}
