package com.ssm.user.controller;

import com.ssm.order.bean.Order;
import com.ssm.order.client.OrderClient;
import com.ssm.user.bean.User;
import com.ssm.user.mapper.UserMapper;
import com.ssm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author brusion
 * @date 2018/9/4
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController implements UserService {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private UserMapper userMapper;

    @Override
    @RequestMapping("/delete/{userId}")
    public int delete(@PathVariable("userId") Integer userId) {
        return userMapper.delete(userId);
    }

    @Override
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public int insert(@RequestBody User user) {
        return userMapper.insert(user);
    }

    @Override
    @RequestMapping("/list")
    public List<User> getList() {
        return userMapper.getList();
    }

    @Override
    @RequestMapping("/getone/{userId}")
    public User getById(@PathVariable("userId") Integer userId) {
        return userMapper.getById(userId);
    }

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody User user) {
        return userMapper.update(user);
    }

    @Override
    @RequestMapping("/order/{orderId}")
    public Order getOrder(@PathVariable("orderId") Integer orderId) {
        return orderClient.getById(orderId);
    }


}
