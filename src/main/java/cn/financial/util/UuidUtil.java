package cn.financial.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
        //List<Integer> code = new ArrayList<Integer>(listCode.size());
    	List<String> code = new ArrayList<String>(listCode.size());
        for (String i : listCode) {
            String result = i.replaceFirst(parentCode, "");
            //code.add(Integer.parseInt(result.trim()));
            code.add(result);
        }
        String sa=Collections.max(code);
        String begin=sa.subSequence(0, 1).toString();  
    	String re="";
        if(begin.equals("0")){
    		int a=Integer.valueOf(sa.substring(1))+1;
    	    re="0"+String.valueOf(a);
  
    	}
        else{
        	int a=Integer.valueOf(sa)+1;
        	re=String.valueOf(a);
        }
        String resultCode = parentCode + (Integer.valueOf(re) < 10 ? "0" + re : re + "");
        return resultCode;

    }
/*    
    public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("0101");
		list.add("010101");
		list.add("010102");
		list.add("0101010101");
		System.out.println(getCodeByBrother("01", list));
	}*/
    
    /**
     * 获取验证过的随机密码  
     * @param len
     * @return
     */
    public static String getRandomPassword(int len) {  
        String result = null;      
        /*if(len >= 6) { 
            for(result = makeRandomPassword(len);len >= 6;result = makeRandomPassword(len)){              
                if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) { 
                    return result; 
                }  
            } 
        }*/  
        while(len>=6){  
            result = makeRandomPassword(len);  
            if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {  
                return result;  
            }   
            result = makeRandomPassword(len);  
        }  
        return "长度不得少于6位!";  
    }  
    /**
     * 生成随机密码
     * @param len
     * @return
     */
    public static String makeRandomPassword(int len){  
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();  
        //System.out.println("字符数组长度:" + charr.length); //可以看到调用此方法多少次  
        StringBuilder sb = new StringBuilder();  
        Random r = new Random();  
          
        for (int x = 0; x < len; ++x) {  
            sb.append(charr[r.nextInt(charr.length)]);  
        }  
        return sb.toString();  
    }
    /**
     * 计算密码有效期（即日起生效，90天之内有效）
     * @return
     */
    public static String expreTime(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1);
        System.out.println("当前日期:"+sf.format(c.getTime()));
        c.add(Calendar.DATE, +91);
        System.out.println("增加91天后日期:"+sf.format(c.getTime())+" 00:00:00");
        return sf.format(c.getTime())+" 00:00:00";
    }
}
