package cn.financial.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点，支持Ext、zTree等Web控件
 * 
 * @author zlf 2018/03/15
 * @param <T>
 *            树节点的绑定数据类
 */
public class TreeNode<T> {
    /**
     * 树节点id 为了兼容多种情况，使用String类型
     */
    private String id;

    /**
     * 树节点上级id
     */
    private String parentId;

    /**
     * 权限信息/orgType
     */
    private String orgType;
    /**
     * 模板信息
     */
    private String orgPlateId;
    private String orgkeyName;
    
    private String orgKey;
    /**
     * 树节点名称，内容和text一样 该字段主要是为了兼容Ext和zTree
     */
    private String name;
    
    private String his_permission;

    /**
     * 是否为叶子节点
     */
    private Boolean leaf = true;
    //private Boolean expanded = false;
//    private T nodeData;

    /**
     * 是否为父节点，该字段和leaf重复，主要是为了兼容Ext和zTree
     */
    //private Boolean isParent = false;
    
    /**
     * 节点的数据库id,这里用String兼容
     */
    private String pid;
    /**
     * 跳转路径
     */
    private String link;
    /**
     * 图片名称
     */
    private String icon;
    
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    
    public String getOrgkeyName() {
        return orgkeyName;
    }

    public void setOrgkeyName(String orgkeyName) {
        this.orgkeyName = orgkeyName;
    }

    /**
     * 子节点，如果没有子节点，则列表长度为0
     */
    private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();

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

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
//    public Boolean getExpanded() {
//        return expanded;
//    }
//
//    public void setExpanded(Boolean expanded) {
//        this.expanded = expanded;
//    }

    public String getHis_permission() {
        return his_permission;
    }

    public void setHis_permission(String his_permission) {
        this.his_permission = his_permission;
    }

    public String getOrgPlateId() {
        return orgPlateId;
    }

    public void setOrgPlateId(String orgPlateId) {
        this.orgPlateId = orgPlateId;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
//        this.isParent = !leaf;
    }

    public Boolean getLeaf() {
        return this.leaf;
    }

//    public Boolean getIsParent() {
//        return isParent;
//    }

    public void setIsParent(Boolean isParent) {
//        this.isParent = isParent;
        this.leaf = !isParent;
    }

//    public T getNodeData() {
//        return nodeData;
//    }
//
//    public void setNodeData(T nodeData) {
//        this.nodeData = nodeData;
//    }

    /**
     * 把树节点列表构造成树，最后返回树的根节点，如果传入的列表有多个根节点，会动态创建一个根节点。
     * 
     * @param nodes
     *            树节点列表
     * @return 根节点
     */
    public static <T> TreeNode<T> buildTree(List<TreeNode<T>> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return null;
        }

        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        // 用来存放nodes里面的顶级树节点
        // 也就是把没有父节点的节点都放到tops里面去
        List<TreeNode<T>> tops = new ArrayList<TreeNode<T>>();

        boolean hasParent = false;
        // 第一次遍历，获取一个节点作为子节点
        for (TreeNode<T> child : nodes) {
            hasParent = false;

            // 当前节点child的父节点id
            String pid = child.getParentId();

            // 如果pid不存在或为空
            // 则当前节点为顶级节点
            if (pid == null || pid.equals("")) {
                // 把当前节点添加到tops中作为顶级节点
                tops.add(child);
                // 跳过当前节点，进入下一轮
                continue;
            }

            // 遍历nodes上的所有节点，判断是否有child的父节点
            for (TreeNode<T> parent : nodes) {
                String id = parent.getId();

                // 如果parent节点的id等于child节点的pid，则parent节点是child节点的父节点
                if (id != null && id.equals(pid)) {

                    // 把child加到parent下
                    parent.getChildren().add(child);
                    parent.setLeaf(false);

                    // child节点有父节点
                    hasParent = true;

                    continue;
                }
            }

            // 如果child节点没有父节点，则child是顶级节点
            // 把child添加到tops中
            if (!hasParent) {
                tops.add(child);
            }
        }

        TreeNode<T> root;
        if (tops.size() == 1) {
            // 如果顶级节点只有一个，该顶级节点是根节点
            root = tops.get(0);
        } else {
            // 如果顶级节点有多个，创建一个根节点，把顶级节点放到根节点下
            root = new TreeNode<T>();
            root.setLeaf(false);
            root.setId("-1");
            root.setName("root");
            root.setParentId("");

            root.getChildren().addAll(tops);
        }

        return root;
    }

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}
}
