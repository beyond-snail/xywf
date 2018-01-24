package com.tool.utils.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.thoughtworks.xstream.XStream;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class XmlUtils {
    private final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private XmlUtils() {
    }

    /**
     * 序列化XML
     *
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> String toXML(Object obj) {
        XStream stream = getXStream();
        stream.processAnnotations(obj.getClass());
        return new StringBuffer(XML_DECLARATION).append(stream.toXML(obj)).toString();
    }

    /**
     * 反序列化XML
     *
     * @param xmlStr
     * @param clazz
     * @return
     */
    public static <T> T fromXML(String xmlStr, Class<T> clazz) {
        XStream stream = getXStream();
        stream.processAnnotations(clazz);
        Object obj = stream.fromXML(xmlStr);
        try {
            return clazz.cast(obj);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * 获取指定节点的值
     *
     * @param xpath
     * @param params
     * @return
     */
    public static String getNodeValue(String dataStr, String xpath) {
        try {
            // 将字符串转为xml
            Document document = DocumentHelper.parseText(dataStr);
            // 查找节点
            Element element = (Element) document.selectSingleNode(xpath);
            if (element != null) {
                return element.getStringValue();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取Xstream实例
     *
     * @return
     */
    public static XStream getXStream() {
        return new XStream();
    }


    public static HashMap<String, String> xmlStringToMap(String xmlContent) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            Document doc = DocumentHelper.parseText(xmlContent);
            if(doc == null)
                return map;
            Element root = doc.getRootElement();
            for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext();) {
                Element e = (Element) iterator.next();
                map.put(e.getName(), e.getText());
            }
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }


        return map;
    }





    public static void main(String[] args) {
        String data = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><response><version>2.0</version><responsecode>0000</responsecode><responsemsg>成功</responsemsg><mchntorderid>18012419474104790372</mchntorderid><orderid>001455706708</orderid><amt>1</amt><bankcard>6222021001097474941</bankcard><rem1/><rem2/><rem3/><signtp>md5</signtp><mchntcd>0003310f1063978</mchntcd><sign>82e92b00c09cc17038a5b7dceb17774f</sign><type>08</type><userid>1536</userid></response>";
        HashMap<String, String> str = xmlStringToMap(data);
        System.out.println(str.toString());
    }




}
