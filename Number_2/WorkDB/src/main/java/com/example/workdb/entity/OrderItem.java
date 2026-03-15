package com.example.workdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "OrderItems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @NotNull(message = "ID заказа")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId", nullable = false)
    @JsonIgnore
    private Order order;

    @NotNull(message = "Номер позиции")
    @Column(name = "ItemNumber", nullable = false)
    private Integer itemNumber;

    @NotNull(message = "Марка стали")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SteelGradeId", nullable = false)
    private SteelGrade steelGrade;

    @NotNull(message = "Диаметр")
    @Positive(message = "Диаметр должен быть положительным!")
    @Column(name = "Diameter", nullable = false, precision = 10, scale = 2)
    private BigDecimal diameter;

    @NotNull(message = "Толщина стенки")
    @Positive(message = "Толщина стенки должна быть положительной!")
    @Column(name = "WallThickness", nullable = false, precision = 10, scale = 2)
    private BigDecimal wallThickness;

    @NotNull(message = "Объем")
    @Positive(message = "Объем должен быть положительным!")
    @Column(name = "Quantity", nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    @NotBlank(message = "Единица измерения")
    @Column(name = "Unit", nullable = false, length = 20)
    private String unit;

    @NotBlank(message = "Статус")
    @Column(name = "Status", nullable = false, length = 20)
    private String status = "новая";

    public OrderItem() {
    }

    public OrderItem(Integer id, Order order, Integer itemNumber, SteelGrade steelGrade,
                     BigDecimal diameter, BigDecimal wallThickness, BigDecimal quantity,
                     String unit, String status) {
        this.id = id;
        this.order = order;
        this.itemNumber = itemNumber;
        this.steelGrade = steelGrade;
        this.diameter = diameter;
        this.wallThickness = wallThickness;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public SteelGrade getSteelGrade() {
        return steelGrade;
    }

    public void setSteelGrade(SteelGrade steelGrade) {
        this.steelGrade = steelGrade;
    }

    public BigDecimal getDiameter() {
        return diameter;
    }

    public void setDiameter(BigDecimal diameter) {
        this.diameter = diameter;
    }

    public BigDecimal getWallThickness() {
        return wallThickness;
    }

    public void setWallThickness(BigDecimal wallThickness) {
        this.wallThickness = wallThickness;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}