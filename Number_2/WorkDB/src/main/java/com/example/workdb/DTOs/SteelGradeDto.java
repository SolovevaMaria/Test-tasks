package com.example.workdb.DTOs;

import jakarta.validation.constraints.NotBlank;

public class SteelGradeDto {
    private Integer id;

    @NotBlank(message = "Название марки стали")
    private String name;

    public SteelGradeDto() {
    }

    public SteelGradeDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}