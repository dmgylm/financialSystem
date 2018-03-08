package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.service.impl.UserServiceImpl;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:conf/spring-test.xml")
/**
 * 用户测试
 * @author gs
 * 2018/3/8
 */
public class userTest {
    @Autowired
    private UserServiceImpl service;
    //新增
    @Test
    void insertTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String cTime="2018-03-05";
        String uTime="2018-03-07";
        try {
            System.out.println(service.insertUser("a", "b", 2, formatter.parse(cTime), formatter.parse(uTime), "3"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    //修改
    @Test
    void updateTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String cTime="2018-03-06";
        String uTime="2018-03-08";
        try {
            System.out.println(service.updateUser("1ccb0f2ca6224f389cbbea57b85d4458", "aa11", "13233", 2, formatter.parse(cTime), formatter.parse(uTime), "4"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteUser("1"));
    }
    //查询全部
    @Test
    public void ListUserTest() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<User> user = service.listUser();
        for(User list:user){
            System.out.println("id: "+list.getId() +"name: "+list.getName() +"pwd: "+list.getPwd() +"privilege: "+list.getPrivilege() +
                    "createTime: "+formatter.format(list.getCreateTime()) +"updateTime: "+formatter.format(list.getUpdateTime()) +"oId: "+list.getoId()+"\n"); 
        }
        
    }
    //根据name查询
    @Test
    public void ListNameTest() {
        int flag = service.listUserById("a");
        System.out.println(flag);
    }
}
