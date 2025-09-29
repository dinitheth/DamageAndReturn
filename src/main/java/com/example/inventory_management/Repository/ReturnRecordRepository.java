package com.example.inventory_management.Repository;

import com.example.inventory_management.Models.ReturnRecord;
import com.example.inventory_management.Models.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRecordRepository extends JpaRepository<ReturnRecord, Long> {

    // Find all records by status (e.g., PENDING, APPROVED, REJECTED)
    List<ReturnRecord> findByStatus(String status);

    // Find all records by return type (e.g., DAMAGED, DEFECTIVE, WRONG_ITEM, EXCESS)
    List<ReturnRecord> findByReturnType(String returnType);

    // Find records linked to a specific spare part
    List<ReturnRecord> findByItem(SparePart item);

    // Find records within a date range
    List<ReturnRecord> findByReturnDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
