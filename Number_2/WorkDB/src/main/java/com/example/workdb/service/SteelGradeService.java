package com.example.workdb.service;

import com.example.workdb.DTOs.SteelGradeDto;
import java.util.List;

public interface SteelGradeService {
    List<SteelGradeDto> getAllSteelGrades();
    SteelGradeDto getSteelGradeById(Integer id);
    SteelGradeDto createSteelGrade(SteelGradeDto steelGradeDto);
    SteelGradeDto updateSteelGrade(Integer id, SteelGradeDto steelGradeDto);
    void deleteSteelGrade(Integer id);
}