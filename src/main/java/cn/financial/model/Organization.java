package cn.financial.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

/**
 * 组织架构实体类
 * 
 * @author zlf 2018/01/13
 *
 */
public class Organization implements Serializable {
	@ApiModelProperty(value="组织架构id",name="id",example="")
    private String id; // 组织架构表id
	@ApiModelProperty(value="组织机构节点的序号",name="code",example="")
    private String code; // 该组织机构节点的序号，两位的，比如（01；0101，0102）
	@ApiModelProperty(value="父节点",name="parentId",example="")
    private String parentId; // 父节点（这里指向code）
	@ApiModelProperty(value="组织架构名",name="orgName",example="")
    private String orgName; // 组织架构名
	@ApiModelProperty(value="创建时间",name="createTime",example="")
    private String createTime; // 创建时间
	@ApiModelProperty(value="更新时间",name="updateTime",example="")
    private String updateTime; // 更新时间
	@ApiModelProperty(value="提交人",name="uId",example="")
    private String uId; // 提交人id
	@ApiModelProperty(value="状态（是否已删除，1表示还存在，0表示已删除",name="status",example="")
    private Integer status; // 状态（是否已删除，1表示还存在，0表示已删除）
	@ApiModelProperty(value="保存历史权限记录",name="his_permission",example="")
    private String his_permission; // 保存历史权限记录
	@ApiModelProperty(value="和模版对应的一个唯一值",name="orgkey",example="")
    private String orgkey;// 和模版对应的一个唯一值
	@ApiModelProperty(value="1：汇总，2：公司，3：部门 ，4：板块(默认是汇总)",name="orgType",example="")
    private Integer orgType;// 1：汇总，2：公司，3：部门 ，4：板块(默认是汇总)
	@ApiModelProperty(value="orgPlateId",name="板块id",example="")
    private String orgPlateId;// 板块id
	@Transient
	private String oId;//组织id
	
    // private List<User> users; // 提交人id（一对多，组织结构为一）

    public String getoId() {
		return oId;
	}

	public void setoId(String oId) {
		this.oId = oId;
	}

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHis_permission() {
        return his_permission;
    }

    public void setHis_permission(String his_permission) {
        this.his_permission = his_permission;
    }

    public String getOrgkey() {
        return orgkey;
    }

    public void setOrgkey(String orgkey) {
        this.orgkey = orgkey;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public String getOrgPlateId() {
        return orgPlateId;
    }

    public void setOrgPlateId(String orgPlateId) {
        this.orgPlateId = orgPlateId;
    }

    @Override
    public String toString() {
        return "Organization [id=" + id + ", code=" + code + ", parentId=" + parentId + ", orgName=" + orgName
                + ", createTime=" + createTime + ", updateTime=" + updateTime + ", uId=" + uId + ", status=" + status
                + ", his_permission=" + his_permission + ", orgkey=" + orgkey + ", orgType=" + orgType + ", orgPlateId="
                + orgPlateId + "]";
    }

    // public List<User> getUsers() {
    // return users;
    // }
    //
    // public void setUsers(List<User> users) {
    // this.users = users;
    // }

}
