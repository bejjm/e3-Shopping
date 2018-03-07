package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemMessageListener implements MessageListener {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try{
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);
            //等待addItem()事务提交,因为addItem()没提交事务就已经发消息过来了
            Thread.sleep(100);
            //根据商品id查询商品信息
            SearchItem searchItem = itemMapper.getItemByid(itemId);
            //创建 一个solr文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档对象中添加域
            //向文档对象添加域
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            //把文档写入solr索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
