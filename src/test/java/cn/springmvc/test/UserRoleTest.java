package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.UserRole;
import cn.financial.service.UserRoleService;
import cn.financial.util.HttpClient3;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml"})
/**
 * 用户角色关联表测试
 * @author gs
 * 2018/3/13
 */
public class UserRoleTest {
    @Autowired
    private UserRoleService service;
    
    HttpClient3 http = new HttpClient3();
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/userRoleInsert?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "roleId=[{\"roleId\":\"8686c94dd9dc4d3e980a7799da6f4819\"}]&uId=3ab47227d7ec441aad625e76c32b46b7");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*String roleIdStr = "[{\"roleId\":\"8686c94dd9dc4d3e980a7799da6f4819\"}]";
        JSONArray sArray = JSON.parseArray(roleIdStr);
        UserRole userRole = null;
        int number = 0;
        for (int i = 0; i < sArray.size(); i++) {
            JSONObject object = (JSONObject) sArray.get(i);
            String roleId =(String)object.get("roleId");
            System.out.println("roleId:==="+roleId);
            if(roleId!=null && !"".equals(roleId)){
                    userRole = new UserRole();
                    userRole.setId(UuidUtil.getUUID());
                    userRole.setrId(roleId);
                    userRole.setuId("3ab47227d7ec441aad625e76c32b46b7");
                    number = service.insertUserRole(userRole);
            }
        }
        try {
            System.out.println(number);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    //查询全部
    @Test
    public void ListUserRoleTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/userRoleIndex?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*List<UserRole> role = service.listUserRole("");
        for(UserRole list:role){
            System.out.println(" rId: "+list.getrId() +" uId: "+list.getuId() +" roleName: "+list.getRoleName()+
            " jobNumber: "+list.getJobNumber()+" userName:"+list.getName()+" realName: "+list.getRealName()+
            " createTime: "+list.getCreateTime() +"\n");
        }*/
        
    }
    //修改（根据用户id修改用户角色关联信息）
    @Test
    public void updateUserRoleTest(){
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/user/userRoleUpdate?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "roleId=[{\"roleId\":\"7136b0ba498f465e975705add4643ba3\"}]&uId=56ffb0e9e4c549718e01eb2d18e466ca");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*int updateNumber = service.deleteUserRole("56ffb0e9e4c549718e01eb2d18e466ca");
        if(updateNumber>0){
            String roleIdStr = "[{\"roleId\":\"7136b0ba498f465e975705add4643ba3\"}]";
            JSONArray sArray = JSON.parseArray(roleIdStr);
            UserRole userRole = null;
            int number = 0;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String roleId =(String)object.get("roleId");
                System.out.println("roleId:==="+roleId);
                if(roleId!=null && !"".equals(roleId)){
                        userRole = new UserRole();
                        userRole.setId(UuidUtil.getUUID());
                        userRole.setrId(roleId);
                        userRole.setuId("56ffb0e9e4c549718e01eb2d18e466ca");
                        number = service.updateUserRole(userRole);
                }
            }
            try {
                System.out.println("修改成功："+number);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }else{
            System.out.println("删除失败");
        }*/
        
    }
}
