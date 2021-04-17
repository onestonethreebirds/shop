package com.supermarket.management.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.common.pojo.SupermarketResult;
import com.supermarket.management.pojo.Content;
import com.supermarket.management.redis.RedisUtils;
import com.supermarket.management.service.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {


    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Resource
    private RedisUtils redisUtils;

    @Value("${SUPERMARKET_PORTAL_AD}")
    private String SUPERMARKET_PORTAL_AD;


    @Override
    public SupermarketResult<Content> queryContentList(Long categoryId,Integer page, Integer rows){
        Content content = new Content();
        content.setCategoryId(categoryId);
        List<Content> list = super.queryListByWhere(content);
        PageHelper.startPage(page,rows);
        PageInfo<Content> pageInfo = new PageInfo<>(list);
        SupermarketResult<Content>supermarketResult=new SupermarketResult<>();
        supermarketResult.setTotal(pageInfo.getTotal());
        supermarketResult.setRows(list);
        return supermarketResult;
    }

    @Override
    public String queryAD(Long categoryId) {

        // 1. 从缓存中命中
        // 为了很好的管理和维护redis，需要redis的key是有意义的
        try {
            String redisJson = this.redisUtils.get(this.SUPERMARKET_PORTAL_AD);
            // 判断是否为空，如果不为空表示命中了，直接返回
            if (StringUtils.isNotBlank(redisJson)) {
                return redisJson;
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        // 根据分类id查询内容
        Content param = new Content();
        param.setCategoryId(categoryId);
        List<Content> list = super.queryListByWhere(param);

        // 遍历内容，把内容封装到List<Map>中，根据前端数据格式进行封装
        // 声明容器存放内容
        List<Map<String, Object>> results = new ArrayList<>();
        for (Content content : list) {

            Map<String, Object> map = new HashMap<>();

            map.put("srcB", content.getPic());
            map.put("height", 240);
            map.put("alt", "");
            map.put("width", 670);
            map.put("src", content.getPic());
            map.put("widthB", 550);
            map.put("href", content.getUrl());
            map.put("heightB", 240);

            // 把封装好的map放到list容器中
            results.add(map);

        }

        String json = "";

        try {
            // 需要把List<Map>序列化为json格式数据,可以使用Jackson工具类
            json = MAPPER.writeValueAsString(results);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            // 3. 把查到的数据放到redis中
            this.redisUtils.set(this.SUPERMARKET_PORTAL_AD, json, 60 * 60 * 24);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json;

    }

    @Override
    public void deleteContent(Long id) {
        List<Object> ids=new ArrayList<>();
        ids.add(id);
        super.deleteByIds(ids);
    }


}
