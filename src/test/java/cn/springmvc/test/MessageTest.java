package cn.springmvc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Message;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
public class MessageTest {

    @Autowired
    private MessageServiceImpl service;

    /**
     * 新增消息表
     */
    @Test
    public void saveMessage() {
        Message message = new Message();
        message.setId(UuidUtil.getUUID());
        message.setStatus(1);
        message.setTheme(1);
        message.setContent("2018年维修表格需要填写");
        message.setuId("1ccb0f2ca6224f389cbbea57b85d4458");
        Integer i = service.saveMessage(message);
        System.out.println(i);
    }

    /**
     * 查询所有的消息表
     */
    @Test
    public void listMessage() {
        List<Message> list = service.listMessage();
        for (Message message : list) {
            System.out.println("id:" + message.getId());
            System.out.println("status:" + message.getStatus());
            System.out.println("theme:" + message.getTheme());
            System.out.println("content:" + message.getContent());
            System.out.println("createTime:" + message.getCreateTime());
            System.out.println("updateTime:" + message.getUpdateTime());
            System.out.println("uid:" + message.getuId());
            System.out.println("isTag:" + message.getIsTag());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 根据传入的map查询相应的消息表
     */
    @Test
    public void listMessageBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        //map.put("id", "aa897350206245f4aeac083865d948c3");
        //map.put("status", 1);
        //map.put("theme", 1);
        map.put("content", "2018年金融表格需要填写");
        //map.put("createTime", "2018-03-16");
        //map.put("updateTime", "2018-03-16");
        map.put("uid", "1ccb0f2ca6224f389cbbea57b85d4458");
        //map.put("isTag", 0);
        List<Message> list = service.listMessageBy(map);
        for (Message message : list) {
            System.out.println("id:" + message.getId());
            System.out.println("status:" + message.getStatus());
            System.out.println("theme:" + message.getTheme());
            System.out.println("content:" + message.getContent());
            System.out.println("createTime:" + message.getCreateTime());
            System.out.println("updateTime:" + message.getUpdateTime());
            System.out.println("uid:" + message.getuId());
            System.out.println("isTag:" + message.getIsTag());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 根据ID查询消息表
     */
    @Test
    public void getMessageById() {
        String id = "aa897350206245f4aeac083865d948c3";
        Message message = service.getMessageById(id);
        System.out.println("id:" + message.getId());
        System.out.println("status:" + message.getStatus());
        System.out.println("theme:" + message.getTheme());
        System.out.println("content:" + message.getContent());
        System.out.println("createTime:" + message.getCreateTime());
        System.out.println("updateTime:" + message.getUpdateTime());
        System.out.println("uid:" + message.getuId());
        System.out.println("isTag:" + message.getIsTag());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * 根据条件修改消息表,这里是根据id来修改其他项,所以map中必须包含id
     */
    @Test
    public void updateMessageById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "aa897350206245f4aeac083865d948c3");
        map.put("status", 4);
        Integer i = service.updateMessageById(map);
        System.out.println(i);
    }

    /**
     * 删除（根据Id删除消息表）
     */
    @Test
    public void deleteMessaageById() {
        String id = "ab55154719bc458f9985f84e05034196";
        Integer i = service.deleteMessageById(id);
        System.out.println(i);
    }
}
