package cn.springmvc.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.RoleResource;
import cn.financial.service.RoleResourceService;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml"})
/**
 * 角色资源权限关联表测试
 * @author gs
 * 2018/3/15
 */
public class RoleResourceTest {
    @Autowired
    private RoleResourceService service;
    //新增
    @Test
    public void insertTest() {
        String resourceIdStr = "[{\"resourceId\":\"0dd6008c6e7f4bce8e1d2ada94341ecf\"},{\"resourceId\":\"c653a61be62d40b3a9b1a69bad184a79\"},{\"resourceId\":\"69105bb7dd0b48e7ace9bc0a0538add9\"},{\"resourceId\":\"a59955f1ce99471ba901f8d7b9218b21\"}]";
        JSONArray sArray = JSON.parseArray(resourceIdStr);
        RoleResource roleResource = null;
        int number = 0;
        for (int i = 0; i < sArray.size(); i++) {
            JSONObject object = (JSONObject) sArray.get(i);
            String resourceId =(String)object.get("resourceId");
            System.out.println("resouceId:==="+resourceId);
            roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getUUID());
            roleResource.setsId(resourceId);
            roleResource.setrId("732a2b28ea63417fbeceee1ac907fb92");
            roleResource.setCreateTime("2018/3/26");
            number = service.insertRoleResource(roleResource);
        }

        try {
            System.out.println(number);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //查询全部or根据角色id查询对应资源权限
    @Test
    public void ListRoleResourceTest() {
        List<RoleResource> roleResource = service.listRoleResource("c368fdf292da47c28f8c95e9e6c9fc2c");
        for(RoleResource list:roleResource){
            System.out.println(" rId: "+list.getrId() +" sId: "+list.getsId() +" roleName: "+list.getRoleName()+
            " name: "+list.getName()+" ParentId:"+list.getParentId()+" Permssion: "+list.getPermssion()+
            " url: "+list.getUrl()+" createTime: "+list.getCreateTime() +"\n");
        }
        
    }
    //修改
    @Test
    public void updateRoleResourceTest(){
        int deleteNumber = service.deleteRoleResource("4543efa5d6f24922ba6bd96f32d1ca42");
        if(deleteNumber > 0){
            String resourceIdStr = "[{\"resourceId\":\"0dd6008c6e7f4bce8e1d2ada94341ecf\"},{\"resourceId\":\"c653a61be62d40b3a9b1a69bad184a79\"},{\"resourceId\":\"69105bb7dd0b48e7ace9bc0a0538add9\"},{\"resourceId\":\"a59955f1ce99471ba901f8d7b9218b21\"}]";
            JSONArray sArray = JSON.parseArray(resourceIdStr);
            RoleResource roleResource = null;
            int number = 0;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String resourceId =(String)object.get("resourceId");
                System.out.println("resouceId:==="+resourceId);
                roleResource = new RoleResource();
                roleResource.setId(UuidUtil.getUUID());
                roleResource.setsId(resourceId);
                roleResource.setrId("4543efa5d6f24922ba6bd96f32d1ca42");
                roleResource.setCreateTime("2018/3/26");
                number = service.updateRoleResource(roleResource);
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
