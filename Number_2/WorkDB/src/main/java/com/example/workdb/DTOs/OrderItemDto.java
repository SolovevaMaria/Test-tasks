package com.example.workdb.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class OrderItemDto {
    private Integer id;
    private Integer orderId;

    @NotNull(message = "Номер позиции")
    private Integer itemNumber;

    @NotNull(message = "ID марки стали")
    private Integer steelGradeId;

    private String steelGradeName;

    @NotNull(message = "Диаметр")
    @Positive(message = "Диаметр должен быть положительным!")
    private BigDecimal diameter;

    @NotNull(message = "Толщина стенки")
    @Positive(message = "Толщина стенки должна быть положительной!")
    private BigDecimal wallThickness;

    @NotNull(message = "Объем")
    @Positive(message = "Объем должен быть положительным!")
    private BigDecimal quantity;

    @NotBlank(message = "Единица измерения")
    private String unit;

    private String status;

    public OrderItemDto() {
    }

    public OrderItemDto(Integer id, Integer orderId, Integer itemNumber, Integer steelGradeId,
                        String steelGradeName, BigDecimal diameter, BigDecimal wallThickness,
                        BigDecimal quantity, String unit, String status) {
        this.id = id;
        this.orderId = orderId;
        this.itemNumber = itemNumber;
        this.steelGradeId = steelGradeId;
        this.steelGradeName = steelGradeName;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getSteelGradeId() {
        return steelGradeId;
    }

    public void setSteelGradeId(Integer steelGradeId) {
        this.steelGradeId = steelGradeId;
    }

    public String getSteelGradeName() {
        return steelGradeName;
    }

    public void setSteelGradeName(String steelGradeName) {
        this.steelGradeName = steelGradeName;
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