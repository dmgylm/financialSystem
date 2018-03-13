package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Role;
import cn.financial.service.impl.RoleServiceImpl;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
/**
 * 角色测试
 * @author gs
 * 2018/3/13
 */
public class RoleTest {
    @Autowired
    private RoleServiceImpl service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        Role role = new Role();
        role.setId(UuidUtil.getUUID());
        role.setRoleName("cccc");
        role.setCreateTime(new Date());
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
        role.setRoleName("aaaaa");
        role.setUpdateTime(new Date());
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
        List<Role> role = service.listRole();
        for(Role list:role){
            String uTime = "";
            if(list.getUpdateTime()!=null && !"".equals(list.getUpdateTime())){
                uTime = formatter.format(list.getUpdateTime());
            }
            System.out.println("id: "+list.getId() +" roleName: "+list.getRoleName() +
                    " createTime: "+formatter.format(list.getCreateTime()) +" updateTime: "+uTime +"\n"); 
        }
        
    }
    //根据id查询
    @Test
    public void ListByIdTest() {
        Role role = service.getRoleById("21b4e7dd874040d9afcc5256442031ef");
        String uTime = "";
        if(role.getUpdateTime()!=null && !"".equals(role.getUpdateTime())){
            uTime = formatter.format(role.getUpdateTime());
          }
        System.out.println("id: "+role.getId()+" roleName: "+role.getRoleName()+
                " createTime: "+formatter.format(role.getCreateTime())+" updateTime: "+uTime);
    }
}
