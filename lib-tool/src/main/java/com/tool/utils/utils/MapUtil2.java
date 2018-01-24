package com.tool.utils.utils;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

public class MapUtil2 {

    /**
     * Map key 排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> order(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }

    public static Map<String, Object> orderMap(Map<String, Object> map) {
        HashMap<String, Object> tempMap = new LinkedHashMap<String, Object>();
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });

        for (Map.Entry<String, Object> infoId : infoIds) {
            tempMap.put(infoId.getKey(), infoId.getValue());
        }

        return tempMap;
    }


    /**
     * 转换对象为map
     *
     * @param object
     * @param ignore
     * @return
     */
    public static Map<String, String> objectToMap(Object object, String... ignore) {
        Map<String, String> tempMap = new LinkedHashMap<String, String>();
        for (Field f : object.getClass().getDeclaredFields()) {
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            boolean ig = false;
            if (ignore != null && ignore.length > 0) {
                for (String i : ignore) {
                    if (i.equals(f.getName())) {
                        ig = true;
                        break;
                    }
                }
            }
            if (ig) {
                continue;
            } else {
                Object o = null;
                try {
                    o = f.get(object);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                tempMap.put(f.getName(), o == null ? "" : o.toString());
            }
        }
        return tempMap;
    }

    /**
     * url 参数串连
     *
     * @param map
     * @param keyLower
     * @param valueUrlencode
     * @return
     */
    public static String mapJoin(Map<String, String> map, boolean keyLower, boolean valueUrlencode) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (map.get(key) != null && !"".equals(map.get(key))) {
                try {
                    String temp = (key.endsWith("_") && key.length() > 1) ? key.substring(0, key.length() - 1) : key;
                    stringBuilder.append(keyLower ? temp.toLowerCase() : temp)
                            .append("=")
                            .append(valueUrlencode ? URLEncoder.encode(map.get(key), "utf-8").replace("+", "%20") : map.get(key))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }


//
//	public static Map<String, String> parseXml(HttpServletRequest request) {
//		Map<String, String> map = new HashMap<String, String>();
//		InputStream inputStream = null;
//		try {
//			inputStream = request.getInputStream();
//			XMLInputFactory factory = XMLInputFactory.newInstance();
//			XMLEventReader reader = factory.createXMLEventReader(inputStream);
//			while (reader.hasNext()) {
//				XMLEvent event = reader.nextEvent();
//				if (event.isStartElement()) {
//					String tagName = event.asStartElement().getName()
//							.toString();
//                   if ("xml".equals(tagName)) {
//
//					} else {
//						map.put(tagName, reader.getElementText());
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (XMLStreamException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (inputStream != null) {
//					inputStream.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return map;
//	}


//	public static String checkNull(HashMap<String, String> resultMap) {
//	    Set<String> keySet =resultMap.keySet();
//	    for(String key:keySet){
//	    	String value=resultMap.get(key);
//	    	if(StringUtils.isBlank(value)){
//	    		return key;
//	    	}else{
//	    		resultMap.put(key, StringUtils.trim(value));
//	    	}
//	    }
//		return null;
//	}

    public static void mapToXML(Map<String, String> map, StringBuffer sb) {
        Set<String> set = map.keySet();
        for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
    }

    public static HashMap<String, String> xmlStringToMap(String xmlContent) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlContent);
        HashMap<String, String> map = new HashMap<String, String>();
        if (doc == null)
            return map;
        Element root = doc.getRootElement();
        for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            map.put(e.getName(), e.getText());
        }
        return map;
    }


    public static void main(String[] args) {
        Map<String, String> result = null;
        try {
            result = xmlStringToMap("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><response><version>2.0</version><responsecode>0000</responsecode><responsemsg>成功</responsemsg><mchntorderid>18012419474104790372</mchntorderid><orderid>001455706708</orderid><amt>1</amt><bankcard>6222021001097474941</bankcard><rem1/><rem2/><rem3/><signtp>md5</signtp><mchntcd>0003310f1063978</mchntcd><sign>82e92b00c09cc17038a5b7dceb17774f</sign><type>08</type><userid>1536</userid></response>");
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(result);
    }

}
