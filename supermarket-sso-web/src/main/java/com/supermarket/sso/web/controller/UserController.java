package com.supermarket.sso.web.controller;
import com.supermarket.management.pojo.User;
import com.supermarket.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    @RequestMapping(value = "check/{param}/{type}",method = RequestMethod.GET)
    public ResponseEntity<String> check(@PathVariable("param") String param, @PathVariable("type") Integer type,String callback){
        if(type<1||type>3){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
           Boolean result=this.userService.check(param,type);
           String resultStr="";
           if(StringUtils.isNotBlank(callback)){
               resultStr=callback+"("+result+")";
           }else {
               resultStr+=result;
           }
           return ResponseEntity.ok(resultStr);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }

    @RequestMapping(value = "{ticket}",method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByTicket(@PathVariable("ticket") String ticket){

        try {
            User user=this.userService.queryUserByTicket(ticket);
            if (user!=null){
                return ResponseEntity.ok(user);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
