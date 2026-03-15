package com.example.workdb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @NotBlank(message = "Номер заказа")
    @Column(name = "OrderNumber", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @NotBlank(message = "Цех-производитель")
    @Column(name = "Workshop", nullable = false, length = 100)
    private String workshop;

    @NotNull(message = "Дата начала")
    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "Дата окончания")
    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @NotBlank(message = "Статус")
    @Column(name = "Status", nullable = false, length = 20)
    private String status = "новый";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(Integer id, String orderNumber, String workshop, LocalDate startDate,
                 LocalDate endDate, String status, List<OrderItem> orderItems) {
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата окончания не может быть раньше даты начала!");
        }
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}