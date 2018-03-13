package cn.springmvc.test;

import java.util.Date;
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
        String id = UuidUtil.getUUID();
        Message message = new Message();
        message.setId(id);
        message.setStatus(1);
        message.setTheme(1);
        message.setContent("dasdasd");
        message.setuId("7963dbc544024729927c41ce8238db40");
        message.setCreateTime(new Date());
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
            System.out.println(message.getId() + "---------------------------------------------" + message.getStatus()
                    + "---------------------------------------------" + message.getTheme()
                    + "---------------------------------------------" + message.getContent()
                    + "---------------------------------------------" + message.getCreateTime()
                    + "---------------------------------------------" + message.getUpdateTime()
                    + "---------------------------------------------" + message.getuId());
        }
    }

    /**
     * 根据传入的map查询相应的消息表
     */
    @Test
    public void listMessageBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "bcb5aab96cb84d4fa8dc9bbb99eace9f");
        //map.put("createTime", new Date());
        List<Message> list = service.listMessageBy(map);
        for (Message message : list) {
            System.out.println(message.getId() + "---------------------------------------------" + message.getStatus()
                    + "---------------------------------------------" + message.getTheme()
                    + "---------------------------------------------" + message.getContent()
                    + "---------------------------------------------" + message.getCreateTime()
                    + "---------------------------------------------" + message.getUpdateTime()
                    + "---------------------------------------------" + message.getuId());
        }
    }

    /**
     * 根据ID查询消息表
     */
    @Test
    public void getMessageById() {
        String id = "bcb5aab96cb84d4fa8dc9bbb99eace9f";
        Message message = service.getMessageById(id);
        System.out.println(message.getId() + "---------------------------------------------" + message.getStatus()
                + "---------------------------------------------" + message.getTheme()
                + "---------------------------------------------" + message.getContent()
                + "---------------------------------------------" + message.getCreateTime()
                + "---------------------------------------------" + message.getUpdateTime()
                + "---------------------------------------------" + message.getuId());
    }

    /**
     * 根据条件修改消息表,这里是根据id来修改其他项,所以map中必须包含id
     */
    @Test
    public void updateMessageById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "bcb5aab96cb84d4fa8dc9bbb99eace9f");
        map.put("uId", "kjdashkdaskdakjdkasjk");
        Integer i = service.updateMessageById(map);
        System.out.println(i);
    }

    /**
     * 删除（根据Id删除消息表）
     */
    @Test
    public void deleteMessaageById() {
        String id = "bcb5aab96cb84d4fa8dc9bbb99eace9f";
        Integer i = service.deleteMessageById(id);
        System.out.println(i);
    }
}
