package com.example.workdb.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class OrderDto {
    private Integer id;

    @NotBlank(message = "Номер заказа")
    private String orderNumber;

    @NotBlank(message = "Цех-производитель")
    private String workshop;

    @NotNull(message = "Дата начала")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания")
    private LocalDate endDate;

    private String status;

    private List<OrderItemDto> orderItems;

    public OrderDto() {
    }

    public OrderDto(Integer id, String orderNumber, String workshop, LocalDate startDate,
                    LocalDate endDate, String status, List<OrderItemDto> orderItems) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.workshop = workshop;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.orderItems = orderItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
}