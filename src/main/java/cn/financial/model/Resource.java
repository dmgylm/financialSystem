package cn.financial.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资源权限表
 * @author gs
 * 2018/3/15
 */
@ApiModel(value="Resource对象",description="角色对象Resource")
public class Resource {
    @ApiModelProperty(value="资源权限id",name="id")
    private String id;//资源权限id
    @ApiModelProperty(value="资源权限名称",name="name")
    private String name;//资源权限名称
    @ApiModelProperty(value="序号,对应功能权限id",name="code")
    private Integer code;//自增长的数字，用于选择父id
    @ApiModelProperty(value="路径",name="url")
    private String url;//路径
    @ApiModelProperty(value="父节点",name="parentId")
    private String parentId;//父节点
    @ApiModelProperty(value="权限",name="permssion")
    private String permssion;//权限
    @ApiModelProperty(value="删除状态0表示删除1表示存在",name="status")
    private Integer status;//删除状态0表示删除1表示存在
    @ApiModelProperty(value="创建时间",name="createTime")
    private String createTime;//创建时间
    @ApiModelProperty(value="更新时间",name="updateTime")
    private String updateTime;//修改时间
 
    public Resource() {
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPermssion() {
        return permssion;
    }

    public void setPermssion(String permssion) {
        this.permssion = permssion;
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
    
}
