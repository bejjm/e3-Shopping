package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * solr索引库维护Service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    //使用的时它的子类httpSolrServer，已经在applicationContext-solr注入
    //注意 Autowired注入子类和实现类的区别
    private SolrServer solrServer;

    @Override
    public E3Result importAllItems() {
        try {

            //查询商品列表
            List<SearchItem> itemList = itemMapper.getItemList();
            //遍历商品列表
            for (SearchItem searchItem : itemList) {
                //创建solr文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档对象添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                //把文档对象写入到索引库
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            //返回成功
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500, "数据导入时反生异常！");
        }
    }


}
