package cn.financial.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * UUID 工具类
 * 
 * @author zlf 2018/03/16
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
     * 根据兄弟节点的code，找到下一个序号,即为新增节点的code
     * 
     * @return
     */
    public static String getCodeByBrother(String parentCode, List<String> listCode) {
        List<Integer> code = new ArrayList<Integer>(listCode.size());
        for (String i : listCode) {
            String result = i.replaceFirst(parentCode, "");
            code.add(Integer.parseInt(result));
        }
        Integer re = Collections.max(code) + 1;
        String resultCode = parentCode + (re < 10 ? "0" + re : re + "");
        return resultCode;
    }

}
