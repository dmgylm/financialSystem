package cn.financial.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonDetail {
	/**
	 * 生成简化数据
	 * @param arr
	 * @param newObj
	 * @return
	 */
	public static JSONObject generateSimplifyJson(JSONArray arr,String newObj) {

		JSONObject nJson = new JSONObject();
		for(int i=0;i<arr.size();i++) {
			Object obj = arr.get(i);
			if (obj instanceof JSONArray) {
				generateSimplifyJson((JSONArray) obj,newObj);
			} else if(obj instanceof JSONObject){
				JSONObject json = (JSONObject)obj;
				if(json.containsKey("type")) {
					if(json.getInt("type")==1) {
						continue;
					}					
					Integer type = json.getInt("type");
					if(type == HtmlGenerate.BOX_TYPE_INPUT|| type == HtmlGenerate.BOX_TYPE_FORMULA){
						if(json.containsKey("key")) {
							String key = json.getString("key");
							Object value="0";
							nJson.put(key, value);
						}
					}
					if(type == HtmlGenerate.BOX_TYPE_BUDGET){//预算的,type=4
						String key = json.getString("key");
						String value=Jsoncombined(newObj,key);
						nJson.put(key, value);
					}
				}
			}
		}
		return nJson;
	}
	
	public static String Jsoncombined(String object,String key) {
		try {
		    String json ="{\"1_\":0,\"2_benyueshiji\":1,\"3_benyueyusuan\":2,\"4_wanchenglv\":3,\"5_zhanbi\":4,\"6heji_benyueshiji\":1,\"7heji_benyueyusuan\":2,\"8heji_wanchenglv\":3,\"9heji_zhanbi\":4,\"10gongzi_benyueshiji\":6,\"11gongzi_benyueyusuan\":7,\"12gongzi_wanchenglv\":8,\"13gongzi_zhanbi\":9,\"14qizhonggongzi_benyueshiji\":11,\"15qizhonggongzi_benyueyusuan\":12,\"16qizhonggongzi_wanchenglv\":13,\"17qizhonggongzi_zhanbi\":14,\"18yewuticheng_benyueshiji\":16,\"19yewuticheng_benyueyusuan\":17,\"20yewuticheng_wanchenglv\":18,\"21yewuticheng_zhanbi\":19,\"22shehuibaoxianfei_benyueshiji\":21,\"23shehuibaoxianfei_benyueyusuan\":22,\"24shehuibaoxianfei_wanchenglv\":23,\"25shehuibaoxianfei_zhanbi\":24,\"26zhufanggongjijin_benyueshiji\":26,\"27zhufanggongjijin_benyueyusuan\":27,\"28zhufanggongjijin_wanchenglv\":28,\"29zhufanggongjijin_zhanbi\":29,\"30laowufei_benyueshiji\":31,\"31laowufei_benyueyusuan\":32,\"32laowufei_wanchenglv\":33,\"33laowufei_zhanbi\":34,\"34jiuyuanfeiyong_benyueshiji\":36,\"35jiuyuanfeiyong_benyueyusuan\":37,\"36jiuyuanfeiyong_wanchenglv\":38,\"37jiuyuanfeiyong_zhanbi\":39,\"38xinxifuwufei_benyueshiji\":41,\"39xinxifuwufei_benyueyusuan\":42,\"40xinxifuwufei_wanchenglv\":43,\"41xinxifuwufei_zhanbi\":44,\"42guanggaofei_benyueshiji\":46,\"43guanggaofei_benyueyusuan\":47,\"44guanggaofei_wanchenglv\":48,\"45guanggaofei_zhanbi\":49,\"46zixunfei_benyueshiji\":51,\"47zixunfei_benyueyusuan\":52,\"48zixunfei_wanchenglv\":53,\"49zixunfei_zhanbi\":54,\"50yinshuazhizuofei_benyueshiji\":56,\"51yinshuazhizuofei_benyueyusuan\":57,\"52yinshuazhizuofei_wanchenglv\":58,\"53yinshuazhizuofei_zhanbi\":59,\"54huodongfei_benyueshiji\":61,\"55huodongfei_benyueyusuan\":62,\"56huodongfei_wanchenglv\":63,\"57huodongfei_zhanbi\":64,\"58bangongfei_benyueshiji\":66,\"59bangongfei_benyueyusuan\":67,\"60bangongfei_wanchenglv\":68,\"61bangongfei_zhanbi\":69,\"62qichefeiyong_benyueshiji\":71,\"63qichefeiyong_benyueyusuan\":72,\"64qichefeiyong_wanchenglv\":73,\"65qichefeiyong_zhanbi\":74,\"66tongxunfei_benyueshiji\":76,\"67tongxunfei_benyueyusuan\":77,\"68tongxunfei_wanchenglv\":78,\"69tongxunfei_zhanbi\":79,\"70shineijiaotongfei_benyueshiji\":81,\"71shineijiaotongfei_benyueyusuan\":82,\"72shineijiaotongfei_wanchenglv\":83,\"73shineijiaotongfei_zhanbi\":84,\"74chalvfei_benyueshiji\":86,\"75chalvfei_benyueyusuan\":87,\"76chalvfei_wanchenglv\":88,\"77chalvfei_zhanbi\":89,\"78kuaidifei_benyueshiji\":91,\"79kuaidifei_benyueyusuan\":92,\"80kuaidifei_wanchenglv\":93,\"81kuaidifei_zhanbi\":94,\"82fulifei_benyueshiji\":96,\"83fulifei_benyueyusuan\":97,\"84fulifei_wanchenglv\":98,\"85fulifei_zhanbi\":99,\"86dizhiyihaopin_benyueshiji\":101,\"87dizhiyihaopin_benyueyusuan\":102,\"88dizhiyihaopin_wanchenglv\":103,\"89dizhiyihaopin_zhanbi\":104,\"90fangzujishuidianfei_benyueshiji\":106,\"91fangzujishuidianfei_benyueyusuan\":107,\"92fangzujishuidianfei_wanchenglv\":108,\"93fangzujishuidianfei_zhanbi\":109,\"94zhejiufeiyong_benyueshiji\":111,\"95zhejiufeiyong_benyueyusuan\":112,\"96zhejiufeiyong_wanchenglv\":113,\"97zhejiufeiyong_zhanbi\":114,\"98zichantanxiao_benyueshiji\":116,\"99zichantanxiao_benyueyusuan\":117,\"100zichantanxiao_wanchenglv\":118,\"101zichantanxiao_zhanbi\":119,\"102qita_benyueshiji\":121,\"103qita_benyueyusuan\":122,\"104qita_wanchenglv\":123,\"105qita_zhanbi\":124,\"106_\":0,\"107_benyueshiji\":1,\"108_benyueyusuan\":2,\"109_wanchenglv\":3,\"110_zhanbi\":4,\"111_\":0,\"112_benyueshiji\":1,\"113_benyueyusuan\":2,\"114_wanchenglv\":3,\"115_zhanbi\":4,\"116_\":0,\"117_benyueshiji\":1,\"118_benyueyusuan\":2,\"119_wanchenglv\":3,\"120_zhanbi\":4,\"121heji_benyueshiji\":1,\"122heji_benyueyusuan\":2,\"123heji_wanchenglv\":3,\"124heji_zhanbi\":4,\"125gongzi_benyueshiji\":6,\"126gongzi_benyueyusuan\":7,\"127gongzi_wanchenglv\":8,\"128gongzi_zhanbi\":9,\"129shehuibaoxianfei_benyueshiji\":11,\"130shehuibaoxianfei_benyueyusuan\":12,\"131shehuibaoxianfei_wanchenglv\":13,\"132shehuibaoxianfei_zhanbi\":14,\"133zhufanggongjijin_benyueshiji\":16,\"134zhufanggongjijin_benyueyusuan\":17,\"135zhufanggongjijin_wanchenglv\":18,\"136zhufanggongjijin_zhanbi\":19,\"137laowufei_benyueshiji\":21,\"138laowufei_benyueyusuan\":22,\"139laowufei_wanchenglv\":23,\"140laowufei_zhanbi\":24,\"141zhaopinpeixunfei_benyueshiji\":26,\"142zhaopinpeixunfei_benyueyusuan\":27,\"143zhaopinpeixunfei_wanchenglv\":28,\"144zhaopinpeixunfei_zhanbi\":29,\"145guanggaofei_benyueshiji\":31,\"146guanggaofei_benyueyusuan\":32,\"147guanggaofei_wanchenglv\":33,\"148guanggaofei_zhanbi\":34,\"149zixunfei_benyueshiji\":36,\"150zixunfei_benyueyusuan\":37,\"151zixunfei_wanchenglv\":38,\"152zixunfei_zhanbi\":39,\"153bangongfei_benyueshiji\":41,\"154bangongfei_benyueyusuan\":42,\"155bangongfei_wanchenglv\":43,\"156bangongfei_zhanbi\":44,\"157qichefeiyong_benyueshiji\":46,\"158qichefeiyong_benyueyusuan\":47,\"159qichefeiyong_wanchenglv\":48,\"160qichefeiyong_zhanbi\":49,\"161yewuzhaodaifei_benyueshiji\":51,\"162yewuzhaodaifei_benyueyusuan\":52,\"163yewuzhaodaifei_wanchenglv\":53,\"164yewuzhaodaifei_zhanbi\":54,\"165tongxunfei_benyueshiji\":56,\"166tongxunfei_benyueyusuan\":57,\"167tongxunfei_wanchenglv\":58,\"168tongxunfei_zhanbi\":59,\"169shineijiaotongfei_benyueshiji\":61,\"170shineijiaotongfei_benyueyusuan\":62,\"171shineijiaotongfei_wanchenglv\":63,\"172shineijiaotongfei_zhanbi\":64,\"173chalvfei_benyueshiji\":66,\"174chalvfei_benyueyusuan\":67,\"175chalvfei_wanchenglv\":68,\"176chalvfei_zhanbi\":69,\"177kuaidifei_benyueshiji\":71,\"178kuaidifei_benyueyusuan\":72,\"179kuaidifei_wanchenglv\":73,\"180kuaidifei_zhanbi\":74,\"181fulifei_benyueshiji\":76,\"182fulifei_benyueyusuan\":77,\"183fulifei_wanchenglv\":78,\"184fulifei_zhanbi\":79,\"185gonghuijingfei_benyueshiji\":81,\"186gonghuijingfei_benyueyusuan\":82,\"187gonghuijingfei_wanchenglv\":83,\"188gonghuijingfei_zhanbi\":84,\"189dizhiyihaopin_benyueshiji\":86,\"190dizhiyihaopin_benyueyusuan\":87,\"191dizhiyihaopin_wanchenglv\":88,\"192dizhiyihaopin_zhanbi\":89,\"193fangzujishuidianfei_benyueshiji\":91,\"194fangzujishuidianfei_benyueyusuan\":92,\"195fangzujishuidianfei_wanchenglv\":93,\"196fangzujishuidianfei_zhanbi\":94,\"197zhejiufeiyong_benyueshiji\":96,\"198zhejiufeiyong_benyueyusuan\":97,\"199zhejiufeiyong_wanchenglv\":98,\"200zhejiufeiyong_zhanbi\":99,\"201zichantanxiao_benyueshiji\":101,\"202zichantanxiao_benyueyusuan\":102,\"203zichantanxiao_wanchenglv\":103,\"204zichantanxiao_zhanbi\":104,\"205guanlishuifei_benyueshiji\":106,\"206guanlishuifei_benyueyusuan\":107,\"207guanlishuifei_wanchenglv\":108,\"208guanlishuifei_zhanbi\":109,\"209shenjinianjianfei_benyueshiji\":111,\"210shenjinianjianfei_benyueyusuan\":112,\"211shenjinianjianfei_wanchenglv\":113,\"212shenjinianjianfei_zhanbi\":114,\"213qita_benyueshiji\":116,\"214qita_benyueyusuan\":117,\"215qita_wanchenglv\":118,\"216qita_zhanbi\":119,\"217_\":0,\"218_benyueshiji\":1,\"219_benyueyusuan\":2,\"220_wanchenglv\":3,\"221_zhanbi\":4,\"222_\":0,\"223_benyueshiji\":1,\"224_benyueyusuan\":2,\"225_wanchenglv\":3,\"226_zhanbi\":4,\"227_\":0,\"228_benyueshiji\":1,\"229_benyueyusuan\":2,\"230_wanchenglv\":3,\"231_zhanbi\":4,\"232heji_benyueshiji\":1,\"233heji_benyueyusuan\":2,\"234heji_wanchenglv\":3,\"235heji_zhanbi\":4,\"236shouxufei_benyueshiji\":6,\"237shouxufei_benyueyusuan\":7,\"238shouxufei_wanchenglv\":8,\"239shouxufei_zhanbi\":9,\"240lixishouru_benyueshiji\":11,\"241lixishouru_benyueyusuan\":12,\"242lixishouru_wanchenglv\":13,\"243lixishouru_zhanbi\":14,\"244lixizhichu_benyueshiji\":16,\"245lixizhichu_benyueyusuan\":17,\"246lixizhichu_wanchenglv\":18,\"247lixizhichu_zhanbi\":19}";
		    JSONObject js=JSONObject.fromObject(json.toString());
			JSONObject ss=JSONObject.fromObject(js.get(object));
			String value=ss.get(key).toString();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
