package cn.financial.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value="查询组织结构信息",description="查询组织结构信息")
public class OrganizaList {
	
	@ApiModelProperty(value="组织架构id",name="id",example="")
	private String id;
	
	@ApiModelProperty(value="组织机构节点序号",name="code",example="")
	private String code;
	
	@ApiModelProperty(value="父节点",name="parentId",example="")
	private String parentId;
	
	@ApiModelProperty(value="组织架构名",name="orgNam",example="")
	private String orgNam;
	
	@ApiModelProperty(value="创建时间",name="createTime",example="")
	private String createTime;
	
	@ApiModelProperty(value="修改时间",name="updateTime",example="")
	private String updateTime;
	
	@ApiModelProperty(value="操作人id",name="uId",example="")
	private String uId;
	
	@ApiModelProperty(value="状态(1:存在;0:删除)",name="status",example="")
	private String status;
	
	@ApiModelProperty(value="历史记录数",name="his_permission",example="")
	private String his_permission;
	
	@ApiModelProperty(value="和模版对应的一个唯一值",name="orgkey",example="")
	private String orgkey;
	
	@ApiModelProperty(value="类别(1:汇总,2:公司,3:部门,4:表示板块)",name="orgType",example="")
	private String orgType;
	
	@ApiModelProperty(value="所属板块id",name="orgPlateId",example="")
	private String orgPlateId;
	
	
}
