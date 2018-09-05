package cn.financial.dao;

import java.util.List;
import java.util.Map;

import cn.financial.model.Message;
import cn.financial.model.User;

/**
 * 消息DAO业务接口
 * 
 * @author zlf 2018/03/13
 *
 */
public interface MessageDAO {

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
     * @param map
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
     * 根据Id删除消息
     * 
     * @param id
     * @return
     */
    Integer deleteMessageById(String id);
    
    /**
     * 检索所有的消息
     * @return
     */
    List<Message> listAllMessage();
    /**
     * 查询未读消息条数
     * @return
     */
    List<Message> listUnread(User user);

}
