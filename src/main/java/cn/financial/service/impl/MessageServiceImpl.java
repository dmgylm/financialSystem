package cn.financial.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.MessageDAO;
import cn.financial.model.Message;
import cn.financial.service.MessageService;

/**
 * 消息Service业务实现类
 * 
 * @author zlf 2018/03/13
 *
 */
@Service("MessageServiceImpl")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDao;

    /**
     * 新增 消息 节点信息
     */
    @Override
    public Integer saveMessage(Message message) {
        return messageDao.saveMessage(message);
    }

    /**
     * 查询所有的消息
     */
    @Override
    public List<Message> listMessage() {
        return messageDao.listMessage();
    }

    /**
     * 根据条件查询消息
     */
    @Override
    public List<Message> listMessageBy(Map<Object, Object> map) {
        return messageDao.listMessageBy(map);
    }

    /**
     * 根据ID查询消息
     */
    @Override
    public Message getMessageById(String id) {
        return messageDao.getMessageById(id);
    }

    /**
     * 根据Id更新消息
     */
    @Override
    public Integer updateMessageById(Map<Object, Object> map) {
        return messageDao.updateMessageById(map);
    }

    /**
     * 根据Id删除消息
     */
    @Override
    public Integer deleteMessageById(String id) {
        return messageDao.deleteMessageById(id);
    }

}
