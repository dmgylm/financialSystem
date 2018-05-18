package cn.financial.util;

import java.text.DecimalFormat;
import java.util.Random;

public class StringUtils {

	/**
	 * 首字母转大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toFirstLetterUpperCase(String str) {
		if (str == null || str.length() < 2) {
			return str;
		}
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	public static boolean isValid(String str) {
		if (str != null && str.trim().length() > 0 && !"null".equalsIgnoreCase(str.trim())) {
			return true;
		}
		return false;
	}

	public static String convertNumber(Number value) {
		if (value == null) {
			return "";
		} else {
			DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
			df.setMaximumFractionDigits(2);
			return df.format(value);
		}
	}
	
	public static String randomString(String base,int length) {
	    Random random = new Random();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < length; i++) {
	        int number = random.nextInt(base.length());
	        System.out.println(number);
	        sb.append(base.charAt(number));
	    }
	    return sb.toString();  
	 }
	
	public static String randomString(int length){
		final String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";  
		return randomString(base, length);
	}
	
	public static String randomNumber(int length){
		final String base = "0123456789";  
		return randomString(base, length);
	}

	public static String subTextString(String str,int len){
        if(str.length()<len/2)return str;
        int count = 0;
        StringBuffer sb = new StringBuffer();
        String[] ss = str.split("");
        for(int i=1;i<ss.length;i++){
            count+=ss[i].getBytes().length>1?2:1;
            sb.append(ss[i]);
            if(count>=len)break;
        }
        //不需要显示...的可以直接return sb.toString();
        //return (sb.toString().length()<str.length())?sb.append("...").toString():str;
        return sb.toString();
    }
	
	/**
	 * String转Byte[]
	 * @param str
	 * @return
	 */
	public static byte[] String2byte(String str) {
		byte[] result = new byte[str.length() / 2];
		int index = 0;
		for (int i = 0; i < str.length(); i += 2) {
			result[index++] = (byte) Integer.parseInt(str.substring(i, i + 2),
					16);
		}
		return result;
	}
	
	public static String stringReplace(String str,int start,int end){
		
		StringBuffer sb = new StringBuffer();
        String[] ss = str.split("");
        for(int i=0;i<ss.length;i++){
            //count+=ss[i].getBytes().length>1?2:1;
            
            if(i<start || i>(ss.length-end-1)){
            	sb.append(ss[i]);
            }else{
            	sb.append("*");
            }
        }
		
		return sb.toString();
	}
	
	
	/**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return Sting
     */
    public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        }else{
            return "0.00";
        }

    }
    public static String formatFloatNumber(Double value) {
        if(value != null){
            if(value.doubleValue() != 0.00){
                java.text.DecimalFormat df = new java.text.DecimalFormat("#");
                return df.format(value.doubleValue());
            }
        }
        return "0";
    }
}
