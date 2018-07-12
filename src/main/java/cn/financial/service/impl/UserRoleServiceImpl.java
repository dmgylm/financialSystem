package cn.financial.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.UserRoleDAO;
import cn.financial.model.UserRole;
import cn.financial.service.UserRoleService;
import cn.financial.util.UuidUtil;


@Service("UserRoleServiceImpl")
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRoleDAO userRoleDao;
    /**
     * 查询所有
     * @return
     */
    @Override
    public List<UserRole> listUserRole(String name) {
        if(name == null || name.equals("")){
            return null;
        }
        return userRoleDao.listUserRole(name);
    }
    /**
     * 新增
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer insertUserRole(UserRole user) {
        if(user.getrId() == null || user.getrId().equals("")){//roleId前台传入的数据是JSON格式
            return -1;
        }
        if(user.getuId() == null || user.getuId().equals("")){//用户id
            return -2;
        }
        JSONArray sArray = JSON.parseArray(user.getrId());
        int userRoleList = 0;
        UserRole userRole = null; 
        for (int i = 0; i < sArray.size(); i++) {
            JSONObject object = (JSONObject) sArray.get(i);
            String roleStr =(String)object.get("roleId");//获取key-roleId键值
            System.out.println("roleId:==="+roleStr);
            if(roleStr!=null && !"".equals(roleStr)){
                userRole = new UserRole();
                userRole.setId(UuidUtil.getUUID());
                userRole.setrId(roleStr);
                userRole.setuId(user.getuId());
                userRoleList = userRoleDao.insertUserRole(userRole);
            }
        }
        return userRoleList;
    }
    /**
     * 删除
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer deleteUserRole(String uId) {
        return userRoleDao.deleteUserRole(uId);
    }
    /**
     * 修改
     * @return
     */
    @Override
    @CacheEvict(value = "find_Roles_Permissions", allEntries = true)
    public Integer updateUserRole(UserRole user) {
        if(user.getrId() == null || user.getrId().equals("")){//roleId前台传入的数据是JSON格式
            return -1;
        }
        if(user.getuId() == null || user.getuId().equals("")){
            return -2;
        }
        int userRoleList = 0;
        int userRoleDelete = userRoleDao.deleteUserRole(user.getuId());//删除
        if(userRoleDelete > 0){
            JSONArray sArray = JSON.parseArray(user.getrId());
            UserRole userRole = null; 
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String roleStr =(String)object.get("roleId");//获取key-roleId键值
                System.out.println("roleId:==="+roleStr);
                if(roleStr!=null && !"".equals(roleStr)){
                    userRole = new UserRole();
                    userRole.setId(UuidUtil.getUUID());
                    userRole.setrId(roleStr);
                    userRole.setuId(user.getuId());
                    userRoleList = userRoleDao.updateUserRole(userRole);//修改（新增数据）
                }
            }  
        }else{
            return -3;
        }
        return userRoleList;
    }
}
 