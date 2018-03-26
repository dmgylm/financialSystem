package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.User;
import cn.financial.service.impl.UserServiceImpl;
import cn.financial.util.PasswordHelper;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml"})
/**
 * 用户测试
 * @author gs
 * 2018/3/8
 */
public class UserTest {
    @Autowired
    private UserServiceImpl service;
    
    @Autowired
    private PasswordHelper PasswordHelper;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        User user = new User();
        user.setId(UuidUtil.getUUID());
        user.setName("admin");
        user.setRealName("ssss");      
        user.setPwd("admin");
        user.setJobNumber("743211124420888");
        user.setCreateTime("2018/3/26");
        //加密密码 
        PasswordHelper.encryptPassword(user);
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
        user.setId("1ec2ae3b2c2743f6a6a7c428c2550199");
        //user.setName("gggggggggggggggg");
        user.setRealName("拖拖拖");
        user.setPwd("3333");
        //user.setJobNumber("7432111244208");
        user.setUpdateTime("2018/3/25");
        //加密密码
        PasswordHelper.encryptPassword(user);
        try {
            System.out.println(service.updateUser(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteUser("2bea324762f74d968d62b6837349dfe0"));
    }
    //查询全部or多条件查询用户列表
    @Test
    public void ListUserTest() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        //map.put("name","恩恩");
        //map.put("realName", "小芳");
        //map.put("jobNumber", "7432111244208");
        //map.put("pwd", "555666");
        map.put("createTime", "2018-03-06");
        //map.put("updateTime", "");
        List<User> user = service.listUser(map);
        for(User list:user){
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" pwd: "+list.getPwd() +
                    " realName: "+list.getRealName()+" jobNumber: "+list.getJobNumber()+" status: "+list.getStatus()+
                    " createTime: "+list.getCreateTime() +" updateTime: "+list.getUpdateTime()+"\n"); 
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
                " createTime: "+user.getCreateTime()+" updateTime: "+user.getUpdateTime());
    }
    //根据name查询
    @Test
    public void ListByNameTest() {
    	User user = service.getUserByName("55");
    	System.out.println("id: "+user.getId()+" name: "+user.getName()+" realName: "+user.getRealName()+" pwd: "+user.getPwd()+
                " createTime: "+user.getCreateTime()+" updateTime: "+user.getUpdateTime());
    }
}
