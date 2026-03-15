package com.example.workdb.controllers;

import com.example.workdb.DTOs.SteelGradeDto;
import com.example.workdb.service.SteelGradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/steel-grades")
public class SteelGradeController {

    private final SteelGradeService steelGradeService;

    public SteelGradeController(SteelGradeService steelGradeService) {
        this.steelGradeService = steelGradeService;
    }

    @GetMapping
    public ResponseEntity<List<SteelGradeDto>> getAllSteelGrades() {
        return ResponseEntity.ok(steelGradeService.getAllSteelGrades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SteelGradeDto> getSteelGradeById(@PathVariable Integer id) {
        return ResponseEntity.ok(steelGradeService.getSteelGradeById(id));
    }

    @PostMapping
    public ResponseEntity<SteelGradeDto> createSteelGrade(@Valid @RequestBody SteelGradeDto steelGradeDto) {
        return new ResponseEntity<>(steelGradeService.createSteelGrade(steelGradeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SteelGradeDto> updateSteelGrade(@PathVariable Integer id,
                                                          @Valid @RequestBody SteelGradeDto steelGradeDto) {
        return ResponseEntity.ok(steelGradeService.updateSteelGrade(id, steelGradeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSteelGrade(@PathVariable Integer id) {
        steelGradeService.deleteSteelGrade(id);
        return ResponseEntity.noContent().build();
    }
}