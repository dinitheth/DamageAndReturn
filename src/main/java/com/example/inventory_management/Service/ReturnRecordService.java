package com.example.inventory_management.Service;

import com.example.inventory_management.Models.ReturnRecord;
import com.example.inventory_management.Models.SparePart;
import com.example.inventory_management.Repository.ReturnRecordRepository;
import com.example.inventory_management.Repository.SparePartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnRecordService {

    @Autowired
    private ReturnRecordRepository returnRecordRepository;

    @Autowired
    private SparePartRepository sparePartRepository;

    // Add new return/damage record
    public ReturnRecord addReturnRecord(ReturnRecord record) {
        record.setStatus("PENDING"); // default status when created
        return returnRecordRepository.save(record);
    }

    // Update existing return/damage record
    public ReturnRecord updateReturnRecord(Long id, ReturnRecord updatedRecord) {
        Optional<ReturnRecord> existingOpt = returnRecordRepository.findById(id);
        if (existingOpt.isPresent()) {
            ReturnRecord existing = existingOpt.get();

            if (updatedRecord != null) {
                existing.setItem(updatedRecord.getItem());
                existing.setQuantity(updatedRecord.getQuantity());
                existing.setReturnType(updatedRecord.getReturnType());
                existing.setReason(updatedRecord.getReason());
                existing.setReturnDate(updatedRecord.getReturnDate());
                existing.setStatus(updatedRecord.getStatus());
                return returnRecordRepository.save(existing);
            }
            return existing;
        }
        return null;
    }

    // Delete return record
    @Transactional
    public boolean deleteReturnRecord(Long id) {
        Optional<ReturnRecord> recordOpt = returnRecordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            return false; // Not found
        }

        ReturnRecord record = recordOpt.get();

        // Ensure associated spare part exists
        SparePart part = record.getItem();
        if (part == null) {
            throw new IllegalStateException("Return record is not linked to a valid spare part.");
        }

        // Perform HARD DELETE
        returnRecordRepository.delete(record);
        return true;
    }

    // Get all return records
    public List<ReturnRecord> listReturnRecords() {
        return returnRecordRepository.findAll();
    }

    // Get by status (e.g., Pending, Approved, Rejected)
    public List<ReturnRecord> listByStatus(String status) {
        return returnRecordRepository.findByStatus(status);
    }

    // Get record by ID
    public ReturnRecord getReturnRecordById(Long id) {
        return returnRecordRepository.findById(id).orElse(null);
    }
}
