package cn.financial.util;


public class PropertiesUtils {

	private Config config;
	private static PropertiesUtils instance = null;
	private static final String configName = "financialSys.properties";
	
	static{
		instance = new PropertiesUtils();
	}
	
	private PropertiesUtils() {
		System.out.println(getClass().getResource("/"));
		
		config = new Config(getClass().getResource("/").getPath() + configName);
	}
	
	public static PropertiesUtils getInstance() {
		return instance;
	}
	
	public static String getProperty(String key) {
        return instance.config.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        value = value == null ? defaultValue : value;
        return value;
    }
    
    /**
     * 获取integer 值.如果没有,就返回null
     * @param key
     * @return
     */
    public static Integer getProperty4Int(String key){
    	String value = getProperty(key);
    	return value != null ? Integer.parseInt(value) : null;
    }
    
    /**
     * 值为null,就返回 设置的值,
     * @param key
     * @param defaultValue
     * @return
     */
    public static Integer getProperty4Int(String key, int defaultValue){
    	String value = getProperty(key);
    	return value != null ? Integer.parseInt(value) : defaultValue;
    }
    
    /**
     * 获取boolean类型配置,默认返回false
     * @param key
     * @return
     */
    public static boolean getProperty4Bool(String key){
    	return Boolean.parseBoolean(getProperty(key));
    }
    
    public static boolean getProperty4Bool(String key, Boolean defaultValue){
    	return Boolean.parseBoolean(getProperty(key, defaultValue.toString()));
    }
}
