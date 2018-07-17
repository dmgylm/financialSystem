package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.financial.model.Resource;
import cn.financial.model.RoleResource;
import cn.financial.service.ResourceService;
import cn.financial.util.HttpClient3;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})
/**
 * 资源权限表测试
 * @author gs
 * 2018/3/15
 */
public class ResourceTest {
    @Autowired
    private ResourceService service;
    
    HttpClient3 http = new HttpClient3();
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/resource/insert?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "code=1&name=333&url=5&permssion=555");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Resource parent = service.getResourceById("","1");//根据code查询对应功能权限parentId(父id是否存在)
        Resource resource = new Resource();
        resource.setId(UuidUtil.getUUID());
        resource.setName("6666666666");
        resource.setUrl("");
        resource.setPermssion("444444");
        if(parent != null && !"".equals(parent)){
            if(parent.getParentId() != null && !"".equals(parent.getParentId()) && !"0".equals(parent.getParentId())){
                resource.setParentId(parent.getParentId()+"/"+parent.getCode());
            }else{
                resource.setParentId("1");
            }
        }else{//没数据返回代表父id不存在直接把1赋值给parentId
            resource.setParentId("1"); 
        }
        try {
            System.out.println(service.insertResource(resource));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    //修改
    @Test
    public void updateTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/resource/upate?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "resourceId=3e983a1c8eee481eafec82cc394cbc69&name=333&url=555");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Resource parent = service.getResourceById("3e983a1c8eee481eafec82cc394cbc69","");//根据id查询对应功能权限parentId
        Resource resource = new Resource();
        resource.setId("3e983a1c8eee481eafec82cc394cbc69");
        resource.setName("权限设置");
        //resource.setUrl("/ttttt");
        if(parent != null && !"".equals(parent)){
            if(parent.getParentId() != null && !"".equals(parent.getParentId()) && !"1".equals(parent.getParentId())){
                resource.setParentId(parent.getParentId());
            }else{
                resource.setParentId("1");
            }
        }else{//没数据返回代表父id不存在直接把1赋值给parentId
            resource.setParentId("1"); 
        }
        //resource.setPermssion("3333333333");
        try {
            System.out.println(service.updateResource(resource));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    //删除
    /*@Test
    public void deleteTest() {   
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/resource/upate?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "resourceId=3e983a1c8eee481eafec82cc394cbc69");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(service.deleteResource("18b0eb4a32fc487aa3d4d43647fae4b4"));
    }*/
    //查询全部
    @Test
    public void ListRoleTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/resource/index?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*List<Resource> resource = service.listResource();
        List<TreeNode<RoleResource>> nodes = new ArrayList<>();
        JSONObject jsonObject = null;
        if(resource.size()>0){
            for (Resource rss : resource) {
                TreeNode<RoleResource> node = new TreeNode<>();
                node.setId(rss.getCode().toString());
                String b=rss.getParentId().substring(rss.getParentId().lastIndexOf("/")+1);
                node.setParentId(b);
                node.setName(rss.getName());
               // node.setNodeData(rss);
                nodes.add(node);
            }
            jsonObject = JSONObject.fromObject(TreeNode.buildTree(nodes));
        }
        System.out.println("resource:"+jsonObject);*/
        /*for(Resource list:resource){
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" code: "+list.getCode()+
                    " url: "+list.getUrl()+" parentId: "+list.getParentId()+" permssion: "+list.getPermssion()+
                    " createTime: "+list.getCreateTime() +" updateTime: "+list.getUpdateTime() +"\n"); 
        }*/
        
    }
    //根据id/code查询
    @Test
    public void ListByIdTest() {
        Resource resource = service.getResourceById("0dd6008c6e7f4bce8e1d2ada94341ecf","");
        if(resource!=null && !"".equals(resource)){
            System.out.println("id: "+resource.getId()+" name: "+resource.getName()+" code: "+resource.getCode()+
                    " url: "+resource.getUrl()+" parentId: "+resource.getParentId()+" permssion: "+resource.getPermssion()+
                    " createTime: "+resource.getCreateTime()+" updateTime: "+resource.getUpdateTime()); 
        }else{
            System.out.println("数据不存在");
        }
        
    }
}
