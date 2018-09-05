package cn.financial.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Organization;
import cn.financial.model.UserOrganization;


public interface UserOrganizationService {
    /**
     * 查询所有
     * @return
     */
    List<UserOrganization> listUserOrganization(String uId,String oId);
    
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
    Integer deleteUserOrganization(String uId);
    /**
     * 修改
     * @param userOrganization
     * @return
     */
    Integer updateUserOrganization(UserOrganization userOrganization); 
    /**
     * 根据用户id查询该组织架构节点下的所有子节点,构建成树
     * @param uId
     * @return
     */
    List<JSONObject> userOrganizationList(String uId);
    
}
