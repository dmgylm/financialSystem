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

}
