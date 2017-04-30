package com.railwayserviceWX.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中   
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流   
        InputStream inputStream = request.getInputStream();
        // 读取输入流   
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素   
        Element root = document.getRootElement();
        // 得到根元素的所有子节点   
        List<Element> elementList = root.elements();

        // 遍历所有子节点   
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源   
        inputStream.close();
        inputStream = null;

        return map;
    }

    public static Document getDocumentFromInputStream(InputStream in) {
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document ret = null;

        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            ret = (Document) builder.parse(new InputSource(in));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
