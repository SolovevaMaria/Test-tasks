package com.example.workdb.repository;



import com.example.workdb.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByWorkshopContainingIgnoreCase(String workshop);

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems oi " +
            "WHERE (:workshop IS NULL OR LOWER(o.workshop) LIKE LOWER(CONCAT('%', :workshop, '%'))) " +
            "AND (:steelGrade IS NULL OR LOWER(oi.steelGrade.name) LIKE LOWER(CONCAT('%', :steelGrade, '%')))")
    List<Order> searchByWorkshopAndSteelGrade(@Param("workshop") String workshop,
                                              @Param("steelGrade") String steelGrade);
}