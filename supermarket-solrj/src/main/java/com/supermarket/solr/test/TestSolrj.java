package com.supermarket.solr.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.List;


public class TestSolrj {
    private HttpSolrServer httpSolrServer;

    @Before
    public void init(){
        httpSolrServer=new HttpSolrServer("http://solr.supermarket.com/solr");

    }


    @Test
    public void testSolrAddAndUpdate() throws Exception{
        // 1. 创建HttpSolrServer，传入接口地址
      //  HttpSolrServer httpSolrServer = new HttpSolrServer("http://solr.supermarket.com/solr/");
        // 2. 创建SolrInputDocument对象，调用add方法构建文档内容
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "test_solr_id");
        document.addField("title", "你们好，我是一个测试solr保存程序23424！");
        // 3. 通过HttpSolrServer调用add方法，把SolrInputDocument对象添加到索引库中
        httpSolrServer.add(document);
        // 4. 提交内容
        httpSolrServer.commit();
    }
    @Test
    public void testDeleteIndex() throws Exception {
        // 根据id删除索引数据
        // this.httpSolrServer.deleteById("test_solr_id");

        // 根据条件删除（如果是*:*就表示全部删除，慎用）
        this.httpSolrServer.deleteByQuery("*:*");

        // 提交
        this.httpSolrServer.commit();
    }

    @Test
    public void testQuery() throws Exception{
        // 1. 创建搜索对象SolrQuery
        SolrQuery solrQuery = new SolrQuery();
        // 2. 设置查询条件(q,fq,)
        solrQuery.setQuery("title:好");
        solrQuery.setFilterQueries("title:我");
        // 3. 如有需要，设置排序(setSort("字段名", ORDER.asc))
        solrQuery.setSort("id", ORDER.asc);
        // 4. 如有需要，设置分页(start与rows)
        solrQuery.setStart(0);
        solrQuery.setRows(10);
        // 5. 如有需要，设置fl(setFi..),设置df(set("df", "字段名")),设置wt(set("wt", "json"))
        solrQuery.setFields("id","title");
        solrQuery.set("df", "title");
        solrQuery.set("wt", "json");
        // 6. 如有需要，设置高亮(setHh,addHf,setHSP前后缀)
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        // 7. 查询数据(server.query)
        QueryResponse response = this.httpSolrServer.query(solrQuery);
        // 8. 获取文档列表(response.getResults)
        SolrDocumentList results = response.getResults();
        // 9. 输出总条数(results.getNumFound)
        System.out.println("搜索到数据的总条数为：" + results.getNumFound());
        //获取高亮数据
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        // 10. 解析查询结果，遍历results，直接通过get("属性取值")
        for (SolrDocument solrDocument : results) {
            System.out.println("id:" + solrDocument.get("id"));
            System.out.println("title:" + solrDocument.get("title"));
            // 11.
            // 如有需要输出高亮，先在循外response.getHighlighting()得到map,再map.get(doc.get("id")).get("高亮字段")
            System.out.println(map.get(solrDocument.get("id")).get("title").get(0));
        }
    }



}
