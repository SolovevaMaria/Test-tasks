package com.example.workdb.service.impl;

import com.example.workdb.DTOs.SteelGradeDto;
import com.example.workdb.entity.SteelGrade;
import com.example.workdb.repository.SteelGradeRepository;
import com.example.workdb.service.SteelGradeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SteelGradeServiceImpl implements SteelGradeService {

    private final SteelGradeRepository steelGradeRepository;

    public SteelGradeServiceImpl(SteelGradeRepository steelGradeRepository) {
        this.steelGradeRepository = steelGradeRepository;
    }

    @Override
    public List<SteelGradeDto> getAllSteelGrades() {
        return steelGradeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SteelGradeDto getSteelGradeById(Integer id) {
        SteelGrade steelGrade = steelGradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Марка стали не найдена с id: " + id));
        return convertToDto(steelGrade);
    }

    @Override
    public SteelGradeDto createSteelGrade(SteelGradeDto steelGradeDto) {
        if (steelGradeRepository.existsByName(steelGradeDto.getName())) {
            throw new IllegalArgumentException("Марка стали с таким названием уже существует");
        }

        SteelGrade steelGrade = new SteelGrade();
        steelGrade.setName(steelGradeDto.getName());

        SteelGrade saved = steelGradeRepository.save(steelGrade);
        return convertToDto(saved);
    }

    @Override
    public SteelGradeDto updateSteelGrade(Integer id, SteelGradeDto steelGradeDto) {
        SteelGrade steelGrade = steelGradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Марка стали не найдена с id: " + id));

        if (!steelGrade.getName().equals(steelGradeDto.getName()) &&
                steelGradeRepository.existsByName(steelGradeDto.getName())) {
            throw new IllegalArgumentException("Марка стали с таким названием уже существует");
        }

        steelGrade.setName(steelGradeDto.getName());

        SteelGrade updated = steelGradeRepository.save(steelGrade);
        return convertToDto(updated);
    }

    @Override
    public void deleteSteelGrade(Integer id) {
        SteelGrade steelGrade = steelGradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Марка стали не найдена с id: " + id));
        steelGradeRepository.delete(steelGrade);
    }

    private SteelGradeDto convertToDto(SteelGrade steelGrade) {
        return new SteelGradeDto(steelGrade.getId(), steelGrade.getName());
    }
}