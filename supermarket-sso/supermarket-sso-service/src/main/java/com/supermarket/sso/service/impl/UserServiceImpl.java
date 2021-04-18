package com.supermarket.sso.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.management.mapper.UserMapper;
import com.supermarket.management.pojo.User;
import com.supermarket.sso.redis.RedisUtils;
import com.supermarket.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${SSO_TICKET_KEY}")
    private String SSO_TICKET_KEY;

    @Override
    public Boolean check(String param, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            case 3:
                user.setEmail(param);
                break;
            default:
                break;
        }
        int count = this.userMapper.selectCount(user);

        if (count>0){
            return false;
        }else {
            return true;
        }
    }

    //用法：1、把json串转成对象，2、把对象转成json串,3解析json串
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public User queryUserByTicket(String ticket) {

        User user=null;
        String jsonStr=redisUtils.get(this.SSO_TICKET_KEY+ticket);
        if(StringUtils.isNotBlank(jsonStr)){
            this.redisUtils.expire(this.SSO_TICKET_KEY+ticket,60*60);
            try {
                //把json串转换成用户对象
                user = MAPPER.readValue(jsonStr, User.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;

    }

}
