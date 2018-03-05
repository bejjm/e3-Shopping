package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestSolrj {

    @Test
    public void addDocument() throws IOException, SolrServerException {
        //创建一个SolrServer对象，创建一个连接。参数solr服务的url,solr默认库为collection1
        SolrServer solrServer =  new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");

        //创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档对象添加域，文档中必须包含一个id域，所有域的名称必须在schema.xml中定义
        document.addField("id","doc1");
        document.addField("item_title","测试商品1");
        document.addField("item_price",100);
        //把文档写入到索引库中
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void delDocument() throws IOException, SolrServerException {
        //创建一个SolrServer对象，创建一个连接。参数solr服务的url,solr默认库为collection1
        SolrServer solrServer =  new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        //删除文档
        //方法一
//        solrServer.deleteById("doc1");
        //方法二
        solrServer.deleteByQuery("id:doc1");
        //提交
        solrServer.commit();

    }
    //solrServer的更新和插入用的是同一个方法，插入时如果solr的id域相同则执行更新操作

    @Test
    public void queryIndex() throws SolrServerException {
        //创建一个SolrServer对象，创建一个连接。参数solr服务的url,solr默认库为collection1
        SolrServer solrServer =  new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        //创建一个SolrQuery对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
//        solrQuery.set("*:*");
        solrQuery.set("q", "*:*");//q为solr查询条件域
        //执行查询 QueryResponse对象
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //取文档列表，获取查询结果的总记录数
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("查询结果总记录数：" + results.getNumFound());
        //如果不设置查询的记录返回的条数（分页用），则默认为10
        for (SolrDocument solrDocument : results) {
            //遍历文档，取域中的内容
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
        }
    }

    @Test
    public void queryIndexFuza() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.132:8080/solr/collection1");
        //创建一个查询对象
        SolrQuery query = new SolrQuery();
        //查询条件
        query.setQuery("手机");
        query.setStart(0);
        query.setRows(20);
        query.set("df", "item_title");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //执行查询
        QueryResponse queryResponse = solrServer.query(query);
        //取文档列表。取查询结果的总记录数
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
        //遍历文档列表，从取域的内容。
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            //取高亮显示
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list !=null && list.size() > 0 ) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            System.out.println(title);
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
        }
    }
}
