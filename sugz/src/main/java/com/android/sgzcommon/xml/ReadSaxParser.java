package com.android.sgzcommon.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public abstract class ReadSaxParser<T> {

    public List<T> parse(InputStream is) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();  //取得SAXParserFactory实例  
        SAXParser parser = factory.newSAXParser();                  //从factory获取SAXParser实例  
        MyHandler handler = new MyHandler();                        //实例化自定义Handler
        parser.parse(is, handler);                                  //根据自定义Handler规则解析输入流  
        return handler.getBooks();
    }

    private class MyHandler extends DefaultHandler {

        private T obj;
        private List<T> objs;
        private StringBuilder builder;

        //返回解析后得到的Book对象集合
        public List<T> getBooks() {
            return objs;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            objs = new ArrayList<T>();
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals(getStartOrEndTag())) {
                obj = getInstance();
            }
            //将字符长度设置为0 以便重新开始读取元素内的字符节点
            builder.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            //将读取的字符数组追加到builder中
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            onEndElement(obj, localName, builder.toString());
            if (localName.equals(getStartOrEndTag())) {
                objs.add(obj);
            }
        }
    }

    public abstract T getInstance();

    public abstract String getStartOrEndTag();

    /**
     * 示例：
     * if (localName.equals("id")) {
     * book.setId(Integer.parseInt(builder.toString()));
     * } else if (localName.equals("name")) {
     * book.setName(builder.toString());
     * } else if (localName.equals("price")) {
     * book.setPrice(Float.parseFloat(builder.toString()));
     * } else if (localName.equals("book")) {
     * books.add(book);
     * }
     *
     * @param t
     * @param localName
     * @param value
     */
    public abstract void onEndElement(T t, String localName, String value);
}