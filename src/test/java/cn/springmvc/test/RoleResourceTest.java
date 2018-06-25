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
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})
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
        //添加所有功能权限
        String resourceIdStr = "[{\"resourceId\":\"0dd6008c6e7f4bce8e1d2ada94341ecf\"},{\"resourceId\":\"f14cdee2deb24271a912918a7f74ce19\"},{\"resourceId\":\"69105bb7dd0b48e7ace9bc0a0538add9\"},{\"resourceId\":\"a59955f1ce99471ba901f8d7b9218b21\"},{\"resourceId\":\"290cfb80455b47a38d7d16592a44b38f\"},{\"resourceId\":\"09eb61f1d57f4a85adaa510c874789f0\"},{\"resourceId\":\"f2fc5945361b4b1f8b5fa744a3b2e647\"},{\"resourceId\":\"73afcc0f3de14ae798c16111b940d660\"},"
                + "{\"resourceId\":\"c4278189665a41ef93e9d66a9b3e505f\"},{\"resourceId\":\"0afb0c0d5f04497d81f2738984f4e8af\"},{\"resourceId\":\"e7cdbe5c4a8d4d0d8f196044cd8410b5\"},{\"resourceId\":\"0b8b0ca5101c49b8beec9c8692610180\"},{\"resourceId\":\"85edba97bb4e45509fbb66f779466d14\"},{\"resourceId\":\"974d401bef384fa0ab3e31cc62c39bad\"},{\"resourceId\":\"2145375652204a61993ef0a8c5927438\"},{\"resourceId\":\"dd9e0fc6e1bc4d699cf599709779ed8d\"},"
                + "{\"resourceId\":\"18b0eb4a32fc487aa3d4d43647fae4b4\"},{\"resourceId\":\"a83ad36258f7497ebf5bcd3aa78ebb54\"},{\"resourceId\":\"35738c252d974ccf899a60b6ee8b59f4\"},{\"resourceId\":\"aa18329768ca4be1992a6647af355617\"},{\"resourceId\":\"e845649552244dec9cf4673d2ea3a384\"},{\"resourceId\":\"ef5ddcad7cfd4ad48fcdb96ff9eecf02\"},{\"resourceId\":\"34a709d78d434edf8f84fbe9c27c2456\"},{\"resourceId\":\"778a8aa4f23944a1b33502584d179b86\"},"
                + "{\"resourceId\":\"77d0a88b47dc4628b09772a43e9d94d4\"},{\"resourceId\":\"3416e4b94f924e8c9c2573f56c0b8391\"},{\"resourceId\":\"05ef177e9121454abcf3f3fd1849896c\"},{\"resourceId\":\"3e65b283d9e54b27b6697cc676e0ad2d\"},{\"resourceId\":\"18122a3e674743889b54ef42bbb984e2\"},{\"resourceId\":\"12e86480ff4047daa366c03e629bdeac\"},{\"resourceId\":\"65dbdfe7a99b4725b05d8f4e37323815\"},{\"resourceId\":\"cc41026faaa44603b9a4af7701864fb7\"},"
                + "{\"resourceId\":\"f2721926f1624fc7a9957dd7f0894215\"},{\"resourceId\":\"85c9103345ae4cbfa2e22bdc1aef62db\"},{\"resourceId\":\"778c1e8b04b44b8489909284bc4992e1\"},{\"resourceId\":\"e8869032b0464deb8288b5d131583aa5\"},{\"resourceId\":\"18673b06aa8641b299be20a7b04a18a6\"},{\"resourceId\":\"3d8f6644c2564558b20908312be164ae\"},{\"resourceId\":\"8b08267c00f44c05be509d01e673f9c1\"},{\"resourceId\":\"0552f9f4f33b466c9bd92b7a81738302\"},"
                + "{\"resourceId\":\"e19b63f3df24467e9545c1471395a56a\"},{\"resourceId\":\"042f6cc29b224cbca6510fe22821606b\"},{\"resourceId\":\"921bfa83018e467091faf34f91b7e401\"},{\"resourceId\":\"03767670e631466fb284391768110a59\"},{\"resourceId\":\"bd51ae689ecc4735b55a39c11bf04909\"}]";
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
            roleResource.setrId("c368fdf292da47c28f8c95e9e6c9fc2c");//超级管理员
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
