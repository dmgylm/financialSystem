package cn.financial.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.financial.model.Message;

/**
 * 消息Service业务接口
 * 
 * @author zlf 2018/03/13
 *
 */
@Service
public interface MessageService {
    /**
     * 新增 消息 节点信息
     * 
     * @param message
     * @return
     */
    Integer saveMessage(Message message);

    /**
     * 根据消息状态展示消息列表
     * 
     * @return
     */
    List<Message> listMessage(Map<Object, Object> map);

    /**
     * 根据条件查询消息
     * 
     * @param map
     * @return
     */
    List<Message> listMessageBy(Map<Object, Object> map);

    /**
     * 根据ID查询消息
     * 
     * @param id
     * @return
     */
    Message getMessageById(String id);

    /**
     * 根据Id更新消息
     * 
     * @param map
     * @return
     */
    Integer updateMessageById(Map<Object, Object> map);
    /**
     * 根据IsTag更新消息
     * 
     * @param map
     * @return
     */
    Integer updateMessageByIsTag(Map<Object, Object> map);
    
    /**
     * 根据Id删除消息
     * 
     * @param id
     * @return
     */
    Integer deleteMessageById(String id);
}
