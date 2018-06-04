package cn.financial.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanyuToPingyinUtils {
	
	
	public static String hanyuToPinyin(String value){
		if(value==null) {
			return null;
		}
		try {
			HanyuPinyinOutputFormat hpof = new HanyuPinyinOutputFormat();
			hpof.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			hpof.setVCharType(HanyuPinyinVCharType.WITH_V);
			value = PinyinHelper
					.toHanyuPinyinString(value, hpof, "");
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return value;
	}

}
