package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.UserRole;
import cn.financial.service.UserRoleService;
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
    private UserRoleService service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        String roleIdStr = "[{\"roleId\":\"732a2b28ea63417fbeceee1ac907fb92\"},{\"roleId\":\"c368fdf292da47c28f8c95e9e6c9fc2c\"},{\"roleId\":\"7136b0ba498f465e975705add4643ba3\"},{\"roleId\":\"21b4e7dd874040d9afcc5256442031ef\"}]";
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
                    userRole.setuId("b7632238905a48a0b221264b4087ebf8");
                    userRole.setCreateTime("2018/3/26");
                    number = service.insertUserRole(userRole);
            }
        }
        try {
            System.out.println(number);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //查询全部
    @Test
    public void ListUserRoleTest() {
        List<UserRole> role = service.listUserRole("");
        for(UserRole list:role){
            System.out.println(" rId: "+list.getrId() +" uId: "+list.getuId() +" roleName: "+list.getRoleName()+
            " jobNumber: "+list.getJobNumber()+" userName:"+list.getName()+" realName: "+list.getRealName()+
            " createTime: "+list.getCreateTime() +"\n");
        }
        
    }
}
