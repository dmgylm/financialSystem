package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class OrganizaResult {
	 @ApiModelProperty(value = "用户组织架构信息")
	 private List<ChildrenObject> children;
	 @ApiModelProperty(value="组织架构code",name="id",example="")
	 private String id;
	 
	 @ApiModelProperty(value = "类别（1:汇总，2:公司，3:部门,4:板块）",name="orgType",example="")
	 private String orgType;
	 
	 @ApiModelProperty(value = "父节点（这里指向code）",name="parentId",example="")
	 private String parentId;
	 
	 @ApiModelProperty(value = "板块id",name="orgPlateId",example="")
	 private String orgPlateId;
	 
	 @ApiModelProperty(value = "机构名称",name="orgName",example="")
	 private String orgName;
	 
	 @ApiModelProperty(value = "所属板块名称",name="orgkeyName",example="")
	 private String orgkeyName;
	 
	 @ApiModelProperty(value = "节点id",name="pid",example="")
	 private String pid;
	 
	 @ApiModelProperty(value = "是否是叶子节点", name="left",example="")
	 private String leaf;
	
	public List<ChildrenObject> getChildren() {
		return children;
	}
	public void setChildren(List<ChildrenObject> children) {
		this.children = children;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getOrgPlateId() {
		return orgPlateId;
	}
	public void setOrgPlateId(String orgPlateId) {
		this.orgPlateId = orgPlateId;
	}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgkeyName() {
		return orgkeyName;
	}
	public void setOrgkeyName(String orgkeyName) {
		this.orgkeyName = orgkeyName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	 
	 
}
