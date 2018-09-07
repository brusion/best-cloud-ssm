package com.ssm.order.service;

import com.ssm.order.bean.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 注意：如果服务中有消费者模块，服务提供者的路径直接写在service中（这样可以保证消费者和提供者的路径一致）
 *
 * @author brusion
 * @date 2018/9/4
 */
public interface OrderService {

    @RequestMapping("/order/delete/{orderId}")
    int delete(@PathVariable("orderId") Integer orderId);

    @RequestMapping(value = "/order/insert", method = RequestMethod.POST)
    int insert(@RequestBody Order order);

    @RequestMapping("/order/list")
    List<Order> getList();

    @RequestMapping("/order/getone/{orderId}")
    Order getById(@PathVariable("orderId") Integer orderId);

    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    int update(@RequestBody Order order);

}
