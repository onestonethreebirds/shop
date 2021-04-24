package com.supermarket.item.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.management.service.ItemDescService;
import com.supermarket.management.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ItemMessageListener implements MessageListener {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            // 获取消息的内容
            TextMessage textMessage = (TextMessage) message;
            try {
                String json = textMessage.getText();

                // 判断json为非空
                if (StringUtils.isNotBlank(json)) {
                    // 使用MAPPER解析json格式的数据，获取jsonNode
                    JsonNode jsonNode = MAPPER.readTree(json);

                    // 获取商品的操作符
                    String type = jsonNode.get("type").asText();

                    // 获取商品id
                    long itemId = jsonNode.get("itemId").asLong();

                    // 根据商品id生成静态化页面
                    this.genHtml(itemId);

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService itemDescService;

    @Value("${SUPERMARKET_ITEM_HTML_PATH}")
    private String SUPERMARKET_ITEM_HTML_PATH;

    private void genHtml(long itemId) throws Exception {
        // 获取freemarker的核心对象，使用spring整合的对象获取
        Configuration configuration = this.freeMarkerConfigurer.getConfiguration();

        // 使用核心对象，获取模板
        Template template = configuration.getTemplate("item.ftl");

        // 设置模型数据
        Map<String, Object> root = new HashMap<>();
        // 把商品基础数据放到root中
        root.put("item", this.itemService.queryById(itemId));
        // 把商品描述数据放到root中
        root.put("itemDesc", this.itemDescService.queryById(itemId));

        // 使用模板输出静态页面
        Writer out = new FileWriter(new File(this.SUPERMARKET_ITEM_HTML_PATH + itemId + ".html"));
        template.process(root, out);

    }

}

