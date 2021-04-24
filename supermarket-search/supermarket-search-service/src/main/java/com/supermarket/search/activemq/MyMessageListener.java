package com.supermarket.search.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.supermarket.management.mapper.ItemMapper;
import com.supermarket.management.pojo.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyMessageListener implements MessageListener {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private CloudSolrServer cloudSolrServer;
	
	private final ObjectMapper mapper = new ObjectMapper();

	public void onMessage(Message message) {
		// 判断消息类型是TextMessage
		if (message instanceof TextMessage) {
			// 如果是，则进行强转
			TextMessage textMessage = (TextMessage) message;

			try {

				// 8. 消费消息，打印消息内容
				String text = textMessage.getText();
				
				JsonNode node = mapper.readTree(text);
				
				String type = node.get("type").asText();
				long itemId = node.get("itemId").asLong();
				
				//判断处理类型
				switch (type) {
				case "save":
					//添加索引
					Item item = this.itemMapper.selectByPrimaryKey(itemId);
					
					//创建文档
					SolrInputDocument doc = new SolrInputDocument();
					// 商品id
					doc.setField("id", item.getId().toString());
					// 商品名称
					doc.setField("item_title", item.getTitle());
					// 商品价格
					doc.setField("item_price", item.getPrice());
					// 商品图片
					if (StringUtils.isNotBlank(item.getImage())) {
						doc.setField("item_image", StringUtils.split(item.getImage(), ",")[0]);
					} else {
						doc.setField("item_image", "");
					}
					// 商品类目id
					doc.setField("item_cid", item.getCid());
					// 商品状态
					doc.setField("item_status", item.getStatus());
					
					this.cloudSolrServer.add(doc);
					this.cloudSolrServer.commit();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
