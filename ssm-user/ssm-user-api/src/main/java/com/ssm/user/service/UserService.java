package com.ssm.user.service;

import com.ssm.order.bean.Order;
import com.ssm.user.bean.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author brusion
 * @date 2018/9/4
 */
public interface UserService {

    @RequestMapping("/delete/{userId}")
    public int delete(@PathVariable("userId") Integer userId);

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public int insert(@RequestBody User user);

    @RequestMapping("/list")
    public List<User> getList();

    @RequestMapping("/getone/{userId}")
    public User getById(@PathVariable("userId") Integer userId);

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody User user);

    @RequestMapping("/order/{OrderId}")
    public Order getOrder(@PathVariable("OrderId") Integer orderId);
}
