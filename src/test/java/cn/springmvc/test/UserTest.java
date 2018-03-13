package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.User;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.UuidUtil;
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
        User user = new User();
        user.setId(UuidUtil.getUUID());
        user.setName("呜呜呜呜");
        user.setRealName("凄凄切切");
        user.setPwd("555666");
        user.setJobNumber("743211124420888");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setoId("555");
        try {
            System.out.println(service.insertUser(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //修改
    @Test
    public void updateTest() {
        User user = new User();
        user.setId("2");
        user.setName("听听");
        user.setRealName("拖拖拖");
        user.setPwd("555666");
        user.setJobNumber("7432111244208");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setoId("555");
        try {
            System.out.println(service.updateUser(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteUser("2"));
    }
    //查询全部
    @Test
    public void ListUserTest() {
        List<User> user = service.listUser();
        for(User list:user){
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" pwd: "+list.getPwd() +
                    " createTime: "+formatter.format(list.getCreateTime()) +" updateTime: "+formatter.format(list.getUpdateTime()) +" oId: "+list.getoId()+"\n"); 
        }
        
    }
    //根据name,pwd查询
    @Test
    public void ListNameTest() {
        Integer flag = service.countUserName("哈哈好","555666");
        System.out.println(flag);
    }
    //根据id查询
    @Test
    public void ListByIdTest() {
        User user = service.getUserById("1ccb0f2ca6224f389cbbea57b85d4458");
        System.out.println("id: "+user.getId()+" name: "+user.getName()+" realName: "+user.getRealName()+" pwd: "+user.getPwd()+
                " createTime: "+formatter.format(user.getCreateTime())+" updateTime: "+formatter.format(user.getUpdateTime())+" oId: "+user.getoId());
    }
}
