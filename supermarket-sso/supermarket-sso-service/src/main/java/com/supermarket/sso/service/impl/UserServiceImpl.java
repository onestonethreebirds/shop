package com.supermarket.sso.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.management.mapper.UserMapper;
import com.supermarket.management.pojo.User;
import com.supermarket.sso.redis.RedisUtils;
import com.supermarket.sso.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${SSO_TICKET_KEY}")
    private String SSO_TICKET_KEY;
    @Value("${SSO_TICKET_KEY_INCR}")
    private String SSO_TICKET_KEY_INCR;

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

    @Override
    public void doRegister(User user) {
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        // 需要给用户密码进行加密，保证密码安全，我们使用MD5加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        // 保存用户
        this.userMapper.insert(user);

    }

    @Override
    public String doLogin(User user) {
        // 根据用户名和密码查询用户
        // 设置密码加密，因为数据是用md5加密的
        user.setUsername(user.getUsername());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        User result = this.userMapper.selectOne(user);

        // 判断用户是否为空，如果不为空表示登录成功
        if (result != null) {
            try {
                // 生成唯一数ticket,可是使用redis的唯一数+用户id
                String ticket = "" + this.redisUtils.incr(this.SSO_TICKET_KEY_INCR) + result.getId();

                // 把ticket和用户数据放到redis中,模拟session，原来的session有效时间是半小时
                this.redisUtils.set(this.SSO_TICKET_KEY+ ticket, MAPPER.writeValueAsString(result), 60 * 30);

                // 返回ticket
                return ticket;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 如果查询的用户为空，返回空
        return null;
    }

}


