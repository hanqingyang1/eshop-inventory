package com.roncoo.eshop.inventory.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.inventory.dao.RedisDAO;
import com.roncoo.eshop.inventory.mapper.UserMapper;
import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisDAO redisDAO;


    @Override
    public User getCachedUserInfo() {
        redisDAO.set("cached_user", "{\"name\": \"zhangsan\", \"age\": 25}") ;
        String json = redisDAO.get("cached_user");
        JSONObject jsonObject = JSONObject.parseObject(json);

        User user = new User();
        user.setName(jsonObject.getString("name"));
        user.setAge(jsonObject.getInteger("age"));

        return user;
    }


    @Override
    public User findUserInfo() {

        User userInfo = userMapper.findUserInfo();
        return userInfo;
    }
}
