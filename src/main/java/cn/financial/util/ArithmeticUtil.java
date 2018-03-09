package cn.financial.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;


public class ArithmeticUtil {
    private static final Pattern pattern = Pattern.compile("[a-zA-Z_]+");
	private static Matcher matcher;
    /**
     * 根据算法保存表info字段的值
     * @param arithmeticPathName 算法文件路径
     * @param json		 前端传来的json
     * @param itemPathName	算法item文件路径
     * @return
     */
    public static JSONObject addinfo(String arithmeticPathName,JSONObject jsons,String itemPathName){
	LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();//用linkedHashMap是因为插入和输出时数据的顺序不会变，map只有在重写hashcode方法时才能这样不然顺序就是乱的
    	
	JSONObject jo1=JSONObject. fromObject(FileUtil.readFile(new File(arithmeticPathName)));//获取算法配置文件
	JSONObject jo2=JSONObject. fromObject(jsons);//获取前端页面传来的json数据
	String item=FileUtil.readFile(new File(itemPathName));//读取配置文件item数据比如营业费用，管理费用
	
	String [] stri=item.split(",");		     	//以数组的方式获取item值
	for (int i = 0; i < stri.length; i++) {
	    String itemName=stri[i].replace("\"","").trim();//获取每个项的名称
	    
	    JSONObject  jo=jo1.getJSONObject(itemName);		//根据item找到算法配置文件对应的小项
	    JSONObject  joT=jo2.getJSONObject(itemName);	//根据item找到前端传来json对应的小项
	    
	    	Iterator iterator = jo.keys();		//迭代算法配置文件的数据 
	        Iterator iterators = joT.keys();	//迭代前端传来json的数据
	        
	        LinkedHashMap<Object,Object>map=new LinkedHashMap <>();
	        List<String>ls=new ArrayList<>();
	        
	        while(iterators.hasNext()){		//保存前端传来json对应小项的
	            ls.add(iterators.next().toString());
	        }
	        
	        Iterator oit = joT.keys();		//迭代前端传来json的数据
	        while(iterator.hasNext()){		//当算法配置文件有下一个数据时
	            String key=(String) iterator.next();//保存当前算法配置文件对应小项的key
	            if(oit.hasNext()){			//当前端传来json的有下一个数据时
	        	String itskey=(String) oit.next();//保存当前前端传来json对应小项的key
	        	if(key.equals(itskey)){		//判断两个key是否相同
	        	    map.put(key, joT.getString(itskey)); //添加到map里，第一个是key，第二个值来源于前端传来的json对应key的value
	        	}
	            }else{
	        	    Map<Object,Object>nmap=new HashMap<>();
					 for (int k = 0; k < ls.size(); k++) {
					     if(jo.getString(key).contains(ls.get(k))){//判断算法配置文件里小项有没有需要计算的比如"salary":"housing_fund+insurance"这种
						 nmap.put(ls.get(k), joT.getString(ls.get(k)));
					     }
			        	  }
					 JSONObject json = JSONObject.fromObject(nmap);
					 map.put(key, analysisFormula(json, jo.getString(key)));//计算结果
		    }
	        }
	        dataMap.put(itemName, map)  ; 
	}
	
	/*  JSONObject JS=JSONObject.fromObject(dataMap);
	        System.out.println(JS+"===========");*/
	
	return JSONObject.fromObject(dataMap);
	
    }
	
	public static Double analysisFormula(JSONObject json,String formula){Binding binding = new Binding();
            GroovyShell shell = new GroovyShell(binding);
            String[] strings = stringFormat(formula);

            for(int i=0;i<strings.length;i++) {
            	String key = strings[i];
            		binding.setVariable(key,json.getDouble(key));
            }
                Object obj =shell.evaluate("return "+formula);
                return Double.valueOf(obj.toString());
	}
	
	public static String[] stringFormat(String sourStr) {
	    String tagerStr = sourStr;
	    matcher = pattern.matcher(tagerStr);
	    List<String> list = new ArrayList<String>();
	    while (matcher.find()) {
	        String key = matcher.group();
	        String keyclone = key.substring(0, key.length()).trim();
	        list.add(keyclone);
	    }
	    return list.toArray(new String[list.size()]);
	}
}
