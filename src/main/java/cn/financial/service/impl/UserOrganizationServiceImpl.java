package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.UserOrganizationDAO;
import cn.financial.model.UserOrganization;
import cn.financial.service.UserOrganizationService;


@Service("UserOrganizationServiceImpl")
public class UserOrganizationServiceImpl implements UserOrganizationService{
    @Autowired
    private UserOrganizationDAO userOrganizationDao;
    /**
     * 查询所有
     * @return
     */
    @Override
    public List<UserOrganization> listUserOrganization(String uId) {
        return userOrganizationDao.listUserOrganization(uId);
    }
    /**
     * 新增
     * @return
     */
    @Override
    public Integer insertUserOrganization(UserOrganization userOrganization) {
        return userOrganizationDao.insertUserOrganization(userOrganization);
    }

}
 