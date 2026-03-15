package com.example.workdb.repository;



import com.example.workdb.entity.SteelGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SteelGradeRepository extends JpaRepository<SteelGrade, Integer> {
    Optional<SteelGrade> findByName(String name);
    boolean existsByName(String name);
}