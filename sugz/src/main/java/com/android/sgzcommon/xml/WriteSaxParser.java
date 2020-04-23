package com.android.sgzcommon.xml;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by sgz on 2017/1/4.
 */

public class WriteSaxParser {
    /**
     * onWrite(handler)中示例：
     * char[] ch = null;
     * AttributesImpl attrs = new AttributesImpl();    //负责存放元素的属性信息
     * for (T book : books) {
     * attrs.clear();  //清空属性列表
     * //添加一个名为id的属性(type影响不大,这里设为string)
     * attrs.addAttribute(uri, localName, "id", "string", String.valueOf(book.getId()));
     * handler.startElement(uri, localName, "book", attrs);    //开始一个book元素 关联上面设定的id属性
     * handler.startElement(uri, localName, "name", null); //开始一个name元素 没有属性
     * ch = String.valueOf(book.getName()).toCharArray();
     * handler.characters(ch, 0, ch.length);   //设置name元素的文本节点
     * handler.endElement(uri, localName, "name");
     * handler.startElement(uri, localName, "price", null);//开始一个price元素 没有属性
     * ch = String.valueOf(book.getPrice()).toCharArray();
     * handler.characters(ch, 0, ch.length);   //设置price元素的文本节点
     * handler.endElement(uri, localName, "price");
     * handler.endElement(uri, localName, "book");
     * }
     */
    public static String serialize(String firstTag, OnWriteListener writeListener) throws Exception {
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();//取得SAXTransformerFactory实例
        TransformerHandler handler = factory.newTransformerHandler();           //从factory获取TransformerHandler实例
        Transformer transformer = handler.getTransformer();                     //从handler获取Transformer实例
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");            // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");                // 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");   // 是否忽略XML声明
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        handler.setResult(result);
        String uri = "";    //代表命名空间的URI 当URI无值时 须置为空字符串
        String localName = "";  //命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串
        handler.startDocument();
        handler.startElement(uri, localName, firstTag, null);
        writeListener.onWrite(handler);
        handler.endElement(uri, localName, firstTag);
        handler.endDocument();
        return writer.toString();
    }

    public interface OnWriteListener {
        void onWrite(TransformerHandler handler);
    }
}
