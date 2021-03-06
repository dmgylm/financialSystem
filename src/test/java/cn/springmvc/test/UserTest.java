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
import org.springframework.test.context.web.WebAppConfiguration;

import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.util.HttpClient3;
import cn.financial.util.UuidUtil;
import cn.financial.util.shiro.PasswordHelper;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
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
    HttpClient3 http = new HttpClient3();
    @Autowired
    private PasswordHelper passwordHelper;
    //登录测试方法
    @Test
    public void login(){  
        //构建SecurityManager环境
        //DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //defaultSecurityManager.setRealm(realms);
        //主体提交认证请求
        //SecurityUtils.setSecurityManager(defaultSecurityManager);
        /*Subject subject  = SecurityUtils.getSubject();//获得主体
        UsernamePasswordToken token = new UsernamePasswordToken("admin1","abcABC123");//通过自定义UserRealm进行认证（继承AuthorizingRealm）
        subject.login(token);
        System.out.println("isAnthenticated:"+subject.isAuthenticated());//是否认证(true为登录成功)
        subject.isAuthenticated();*/
        String url = "http://192.168.113.135:8080/financialSys/login";
        HashMap<String, String> params = new HashMap<>();
        /*params.put("userId", "3ab47227d7ec441aad625e76c32b46b7");*/
        params.put("username", "admin1");
        params.put("password", "12345aA");
        try {
            HttpClient3 https=new HttpClient3();
            System.out.println("result="+https.doPost(url, params)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void pwdTest() {
        /*User userPwd = new User();
        userPwd.setPwd("abcABC123");
        userPwd.setSalt("06a9f1b7e86c44f6a35f2e0bf8d2b35c");
        passwordHelper.encryptPassword(userPwd);
        String oldPwd = userPwd.getPwd();//旧密码加密(页面传入)
         */        
        String url = "http://192.168.113.135:8080/financialSys/user/passWord?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37";
        HashMap<String, String> params = new HashMap<>();
        /*params.put("userId", "3ab47227d7ec441aad625e76c32b46b7");*/
        params.put("oldPwd", "12345aA");
        params.put("newPwd", "12345aAbb");
        try {
            HttpClient3 https=new HttpClient3();
           System.out.println("result="+https.doPost(url, params)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //新增
    @Test
    public void insertTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/insert?meta.session.id=37be1247-d113-4ee5-9745-402cf1f91bc0", "name=cc&realName=222&jobNumber=23141344323");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*User user = new User();
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
        }*/
    }
    //修改
    @Test
    public void updateTest() {
        /*User user = new User();
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
        }*/
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/update?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "userId=56ffb0e9e4c549718e01eb2d18e466ca&name=333&realName=222&jobNumber=23141344323");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        //System.out.println(service.deleteUser("1ec2ae3b2c2743f6a6a7c428c2550199"));
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/delete?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "userId=56ffb0e9e4c549718e01eb2d18e466ca");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查询全部or多条件查询用户列表
    @Test
    public void ListUserTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/index?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "page=1&pageSize=1");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*int page = 1;
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
        }*/
        
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
        /*User user = service.getUserById("1cb54fff435b4fff8aa7c1fa391f519b");
        System.out.println("id: "+user.getId()+" name: "+user.getName()+" realName: "+user.getRealName()+" pwd: "+user.getPwd()+
                " createTime: "+user.getCreateTime()+" updateTime: "+user.getUpdateTime());*/
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/userById?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "userId=56ffb0e9e4c549718e01eb2d18e466ca");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据name查询
    @Test
    public void ListByNameTest() {
    	User user = service.getUserByName("55");
    	System.out.println("id: "+user.getId()+" name: "+user.getName()+" realName: "+user.getRealName()+" pwd: "+user.getPwd()+
                " createTime: "+user.getCreateTime()+" updateTime: "+user.getUpdateTime());
    }
}
