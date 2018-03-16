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
import cn.financial.model.UserOrganization;
import cn.financial.service.impl.UserOrganizationServiceImpl;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
public class UserOrganizationTest {
    @Autowired
    private UserOrganizationServiceImpl service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setId(UuidUtil.getUUID());
        userOrganization.setoId("dc435e2de0fa4fc68b650faeddfaccf9");
        userOrganization.setuId("1cb54fff435b4fff8aa7c1fa391f519b");
        userOrganization.setCreateTime(new Date());
        try {
            System.out.println(service.insertUserOrganization(userOrganization));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //查询全部
    @Test
    public void ListUserOrganizationTest() {
        List<UserOrganization> role = service.listUserOrganization("1cb54fff435b4fff8aa7c1fa391f519b");
        for(UserOrganization list:role){
            System.out.println(" oId: "+list.getoId() +" uId: "+list.getuId() +" jobNumber: "+list.getJobNumber()+
                    " userName: "+list.getName()+" realName: "+list.getRealName()+" code: "+list.getCode()+
                    " ParentId: "+list.getParentId()+" His_permission: "+list.getHis_permission()+
                    " orgName: "+list.getOrgName()+" createTime: "+formatter.format(list.getCreateTime()) +"\n");
        }
        
    }
    
}
