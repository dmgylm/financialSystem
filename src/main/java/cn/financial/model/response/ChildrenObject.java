package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class ChildrenObject {
    @ApiModelProperty(value = "板块类型/是否是按钮Y表示是")
    private String orgType;
    @ApiModelProperty(value = "当前节点序号")
    private String id;
    @ApiModelProperty(value = "父节点")
    private String parentId;
    @ApiModelProperty(value = "所属板块id")
    private String orgPlateId;
    @ApiModelProperty(value = "组织架构名称")
    private String name;
    @ApiModelProperty(value = "所属板块名称/菜单栏英文名")
    private String orgkeyName;
    @ApiModelProperty(value = "子节点")
    private String children;
    @ApiModelProperty(value = "节点id")
    private String pid;
    @ApiModelProperty(value = "是否是叶子节点")
    private String leaf;
    @ApiModelProperty(value = "是否匹配  true表示匹配false表示不匹配")
    private boolean mathc;
    @ApiModelProperty(value = "跳转路径")
    private String link;
    @ApiModelProperty(value = "图片名称")
    private String icon;
    
    public String getOrgType() {
        return orgType;
    }
    public void setOrgType(String orgType) {
        this.orgType = orgType;
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
    public String getOrgPlateId() {
        return orgPlateId;
    }
    public void setOrgPlateId(String orgPlateId) {
        this.orgPlateId = orgPlateId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOrgkeyName() {
        return orgkeyName;
    }
    public void setOrgkeyName(String orgkeyName) {
        this.orgkeyName = orgkeyName;
    }
    public String getChildren() {
        return children;
    }
    public void setChildren(String children) {
        this.children = children;
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
    public boolean isMathc() {
        return mathc;
    }
    public void setMathc(boolean mathc) {
        this.mathc = mathc;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
