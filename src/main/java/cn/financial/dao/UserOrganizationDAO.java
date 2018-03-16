package cn.financial.dao;

import java.util.List;


import cn.financial.model.UserOrganization;

/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
public interface UserOrganizationDAO {
    /**
     * 查询所有
     * @return
     */
    List<UserOrganization> listUserOrganization();
    /**
     * 新增
     * @param userOrganization
     * @return
     */
    Integer insertUserOrganization(UserOrganization userOrganization);

}
