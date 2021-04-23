package com.supermarket.search.service.test;

import com.github.pagehelper.PageHelper;
import com.supermarket.management.mapper.ItemMapper;
import com.supermarket.management.pojo.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;


public class ItemData {

    private CloudSolrServer cloudSolrServer;
    private ItemMapper itemMapper;
    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");

        // 从容器中获取声明的bean
        this.cloudSolrServer = context.getBean(CloudSolrServer.class);
        this.itemMapper = context.getBean(ItemMapper.class);

    }

    @Test
    public void testItemData() throws Exception {
        // 可以使用循环来获取所有的商品数据
        int i = 1, pagesize = 500;
        do {
            // 分页查询商品数据
            PageHelper.startPage(i, 500);
            List<Item> list = this.itemMapper.select(null);

            // 遍历结果，把商品放到索引库中
            List<SolrInputDocument> docs = new ArrayList<>();
            for (Item item : list) {
                // 把获取的商品数据，批量导入到solr索引库中
                SolrInputDocument document = new SolrInputDocument();
                // 商品id
                document.setField("id", item.getId().toString());
                // 商品名称
                document.setField("item_title", item.getTitle());
                // 商品价格
                document.setField("item_price", item.getPrice());
                // 商品图片
                if (StringUtils.isNotBlank(item.getImage())) {
                    document.setField("item_image", StringUtils.split(item.getImage(), ",")[0]);
                } else {
                    document.setField("item_image", "");
                }
                // 商品类目id
                document.setField("item_cid", item.getCid());
                // 商品状态
                document.setField("item_status", item.getStatus());

                // 把Document放到集合中，统一提交
                docs.add(document);
            }

            // 把数据保存在solr索引库中
            this.cloudSolrServer.add(docs);
            this.cloudSolrServer.commit();

            i++;
            pagesize = list.size();

        } while (pagesize == 500);

    }



}
