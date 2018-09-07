package com.ssm.order.controller;

import com.ssm.order.bean.Order;
import com.ssm.order.client.OrderClient;
import com.ssm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
@RequestMapping("/client")
public class ClientController implements OrderService{

    @Autowired
    private OrderClient orderClient;


    @Override
    @RequestMapping("/delete/{orderId}")
    public int delete(@PathVariable("orderId") Integer orderId) {
        return orderClient.delete(orderId);
    }

    @Override
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public int insert(@RequestBody Order order) {
        return orderClient.insert(order);
    }

    @Override
    @RequestMapping("/list")
    public List<Order> getList() {
        return orderClient.getList();
    }

    @Override
    @RequestMapping("/getone/{orderId}")
    public Order getById(@PathVariable("orderId") Integer orderId) {
        return orderClient.getById(orderId);
    }

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody Order order) {
        return orderClient.update(order);
    }

}
