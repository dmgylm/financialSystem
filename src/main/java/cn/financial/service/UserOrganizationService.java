package cn.financial.service;

import java.util.List;

import cn.financial.model.UserOrganization;


public interface UserOrganizationService {
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
