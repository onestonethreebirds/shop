package com.supermarket.management.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Item;
import com.supermarket.management.pojo.ItemDesc;
import com.supermarket.management.service.ItemDescService;
import com.supermarket.management.service.ItemService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

    @Resource
    private ItemDescService itemDescService;
    @Override
    public void saveItem(Item item,String desc) {
        item.setStatus(1);
        super.save(item);
        ItemDesc itemDesc = new ItemDesc();

        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.save(itemDesc);
//使用activemq发送消息，消息内容尽量简洁，发送操作符和商品id即可
        this.sendMessage("save",item.getId());

    }
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 注入JMSTemplate
    @Autowired
    private JmsTemplate jmsTemplate;

    // 注入Destination
    @Autowired
    private Destination destination;

    private void sendMessage(final String type, Long itemId) {
        this.jmsTemplate.send(destination, new MessageCreator() {

            /**
             * Create a {@link Message} to be sent.
             *
             * @param session the JMS {@link Session} to be used to create the
             *                {@code Message} (never {@code null})
             * @return the {@code Message} to be sent
             * @throws JMSException if thrown by JMS API methods
             */
            @Override
            public Message createMessage(Session session) throws JMSException {
                // 声明消息
                TextMessage textMessage = new ActiveMQTextMessage();

                // 构建消息内容
                // 使用json格式的数据封装需要传递的消息
                Map<String, Object> map = new HashMap<>();
                // 操作符
                map.put("type", type);
                // 商品id
                map.put("itemId", itemId);

                try {
                    // 把map转为json格式的数据
                    String json = MAPPER.writeValueAsString(map);
                    // 设置消息内容
                    textMessage.setText(json);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return textMessage;
            }
        });
    }





    @Override
    public SupermarketResult<Item> queryItemList(Integer page, Integer rows) {

        PageHelper.startPage(page,rows);
        List<Item> list = super.queryListByWhere(null);
        PageInfo<Item> pageInfo = new PageInfo<>(list);
        SupermarketResult<Item> supermarketResult = new SupermarketResult<>();
        supermarketResult.setTotal(pageInfo.getTotal());
        supermarketResult.setRows(list);
        return supermarketResult;
    }
}
