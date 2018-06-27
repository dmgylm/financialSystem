package cn.springmvc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})
/**
 * 用户测试
 * @author gs
 * 2018/3/8
 */
public class UserTest {
    @Autowired
    private UserService service;
    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //登录测试方法
    @Test
    public void login(){  
        //构建SecurityManager环境
        //DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //defaultSecurityManager.setRealm(realms);
        //主体提交认证请求
        //SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject  = SecurityUtils.getSubject();//获得主体
        UsernamePasswordToken token = new UsernamePasswordToken("aa","...5aA");//通过自定义UserRealm进行认证（继承AuthorizingRealm）
        subject.login(token);
        System.out.println("isAnthenticated:"+subject.isAuthenticated());//是否认证(true为登录成功)
        subject.isAuthenticated();
    }
    //新增
    @Test
    public void insertTest() {
        User user = new User();
        user.setId(UuidUtil.getUUID());
        user.setSalt(UuidUtil.getUUID());
        user.setName("gg");
        user.setRealName("3333");      
        user.setPwd("Welcome1");
        user.setJobNumber("743211124420888999");
        try {
            Integer flag = service.countUserName("gg","");//查询用户名是否存在(真实姓名可以重复)
            if(flag>0){
                System.out.println("用户名不能重复");
                return;
            }
            System.out.println(service.insertUser(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //修改
    @Test
    public void updateTest() {
        User user = new User();
        user.setId("56ffb0e9e4c549718e01eb2d18e466ca");
        user.setName("hhhh");
        user.setRealName("啦啦啦");
        user.setPwd("4444");
        user.setSalt(UuidUtil.getUUID());
        //user.setJobNumber("7432111244208");
        try {
            System.out.println(service.updateUser(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteUser("1ec2ae3b2c2743f6a6a7c428c2550199"));
    }
    //查询全部or多条件查询用户列表
    @Test
    public void ListUserTest() {
        int page = 1;
        int pageSize = 5;
        page = pageSize * (page - 1);
        Map<Object, Object> map = new HashMap<Object, Object>();
        //map.put("name","恩恩");
        //map.put("realName", "小芳");
        //map.put("jobNumber", "7432111244208");
        //map.put("pwd", "555666");
        map.put("status", 1);
        map.put("pageSize", pageSize);//条数
        map.put("start", page);//页码
        map.put("createTime", "2018-03-20 16:00:00");
        //map.put("updateTime", "");
        List<User> user = service.listUser(map);
        for(User list:user){
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" pwd: "+list.getPwd() +
                    " realName: "+list.getRealName()+" jobNumber: "+list.getJobNumber()+" status: "+list.getStatus()+
                    " createTime: "+list.getCreateTime() +" updateTime: "+list.getUpdateTime() +"\n"); 
        }
        
    }
    //根据name,jobNumber查询
    @Test
    public void ListNameTest() {
        Integer flag = service.countUserName("","74321");
        System.out.println(flag);
    }
    //根据id查询
    @Test
    public void ListByIdTest() {
        User user = service.getUserById("1cb54fff435b4fff8aa7c1fa391f519b");
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
