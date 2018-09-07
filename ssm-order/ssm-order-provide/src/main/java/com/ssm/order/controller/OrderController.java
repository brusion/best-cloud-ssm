package com.ssm.order.controller;

import com.ssm.order.bean.Order;
import com.ssm.order.mapper.OrderMapper;
import com.ssm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author brusion
 * @date 2018/9/4
 */
@CrossOrigin
@RestController
public class OrderController implements OrderService {

    @Autowired
    private OrderMapper orderMapper;


    @Override
    @RequestMapping("/order/delete/{orderId}")
    public int delete(@PathVariable("orderId") Integer orderId) {
        return orderMapper.delete(orderId);
    }

    @Override
    @RequestMapping(value = "/order/insert", method = RequestMethod.POST)
    public int insert(@RequestBody Order order) {
        return orderMapper.insert(order);
    }

    @Override
    @RequestMapping("/order/list")
    public List<Order> getList() {
        return orderMapper.getList();
    }

    @Override
    @RequestMapping("/order/getone/{orderId}")
    public Order getById(@PathVariable("orderId") Integer orderId) {
        return orderMapper.getById(orderId);
    }

    @Override
    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    public int update(@RequestBody Order order) {
        return orderMapper.update(order);
    }

}
