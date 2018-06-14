package cn.financial.util;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 操作returnStateCode.xml
 * 
 * @author zlf
 *
 */
public class ElementXMLUtils {

    private static final String configName = "conf/returnStateCode.xml";

    /**
     * 返回状态码xml的节点值
     * 
     * @return
     * @throws DocumentException
     */
    public static String returnValue(String name, String value) {
        String result = "";
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(ElementXMLUtils.class.getClassLoader().getResource(configName).getPath());
            // 获取文档根节点
            Element root = document.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> lists = root.elements();
            for (Element e : lists) {
                Attribute attribute = e.attribute("name");
                String attrName = attribute.getValue();
                if (attrName.equals(name)) {
                    Element element = e.element(value);
                    String re = element.getStringValue();
                    if (re != null && re.length() != 0 && !re.equals("")) {
                        result = re;
                    }
                }
            }
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        return result;
    }

}
