package com.example.workdb.service;

import com.example.workdb.DTOs.OrderDto;
import com.example.workdb.DTOs.OrderSearchDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Integer id);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Integer id, OrderDto orderDto);
    void deleteOrder(Integer id);
    List<OrderDto> searchOrders(OrderSearchDto searchDto);
}