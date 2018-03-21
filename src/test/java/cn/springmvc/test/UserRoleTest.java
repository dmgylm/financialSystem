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
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml"})
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
        userRole.setrId("21b4e7dd874040d9afcc5256442031ef");
        userRole.setuId("2ad46022ada048f1aedd6dc67cf8ac74");
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
        List<UserRole> role = service.listUserRole("rr");
        for(UserRole list:role){
            System.out.println(" rId: "+list.getrId() +" uId: "+list.getuId() +" roleName: "+list.getRoleName()+
            " jobNumber: "+list.getJobNumber()+" userName:"+list.getName()+" realName: "+list.getRealName()+
            " createTime: "+formatter.format(list.getCreateTime()) +"\n");
        }
        
    }
}
