package com.ssm.order.client;

import com.ssm.order.bean.Order;
import com.ssm.order.service.OrderService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author brusion
 * @date 2018/9/4
 */
@FeignClient(name = "order-provide",fallback = OrderClient.OrderHystrix.class)
public interface OrderClient extends OrderService{

    @Component
    public class OrderHystrix implements OrderClient{
        @Override
        public int delete(Integer orderId) {
            return 0;
        }

        @Override
        public int insert(Order order) {
            return 0;
        }

        @Override
        public List<Order> getList() {
            return null;
        }

        @Override
        public Order getById(Integer orderId) {
            Order order = new Order();
            order.setOrderMark("error data");
            return order;
        }

        @Override
        public int update(Order order) {
            return 0;
        }
    }
}
