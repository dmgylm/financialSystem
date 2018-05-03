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

import cn.financial.model.UserOrganization;
import cn.financial.service.UserOrganizationService;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml" })
/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
public class UserOrganizationTest {
    @Autowired
    private UserOrganizationService service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        //Map<Object, Object> map = new HashMap<>();
        //String codeStr = "[{\"code\":\"0101\"},{\"code\":\"0102\"},{\"code\":\"010101\"},{\"code\":\"010102\"}]";
        String orgIdStr = "[{\"orgId\":\"f8483e1c85e84323853aeee27b4e8c91\"},{\"orgId\":\"e71064dc0fc443fa8893ce489aed8c38\"},{\"orgId\":\"92aaf16c788f4797a0512e155c15f83c\"},{\"orgId\":\"634c24cf93c64665aca409b54bfb3f6e\"}]";
        JSONArray sArray = JSON.parseArray(orgIdStr);
        UserOrganization userOrganization = null;
        int number=0;
        for (int i = 0; i < sArray.size(); i++) {
            JSONObject object = (JSONObject) sArray.get(i);
            String orgId =(String)object.get("orgId");
            //map.put("code", code);// 根据组织架构code查询id
            //List<Organization> list = organizationService.listOrganizationBy(map);
            //if(list.size()>0){
                //for(Organization orgList:list){
                    userOrganization = new UserOrganization();
                    userOrganization.setId(UuidUtil.getUUID());
                    userOrganization.setoId(orgId);
                    System.out.println("organizationId:==="+orgId);
                    userOrganization.setuId("b7632238905a48a0b221264b4087ebf8");
                    userOrganization.setCreateTime("2018/3/26");
                    number = service.insertUserOrganization(userOrganization);
                //}
            //}
        }
        
        try {
            System.out.println(number);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //查询全部or根据用户id查询
    @Test
    public void ListUserOrganizationTest() {
        List<UserOrganization> role = service.listUserOrganization("1cb54fff435b4fff8aa7c1fa391f519b");
        for(UserOrganization list:role){
            System.out.println(" oId: "+list.getoId() +" uId: "+list.getuId() +" jobNumber: "+list.getJobNumber()+
                    " userName: "+list.getName()+" realName: "+list.getRealName()+" code: "+list.getCode()+
                    " ParentId: "+list.getParentId()+" His_permission: "+list.getHis_permission()+
                    " orgName: "+list.getOrgName()+" createTime: "+list.getCreateTime() +"\n");
        }
        
    }
    
    //修改
    @Test
    public void updateUserOrganizationTest(){
        UserOrganization userOrganization = null;
        int deleteNumber = service.deleteUserOrganization("1beb41326553418f9fc9b45d037a0925");
        if(deleteNumber>0){
            String orgIdStr = "[{\"orgId\":\"f8483e1c85e84323853aeee27b4e8c91\"},{\"orgId\":\"e71064dc0fc443fa8893ce489aed8c38\"},{\"orgId\":\"92aaf16c788f4797a0512e155c15f83c\"},{\"orgId\":\"634c24cf93c64665aca409b54bfb3f6e\"}]";
            JSONArray sArray = JSON.parseArray(orgIdStr);
            int number=0;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String orgId =(String)object.get("orgId");
                //map.put("code", code);// 根据组织架构code查询id
                //List<Organization> list = organizationService.listOrganizationBy(map);
                //if(list.size()>0){
                    //for(Organization orgList:list){
                        userOrganization = new UserOrganization();
                        userOrganization.setuId("1beb41326553418f9fc9b45d037a0925");
                        //userOrganization.setId("1beb41326553418f9fc9b45d037a0925");
                        userOrganization.setId(UuidUtil.getUUID());
                        userOrganization.setoId(orgId);
                        System.out.println("organizationId:==="+orgId);
                        
                        userOrganization.setCreateTime("2018/05/03");
                        number = service.updateUserOrganization(userOrganization);
                    //}
                //}
            }
            
            try {
                System.out.println("修改成功："+number);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }else{
            System.out.println("删除失败");
        }
        
    }
    
}
