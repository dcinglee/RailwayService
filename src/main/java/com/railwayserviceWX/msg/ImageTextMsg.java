package com.railwayserviceWX.msg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息
 *
 * @author lid
 * @date 2017.2.22
 */
public class ImageTextMsg extends Msg {

    // 图文消息个数，限制为10条以内
    private String articleCount;
    // 图文消息的数据
    private List<Item> items = new ArrayList<Item>(3);
    // 位0x0001被标志时，星标刚收到的消息。
    private String funcFlag;

    /**
     * 默认构造
     */
    public ImageTextMsg() {
        this.head = new MsgHead();
        this.head.setMsgType(Msg.MSG_TYPE_IMAGE_TEXT);
    }

    @Override
    public void write(Document document) {
        Element root = document.createElement(WXXmlElementName.ROOT);
        head.write(root, document);
        Element articleCountElement = document.createElement(WXXmlElementName.ARTICLE_COUNT);
        articleCountElement.setTextContent(this.articleCount);

        Element articlesElement = document.createElement(WXXmlElementName.ARTICLES);
        int size = Integer.parseInt(this.articleCount);
        for (int i = 0; i < size; i++) {
            Item currentItem = items.get(i);//获取当前
            Element itemElement = document.createElement(WXXmlElementName.ITEM);
            Element titleElement = document.createElement(WXXmlElementName.TITLE);
            titleElement.setTextContent(currentItem.getTitle());
            Element descriptionElement = document.createElement(WXXmlElementName.DESCRITION);
            descriptionElement.setTextContent(currentItem.getDescription());
            Element picUrlElement = document.createElement(WXXmlElementName.PIC_URL);
            picUrlElement.setTextContent(currentItem.getPicUrl());
            Element urlElement = document.createElement(WXXmlElementName.URL);
            urlElement.setTextContent(currentItem.getUrl());
            itemElement.appendChild(titleElement);
            itemElement.appendChild(descriptionElement);
            itemElement.appendChild(picUrlElement);
            itemElement.appendChild(urlElement);

            articlesElement.appendChild(itemElement);
        }

        Element funcFlagElement = document.createElement(WXXmlElementName.FUNC_FLAG);
        funcFlagElement.setTextContent(this.funcFlag);

        root.appendChild(articleCountElement);
        root.appendChild(articlesElement);

        document.appendChild(root);
    }

    @Override
    public void read(Document document) {

    }

    public String getFuncFlag() {
        return funcFlag;
    }

    public void setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
    }

    public void addItem(Item item) {
        this.items.add(item);
        this.reflushArticleCount();
    }

    public void removeItem(int index) {
        this.items.remove(index);
        this.reflushArticleCount();
    }

    /**
     * 刷新当前文章条数
     */
    private void reflushArticleCount() {
        this.articleCount = "" + this.items.size();
    }
}
