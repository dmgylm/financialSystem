package cn.financial.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.financial.model.Organization;
import cn.financial.model.User;

/**
 * UUID 工具类
 * 
 * @author user
 *
 */
public class UuidUtil {

    /**
     * 生成 UUID
     * 
     * @return
     */
    public static synchronized String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }
    
    /**
     * user
     */
    public static User strToUser(String userId, String name, String pwd, Integer privilege, Date createTime, Date updateTime, String oId){
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setPwd(pwd);
        user.setPrivilege(privilege);
        user.setCreateTime(createTime);
        user.setUpdateTime(updateTime);
        user.setoId(oId);
        return user;
    }
}
