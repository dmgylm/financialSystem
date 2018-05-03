package cn.financial.service;

import java.util.List;

import cn.financial.model.UserOrganization;


public interface UserOrganizationService {
    /**
     * 查询所有
     * @return
     */
    List<UserOrganization> listUserOrganization(String uId);
    /**
     * 新增
     * @param userOrganization
     * @return
     */
    Integer insertUserOrganization(UserOrganization userOrganization);
    /**
     * 修改
     * @param uId
     * @return
     */
    Integer updateUserOrganization(String uId);
}
