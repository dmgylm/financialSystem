package cn.financial.model;


public class Organization {

    private String id; // 组织架构表id

    private String code; // 该组织机构节点的序号，两位的，比如（01；0101，0102）

    private String parentId; // 父节点（这里指向code）

    private String orgName; // 组织架构名

    private String createTime; // 创建时间

    private String updateTime; // 更新时间

    private String uId; // 提交人id

    private Integer status; // 状态（是否已删除，1表示还存在，0表示已删除）

    private String his_permission; // 保存历史权限记录

    // private List<User> users; // 提交人id（一对多，组织结构为一）

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

    @Override
    public String toString() {
        return "Organization [id=" + id + ", code=" + code + ", parentId=" + parentId + ", orgName=" + orgName
                + ", createTime=" + createTime + ", updateTime=" + updateTime + ", uId=" + uId + ", status=" + status
                + ", his_permission=" + his_permission + "]";
    }

    // public List<User> getUsers() {
    // return users;
    // }
    //
    // public void setUsers(List<User> users) {
    // this.users = users;
    // }

}
