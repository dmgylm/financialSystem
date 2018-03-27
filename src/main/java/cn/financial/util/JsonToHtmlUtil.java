package cn.financial.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * json工具类
 * 
 * @author zlf 2018/03/19
 *
 */
public class JsonToHtmlUtil {

    /**
     * 递归解析JSon
     * 
     * @param objJson
     */
    public static void analysisJson(Object objJson) {
        // 如果为json对象
        if (objJson instanceof JSONObject) {
            // 将Object对象转换为JSONObject对象
            JSONObject jsonObject = (JSONObject) objJson;
            // 迭代所有的Key值
            Iterator it = jsonObject.keys();
            // 遍历每个Key值
            while (it.hasNext()) {
                // 将key值转换为字符串
                String key = it.next().toString();
                // 根据key获取对象
                Object object = jsonObject.get(key);
                // 如果得到的是数组
                if (object instanceof JSONArray) {
                    // 将Object对象转换为JSONObject对象
                    JSONArray objArray = (JSONArray) object;
                    // 调用回调方法
                    analysisJson(objArray);
                }
                // 如果key中是一个json对象
                else if (object instanceof JSONObject) {
                    // 调用回调方法
                    analysisJson((JSONObject) object);
                } else {
                    System.out.println(key);
                }
            }
        }
        // 如果obj为json数组
        if (objJson instanceof JSONArray) {
            // 将接收到的对象转换为JSONArray数组
            JSONArray objArray = (JSONArray) objJson;
            // 对JSONArray数组进行循环遍历
            for (int i = 0; i < objArray.size(); i++) {
                analysisJson(objArray.get(i));
            }
        }
    }

    /**
     * 要返回的字符串
     */
    public static String str = "";

    /**
     * 解析json拼接
     * <table>
     * 标签
     *
     * @param objJson
     *            要解析的JSONObject对象
     * @param listtd
     *            横坐标string集合
     * @param listtr
     *            纵坐标string集合
     * @param listtr1
     *            纵坐标JSONObject集合
     */
    public static String jsonToTable(Object objJson, List<String> listtd, List<String> listtr, List<JSONObject> listtr1) {
        if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                if (object instanceof JSONObject) {
                    listtr.add(key);
                    listtr1.add((JSONObject) object);
                    str += ("<tr><td>" + key + "</td></tr>");
                    jsonToTable((JSONObject) object, listtd, listtr, listtr1);
                } else {
                    if (!listtd.contains(key)) {
                        listtd.add(key);
                    }
                }
            }
        }
        return str;
    }

    /**
     * json获取jsonobject各个key节点,即得到rowspan的值
     *
     * @param objJson
     *            要解析的JSONObject对象
     * @param listend
     *            此杜喜爱那个对底层的节点String集合
     * @return
     */
    public static void getNum(Object objJson, List<String> listend) {
        if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                if (object instanceof JSONObject) {
                    getNum((JSONObject) object, listend);
                    listend.add(key);
                }
            }
        }
    }

    /**
     * 对map集合进行逆序排序
     * 
     * @param oldhMap
     *            要排序的map集合
     * @return
     */
    public static HashMap<String, Object> sortMap(HashMap<String, Object> oldhMap) {
        // 把map转成Set集合
        Set<Entry<String, Object>> set = oldhMap.entrySet();
        // 通过set 创建一个 ArrayList 集合
        ArrayList<Entry<String, Object>> arrayList = new ArrayList<>(set);
        // 对arraylist进行倒序排序
        Collections.sort(arrayList, new Comparator<Entry<String, Object>>() {
            @Override
            public int compare(Entry<String, Object> arg0, Entry<String, Object> arg1) {
                // 逆序
                return -1;
            }
        });
        // 创建一个map
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < arrayList.size(); i++) {
            Entry<String, Object> entry = arrayList.get(i);
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 将JSONObject对象转成map对象
     * 
     * @param jsonObject
     *            要转换的jsonObject对象
     * @param listtd
     *            横坐标string集合
     * @return
     */
    public static void JsonToMap(Object jsonObject, HashMap<String, Object> map, List<String> listtd) {
        if (jsonObject instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) jsonObject;
            Iterator it = jsonObj.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObj.get(key);
                if (object instanceof JSONObject) {
                    HashMap<String, Object> mapresult = new HashMap<>();
                    JsonToMap((JSONObject) object, mapresult, listtd);
                    map.put(key, sortMap(mapresult));
                } else {
                    String value = jsonObj.get(key).toString();
                    map.put(key, value);
                    if (!listtd.contains(key)) {
                        listtd.add(key);
                    }
                }
            }
        }
    }

    /**
     * 找到json的纵坐标
     *
     * @param objJson
     *            要解析的JSONObject对象
     * @param listtr
     *            纵坐标string集合
     * @param listtr1
     *            纵坐标JSONObject集合
     */
    public static void JsonTr(Object objJson, List<String> listtr, List<JSONObject> listtr1) {
        if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                if (object instanceof JSONObject) {
                    listtr.add(key);
                    listtr1.add((JSONObject) object);
                    JsonTr((JSONObject) object, listtr, listtr1);
                }
            }
        }
    }

    /**
     * 用于拼接table
     */
    public static String str1 = "</tr>";
    /**
     * 用来记录等级
     */
    public static int index = 0;

    /**
     * 将map对象拼接成table标签
     * 
     * @param map
     *            要解析的HashMap对象
     * @return
     */
    public static String iteration(Map map) {
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Boolean falg = true;
            Map.Entry entry = (Map.Entry) entries.next();
            if (entry.getValue() instanceof Map) {
                Map temp = (Map) entry.getValue();
                iteration(temp);
                Iterator entries2 = temp.entrySet().iterator();
                while (entries2.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) entries2.next();
                    if (entry2.getValue() instanceof Map) {
                        falg = false;
                    }
                }
                if (!falg) {
                    index++;
                    str1 = "<tr><td>" + entry.getKey() + "</td></tr>" + str1;
                } else {
                    str1 = "<tr><td>" + entry.getKey() + "</td>" + str1;
                }
            } else {
                String tr_temp = str1.substring(0, 4);
                if ("<tr>".equals(tr_temp)) {
                    str1 = "</tr>" + str1;
                }
                str1 = "<td>" + entry.getValue() + "</td>" + str1;
            }
        }
        return str1;
    }

}
