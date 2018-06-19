package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Resource;
import cn.financial.model.RoleResource;
import cn.financial.service.ResourceService;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml","classpath:conf/spring-redis.xml"})
/**
 * 资源权限表测试
 * @author gs
 * 2018/3/15
 */
public class ResourceTest {
    @Autowired
    private ResourceService service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        Resource parent = service.getResourceById("","31");//根据code查询对应功能权限parentId(父id是否存在)
        Resource resource = new Resource();
        resource.setId(UuidUtil.getUUID());
        resource.setName("6666666666");
        resource.setUrl("/444444");
        resource.setPermssion("444444");
        if(parent != null && !"".equals(parent)){
            if(parent.getParentId() != null && !"".equals(parent.getParentId()) && !"1".equals(parent.getParentId())){
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
        }
    }
    //修改
    @Test
    public void updateTest() {
        Resource parent = service.getResourceById("f14cdee2deb24271a912918a7f74ce19","");//根据code查询对应功能权限parentId(父id是否存在)
        Resource resource = new Resource();
        resource.setId("f14cdee2deb24271a912918a7f74ce19");
        resource.setName("4444444");
        resource.setUrl("/ttttt");
        if(parent != null && !"".equals(parent)){
            if(parent.getParentId() != null && !"".equals(parent.getParentId()) && !"1".equals(parent.getParentId())){
                resource.setParentId(parent.getParentId());
            }else{
                resource.setParentId("1");
            }
        }else{//没数据返回代表父id不存在直接把1赋值给parentId
            resource.setParentId("1"); 
        }
        resource.setPermssion("3333333333");
        resource.setUpdateTime("2018/05/04");
        try {
            System.out.println(service.updateResource(resource));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteResource("18b0eb4a32fc487aa3d4d43647fae4b4"));
    }
    //查询全部
    @Test
    public void ListRoleTest() {
        List<Resource> resource = service.listResource();
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
        System.out.println("resource:"+jsonObject);
        /*for(Resource list:resource){
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" code: "+list.getCode()+
                    " url: "+list.getUrl()+" parentId: "+list.getParentId()+" permssion: "+list.getPermssion()+
                    " createTime: "+list.getCreateTime() +" updateTime: "+list.getUpdateTime() +"\n"); 
        }*/
        
    }
    //根据id/code查询
    @Test
    public void ListByIdTest() {
        //35a51fa5b6ed49ad920b30055ea9f4c6  1
        Resource resource = service.getResourceById("18b0eb4a32fc487aa3d4d43647fae4b4","");
        if(resource!=null && !"".equals(resource)){
            System.out.println("id: "+resource.getId()+" name: "+resource.getName()+" code: "+resource.getCode()+
                    " url: "+resource.getUrl()+" parentId: "+resource.getParentId()+" permssion: "+resource.getPermssion()+
                    " createTime: "+resource.getCreateTime()+" updateTime: "+resource.getUpdateTime()); 
        }else{
            System.out.println("数据不存在");
        }
        
    }
}
