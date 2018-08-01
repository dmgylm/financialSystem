package cn.financial.service.impl;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.RoleDAO;
import cn.financial.model.Role;
import cn.financial.service.RoleService;


@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDAO roleDao;
    /**
     * 查询全部角色/根据roleName查询
     */
    @Override
    public List<Role> listRole(String roleName, String rName) {
        return roleDao.listRole(roleName, rName);
    }
    /**
     * 根据roleId查询角色
     */
    @Override
    public Role getRoleById(String roleId) {
        if(roleId == null || roleId.equals("")){
            return null;
        }
        return roleDao.getRoleById(roleId);
    }
    /**
     * 新增角色
     */
    @Override
    public Integer insertRole(Role role) {
        return roleDao.insertRole(role);
    }
    /**
     * 修改角色
     */
    @Override
    public Integer updateRole(Role role) {
        if(role.getId() == null || role.getId().equals("")){
            return -1;
        }
        return roleDao.updateRole(role);
    }
    /**
     * 删除角色
     */
    @Override
    public Integer deleteRole(String roleId) {
        if(roleId == null || roleId.equals("")){
            return -1;
        }
        return roleDao.deleteRole(roleId);
    }
    
    public static void addRoleType(JSONObject json, HashSet<Object> twoList){
        //System.out.println("--------"+twoList);
        //循环匹配
        json.put("mathc", false);
        if(twoList.contains(json.getString("pid"))){
            json.put("mathc", true);
        }
        if(json.containsKey("children") && CollectionUtils.isNotEmpty(json.getJSONArray("children"))){
            for (Object item : json.getJSONArray("children")) {
                JSONObject  itemChildren = (JSONObject)JSONObject.toJSON(item);
                for (Object itemStr : twoList) {
                    itemChildren.put("mathc", false);
                    //System.out.println("------"+itemStr+"========="+itemChildren.getString("pid"));
                   if(itemChildren.getString("pid").equals(itemStr)){
                       itemChildren.put("mathc", true);
                       break;
                   }
                }
                addRoleType(itemChildren,twoList);
            }
        }
    } 
}
 