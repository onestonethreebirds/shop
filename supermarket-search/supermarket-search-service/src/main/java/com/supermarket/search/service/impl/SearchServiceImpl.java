package com.supermarket.search.service.impl;

import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.mapper.ItemMapper;
import com.supermarket.management.pojo.Item;
import com.supermarket.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
/**

    @Autowired
    private CloudSolrServer cloudSolrServer;
    @Override
    public SupermarketResult<Item> search(String query, Integer page, Integer rows) {

        // 封装查询对象
        SolrQuery solrQuery = new SolrQuery();
        // 设置查询语句
        if (StringUtils.isNotBlank(query)) {
            solrQuery.setQuery("item_title:" + query + " AND item_status:1");
        } else {
            solrQuery.setQuery("item_status:1");
        }
// 设置分页
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
// 高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
// 声明返回结果对象SupermarketResult
        SupermarketResult<Item> supermarketResult = new SupermarketResult<>();
        try {
            // 执行查询
            QueryResponse response = this.cloudSolrServer.query(solrQuery);
            SolrDocumentList results = response.getResults();

            // 获取高亮数据
            Map<String, Map<String, List<String>>> map = response.getHighlighting();

            // 解析结果集
            // 声明存放商品的集合
            List<Item> list = new ArrayList<>();
            for (SolrDocument solrDocument : results) {
                Item item = new Item();

                // 解析Document
                // 商品id
                item.setId(Long.parseLong(solrDocument.get("id").toString()));

                // 获取高亮的数据
                List<String> hlist = map.get(solrDocument.get("id").toString()).get("item_title");

                // 商品title
                if (hlist != null && hlist.size() > 0) {
                    item.setTitle(hlist.get(0));
                } else {
                    item.setTitle(solrDocument.get("item_title").toString());
                }

                // 商品图片image
                item.setImage(solrDocument.get("item_image").toString());

                // 商品价格price
                item.setPrice(Double.parseDouble(solrDocument.get("item_price").toString()));

                // 商品cid
                item.setCid(Long.parseLong(solrDocument.get("item_cid").toString()));

                // 把封装好的商品数据放到集合中
                list.add(item);

            }

            // 封装返回数据taoResult
            // 设置结果集
            supermarketResult.setRows(list);
            // 设置查询的数据总条数
            supermarketResult.setTotal(results.getNumFound());

            return supermarketResult;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 如果查询有异常，就返回一个空的结果
        return supermarketResult;

    }
*/

@Autowired
private CloudSolrServer solrServer;

    @Override
    public SupermarketResult<Item> search(String query,Integer page, Integer rows ) {
        SupermarketResult<Item> result = new SupermarketResult<>();

        try {
            // 1. 创建搜索对象SolrQuery
            SolrQuery solrQuery = new SolrQuery();
            // 2. 设置查询条件(q,fq,)
            if (StringUtils.isNoneBlank(query)) {
                solrQuery.setQuery("item_title:" + query);
            } else {
                solrQuery.setQuery("*:*");
            }
            // 商品状态
            solrQuery.setFilterQueries("item_status:1");
            // 3. 如有需要，设置排序(setSort("字段名", ORDER.asc))
            // query.setSort("id", ORDER.desc);
            // 4. 如有需要，设置分页(start与rows)
            int start = (page - 1) * rows;
            solrQuery.setStart(start);
            solrQuery.setRows(rows);
            // 5. 如有需要，设置fl(setFi..),设置df(set("df", "字段名")),设置wt(set("wt",
            // "json"))
            // query.setFields("id,title");
            // query.set("df","id");
            solrQuery.set("wt", "json");
            // 6. 如有需要，设置高亮(setHh,addHf,setHSP前后缀)
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("item_title");
            solrQuery.setHighlightSimplePre("<em style='color:red;'>");
            solrQuery.setHighlightSimplePost("</em>");
            // 7. 查询数据(server.query)
            QueryResponse response = this.solrServer.query(solrQuery);
            // 8. 获取文档列表(response.getResults)
            SolrDocumentList docs = response.getResults();

            // 9. 输出总条数(results.getNumFound)
            result.setTotal(docs.getNumFound());
            // 10. 解析查询结果，遍历results，直接通过get("属性取值")

            // 11.如有需要输出高亮，先在循外response.getHighlighting()得到map,
            Map<String, Map<String, List<String>>> map = response.getHighlighting();
            List<Item> items = new ArrayList<>();
            Item item = null;
            for (SolrDocument doc : docs) {
                item = new Item();
                // 商品id
                item.setId(new Long(doc.get("id").toString()));
                // 再map.get(doc.get("id")).get("高亮字段")
                // 商品标题
                List<String> list = map.get(doc.get("id")).get("item_title");
                if (list != null && list.size() > 0) {
                    item.setTitle(list.get(0));

                } else {
                    item.setTitle(doc.get("item_title").toString());
                }
                // 商品价格
                item.setPrice(doc.get("item_price") == null ? 0 : new Double(doc.get("item_price").toString()));
                //商品图片
                item.setImage(doc.get("item_image") == null ? null : doc.get("item_image").toString());
                //类别id
                item.setCid(doc.get("item_cid") == null ? 0 : new Long(doc.get("item_cid").toString()));

                items.add(item);
            }
            result.setRows(items);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
