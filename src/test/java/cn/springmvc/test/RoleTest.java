package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Role;
import cn.financial.service.RoleService;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml" })
/**
 * 角色测试
 * @author gs
 * 2018/3/13
 */
public class RoleTest {
    @Autowired
    private RoleService service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        String roleName = "bb";
        List<Role> roleNameList = service.listRole(roleName);//根据roleName查询角色信息
        if(roleNameList.size()>0){//roleName不能重复
            System.out.println("角色名称不能重复");
            return;
        }
        Role role = new Role();
        role.setId(UuidUtil.getUUID());
        role.setRoleName(roleName);
        try {
            System.out.println(service.insertRole(role));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //修改
    @Test
    public void updateTest() {
        Role role = new Role();
        role.setId("732a2b28ea63417fbeceee1ac907fb92");
        role.setRoleName("制单员2");
        try {
            System.out.println(service.updateRole(role));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteRole("732a2b28ea63417fbeceee1ac907fb92"));
    }
    //查询全部
    @Test
    public void ListRoleTest() {
        List<Role> role = service.listRole("");
        for(Role list:role){
            System.out.println("id: "+list.getId() +" roleName: "+list.getRoleName() +
                    " createTime: "+list.getCreateTime() +" updateTime: "+list.getUpdateTime() +"\n"); 
        }
        
    }
    //根据id查询
    @Test
    public void ListByIdTest() {
        Role role = service.getRoleById("732a2b28ea63417fbeceee1ac907fb92");
        System.out.println("id: "+role.getId()+" roleName: "+role.getRoleName()+
                " createTime: "+role.getCreateTime()+" updateTime: "+role.getUpdateTime());
    }
}
