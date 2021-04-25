package com.supermarket.solr.test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

public class SolrjCloudTest {
	private CloudSolrServer solrServer;

	@Before
	public void init() {
		String zkHost = "192.168.37.161:3181,192.168.37.161:3182,192.168.37.161:3183";
		solrServer = new CloudSolrServer(zkHost);
		
		//指定solr集群的collection的名字
		solrServer.setDefaultCollection("collection2");
	}

	@Test
	public void testCreate() throws Exception {
		// 1. 创建HttpSolrServer，传入接口地址
		//HttpSolrServer solrServer = new HttpSolrServer("http://solr.supermarket.com/solr/");
		// 2. 创建SolrInputDocument对象，调用add方法构建文档内容
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "test_003");
		doc.addField("title", "这是测试3号");
		// 3. 通过HttpSolrServer调用add方法，把SolrInputDocument对象添加到索引库中
		solrServer.add(doc);
		// 4. 提交内容
		solrServer.commit();
	}

	@Test
	public void testUpdate() throws Exception {
		// 2. 创建SolrInputDocument对象，调用add方法构建文档内容
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "test_001");
		doc.addField("title", "这是测试1号123");
		// 3. 通过HttpSolrServer调用add方法，把SolrInputDocument对象添加到索引库中
		solrServer.add(doc);
		// 4. 提交内容
		solrServer.commit();
	}

	@Test
	public void testDel() throws Exception {
		// 跟据id删除索引
		// this.solrServer.deleteById("test_001");
		solrServer.deleteByQuery("*:*");
		this.solrServer.commit();
	}

	@Test
	public void testQuery() throws Exception {
		// 1. 创建搜索对象SolrQuery
		SolrQuery query = new SolrQuery();
		// 2. 设置查询条件(q,fq,)
		query.setQuery("title:好");
		// query.setFilterQueries("title:我");
		// 3. 如有需要，设置排序(setSort("字段名", ORDER.asc))
		query.setSort("id", ORDER.desc);
		// 4. 如有需要，设置分页(start与rows)
		query.setStart(0);
		query.setRows(5);
		// 5. 如有需要，设置fl(setFi..),设置df(set("df", "字段名")),设置wt(set("wt", "json"))
		query.setFields("id,title");
		// query.set("df","id");
		query.set("wt", "json");
		// 6. 如有需要，设置高亮(setHh,addHf,setHSP前后缀)
		query.setHighlight(true);
		query.addHighlightField("title");
		query.setHighlightSimplePre("<em style='color:red;'>");
		query.setHighlightSimplePost("</em>");
		// 7. 查询数据(server.query)
		QueryResponse response = this.solrServer.query(query);
		// 8. 获取文档列表(response.getResults)
		SolrDocumentList docs = response.getResults();

		// 9. 输出总条数(results.getNumFound)
		System.out.println(docs.getNumFound());
		// 10. 解析查询结果，遍历results，直接通过get("属性取值")

		// 11.如有需要输出高亮，先在循外response.getHighlighting()得到map,
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		for (SolrDocument doc : docs) {
			// 再map.get(doc.get("id")).get("高亮字段")
			List<String> list = map.get(doc.get("id")).get("title");
			if (list != null && list.size() > 0) {
				System.out.println(doc.get("id") + ":" + list.get(0));
			} else {
				System.out.println(doc.get("id") + ":" + doc.get("title"));
			}
		}

	}
}
