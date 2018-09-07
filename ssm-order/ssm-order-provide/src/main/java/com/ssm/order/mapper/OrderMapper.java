package com.ssm.order.mapper;

import com.ssm.order.bean.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    int delete(Integer orderId);

    int insert(Order record);

    List<Order> getList();

    Order getById(Integer orderId);

    int update(Order record);
}