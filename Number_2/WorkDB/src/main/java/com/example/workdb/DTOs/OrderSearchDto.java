package com.example.workdb.DTOs;

import java.math.BigDecimal;

public class OrderSearchDto {
    private String workshop;
    private String steelGrade;
    private BigDecimal diameter;
    private BigDecimal wallThickness;

    public OrderSearchDto() {
    }

    public OrderSearchDto(String workshop, String steelGrade, BigDecimal diameter, BigDecimal wallThickness) {
        this.workshop = workshop;
        this.steelGrade = steelGrade;
        this.diameter = diameter;
        this.wallThickness = wallThickness;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getSteelGrade() {
        return steelGrade;
    }

    public void setSteelGrade(String steelGrade) {
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
}