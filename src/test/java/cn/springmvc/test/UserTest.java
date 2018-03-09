package cn.springmvc.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.User;
import cn.financial.service.impl.UserServiceImpl;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
/**
 * 用户测试
 * @author gs
 * 2018/3/8
 */
public class UserTest {
    @Autowired
    private UserServiceImpl service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String cTime="2018-03-05";
        String uTime="2018-03-07";
        try {
            System.out.println(service.insertUser("eee", "ccc", "b", 2, formatter.parse(cTime), formatter.parse(uTime), "355"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    //修改
    @Test
    public void updateTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String cTime="2018-03-06";
        String uTime="2018-03-08";
        try {
            System.out.println(service.updateUser("a6c920ed8c8a4d149869d19088945205", "1", "55", "", 2, formatter.parse(cTime), formatter.parse(uTime), ""));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        //System.out.println(service.deleteUser("33333"));
        System.out.println(service.deleteUser("2"));
    }
    //查询全部
    @Test
    public void ListUserTest() {
        List<User> user = service.listUser();
        for(User list:user){
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" pwd: "+list.getPwd() +" privilege: "+list.getPrivilege() +
                    " createTime: "+formatter.format(list.getCreateTime()) +" updateTime: "+formatter.format(list.getUpdateTime()) +" oId: "+list.getoId()+"\n"); 
        }
        
    }
    //根据name查询
    @Test
    public void ListNameTest() {
        Integer flag = service.countUserName("a");
        System.out.println(flag);
    }
    //根据id查询
    @Test
    public void ListByIdTest() {
        User user = service.getUserById("1ccb0f2ca6224f389cbbea57b85d4458");
        System.out.println("id: "+user.getId()+" name: "+user.getName()+" realName: "+user.getRealName()+" pwd: "+user.getPwd()+" privilege: "+user.getPrivilege()+
                " createTime: "+formatter.format(user.getCreateTime())+" updateTime: "+formatter.format(user.getUpdateTime())+" oId: "+user.getoId());
    }
}
