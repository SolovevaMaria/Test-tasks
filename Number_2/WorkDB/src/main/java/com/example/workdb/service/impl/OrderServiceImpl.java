package com.example.workdb.service.impl;

import com.example.workdb.DTOs.OrderDto;
import com.example.workdb.DTOs.OrderItemDto;
import com.example.workdb.DTOs.OrderSearchDto;
import com.example.workdb.entity.Order;
import com.example.workdb.entity.OrderItem;
import com.example.workdb.entity.SteelGrade;
import com.example.workdb.repository.OrderRepository;
import com.example.workdb.repository.SteelGradeRepository;
import com.example.workdb.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SteelGradeRepository steelGradeRepository;

    public OrderServiceImpl(OrderRepository orderRepository, SteelGradeRepository steelGradeRepository) {
        this.orderRepository = orderRepository;
        this.steelGradeRepository = steelGradeRepository;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден с id: " + id));
        return convertToDto(order);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        updateOrderFromDto(order, orderDto);

        Order saved = orderRepository.save(order);
        return convertToDto(saved);
    }

    @Override
    public OrderDto updateOrder(Integer id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден с id: " + id));

        order.getOrderItems().clear();

        updateOrderFromDto(order, orderDto);

        Order updated = orderRepository.save(order);
        return convertToDto(updated);
    }

    @Override
    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден с id: " + id));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> searchOrders(OrderSearchDto searchDto) {
        List<Order> orders = orderRepository.searchByWorkshopAndSteelGrade(
                searchDto.getWorkshop(),
                searchDto.getSteelGrade()
        );

        if (searchDto.getDiameter() != null || searchDto.getWallThickness() != null) {
            orders = orders.stream()
                    .filter(order -> order.getOrderItems().stream()
                            .anyMatch(item -> matchesCharacteristics(item, searchDto)))
                    .collect(Collectors.toList());
        }

        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private boolean matchesCharacteristics(OrderItem item, OrderSearchDto searchDto) {
        boolean matches = true;
        if (searchDto.getDiameter() != null) {
            matches = matches && item.getDiameter().compareTo(searchDto.getDiameter()) == 0;
        }
        if (searchDto.getWallThickness() != null) {
            matches = matches && item.getWallThickness().compareTo(searchDto.getWallThickness()) == 0;
        }
        return matches;
    }

    private void updateOrderFromDto(Order order, OrderDto orderDto) {
        order.setOrderNumber(orderDto.getOrderNumber());
        order.setWorkshop(orderDto.getWorkshop());
        order.setStartDate(orderDto.getStartDate());
        order.setEndDate(orderDto.getEndDate());
        order.setStatus(orderDto.getStatus() != null ? orderDto.getStatus() : "новый");

        if (orderDto.getOrderItems() != null) {
            for (OrderItemDto itemDto : orderDto.getOrderItems()) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setItemNumber(itemDto.getItemNumber());

                SteelGrade steelGrade = steelGradeRepository.findById(itemDto.getSteelGradeId())
                        .orElseThrow(() -> new EntityNotFoundException("Марка стали не найдена с id: " + itemDto.getSteelGradeId()));
                item.setSteelGrade(steelGrade);

                item.setDiameter(itemDto.getDiameter());
                item.setWallThickness(itemDto.getWallThickness());
                item.setQuantity(itemDto.getQuantity());
                item.setUnit(itemDto.getUnit());
                item.setStatus(itemDto.getStatus() != null ? itemDto.getStatus() : "новая");

                order.getOrderItems().add(item);
            }
        }
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setWorkshop(order.getWorkshop());
        dto.setStartDate(order.getStartDate());
        dto.setEndDate(order.getEndDate());
        dto.setStatus(order.getStatus());

        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList());
        dto.setOrderItems(itemDtos);

        return dto;
    }

    private OrderItemDto convertItemToDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrder().getId());
        dto.setItemNumber(item.getItemNumber());
        dto.setSteelGradeId(item.getSteelGrade().getId());
        dto.setSteelGradeName(item.getSteelGrade().getName());
        dto.setDiameter(item.getDiameter());
        dto.setWallThickness(item.getWallThickness());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getUnit());
        dto.setStatus(item.getStatus());
        return dto;
    }
}