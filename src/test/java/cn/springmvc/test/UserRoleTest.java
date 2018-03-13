package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.UserRole;
import cn.financial.service.impl.UserRoleServiceImpl;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
/**
 * 用户角色关联表测试
 * @author gs
 * 2018/3/13
 */
public class UserRoleTest {
    @Autowired
    private UserRoleServiceImpl service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        UserRole userRole = new UserRole();
        userRole.setId(UuidUtil.getUUID());
        userRole.setrId("55555555");
        userRole.setuId("66666");
        userRole.setCreateTime(new Date());
        try {
            System.out.println(service.insertUserRole(userRole));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //查询全部
    @Test
    public void ListUserRoleTest() {
        List<UserRole> role = service.listUserRole();
        for(UserRole list:role){
            String uTime = "";
            if(list.getUpdateTime()!=null && !"".equals(list.getUpdateTime())){
                uTime = formatter.format(list.getUpdateTime());
            }
            System.out.println("id: "+list.getId() +" rId: "+list.getrId() +" uId: "+list.getuId() +
                    " createTime: "+formatter.format(list.getCreateTime()) +" updateTime: "+uTime+"\n"); 
        }
        
    }
    //根据id查询
    @Test
    public void ListByIdTest() {
        UserRole role = service.getUserRoleById("d1d0abd15383460bbf48997ed6fd581f");
        String uTime = "";
        if(role.getUpdateTime()!=null && !"".equals(role.getUpdateTime())){
          uTime = formatter.format(role.getUpdateTime());
        }
        System.out.println("id: "+role.getId()+" rId: "+role.getrId() +" uId: "+role.getuId() +
                " createTime: "+formatter.format(role.getCreateTime())+" updateTime: "+uTime);
    }
}
