package cn.financial.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	List<UserOrganization> listUserOrganization(@Param("uId") String uId,@Param("oId")String oId);
	
	/**
     * 根据uId查询组织架构的最高节点
     * @return
     */
    UserOrganization maxOrganizations(@Param("uId") String uId);
	
    /**
     * 新增
     * @param userOrganization
     * @return
     */
    Integer insertUserOrganization(UserOrganization userOrganization);
    /**
     * 删除
     * @param uId
     * @return
     */
    Integer deleteUserOrganization(@Param("uId") String uId);
    /**
     * 修改
     * @param userOrganization
     * @return
     */
    Integer updateUserOrganization(UserOrganization userOrganization);
	List<UserOrganization> listUserOrganizations(@Param("sId") String sId);
}
