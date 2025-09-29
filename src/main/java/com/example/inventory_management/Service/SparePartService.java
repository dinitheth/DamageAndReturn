package com.example.inventory_management.Service;

import com.example.inventory_management.Models.SparePart;
import com.example.inventory_management.Repository.SparePartRepository;
import com.example.inventory_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SparePartService {
    @Autowired
    private SparePartRepository sparePartRepository;

    @Autowired
    private UserRepository userRepository;

    //Add new spare part
    public SparePart addSparePart(SparePart part){
        part.setActive(true);
        part.setLastUpdated(LocalDateTime.now());
        return sparePartRepository.save(part);
    }

    public SparePart updateSparePart(Long id, SparePart updatedPart){
        Optional<SparePart> exisingOpt = sparePartRepository.findById(id);
        if(exisingOpt.isPresent()){
            SparePart existing = exisingOpt.get();
            existing.setPartName(updatedPart.getPartName());
            existing.setCategory(updatedPart.getCategory());
            existing.setBrand(updatedPart.getBrand());
            existing.setUnitPrice(updatedPart.getUnitPrice());
            existing.setMinimumStockLevel(updatedPart.getMinimumStockLevel());
            existing.setCurrentStockLevel(updatedPart.getCurrentStockLevel());
            existing.setStorageLocation(updatedPart.getStorageLocation());
            existing.setUpdatedBy(updatedPart.getUpdatedBy());
            existing.setLastUpdated(LocalDateTime.now());
            return sparePartRepository.save(existing);
        }
        return null;
    }

    //Soft-delete (set isActive = false)
    public void deleteSparePart(Long id) {
        sparePartRepository.deleteById(id);
    }

    //Manual stock update(+ or -)
    public boolean updateStock(Long partId, int quantityChange, Long updatedBy){
        if (partId == null || updatedBy == null) {
            return false;
        }

        Optional<SparePart> partOpt = sparePartRepository.findById(partId);
        if(partOpt.isPresent()){
            SparePart part = partOpt.get();
            int newStockLevel = part.getCurrentStockLevel() + quantityChange;

            // Prevent negative stock
            if (newStockLevel < 0) {
                return false;
            }

            part.setCurrentStockLevel(newStockLevel);
            part.setUpdatedBy(updatedBy);
            part.setLastUpdated(LocalDateTime.now());
            sparePartRepository.save(part);
            return true;
        }
        return false;
    }

    //Get parts below minimum stock
    public List<SparePart> getLowStockParts(){
        return sparePartRepository.findPartsBelowMinimumStockLevel();
    }

    public List<SparePart> getAllSpareParts() {
        return sparePartRepository.findByIsActive(true);
    }
    // Add this method if you need to get ALL parts (including inactive)
    public List<SparePart> getAllPartsIncludingInactive() {
        return sparePartRepository.findAll();
    }
    // Update getPartById to handle soft-deleted parts
    public SparePart getPartById(Long id) {
        Optional<SparePart> part = sparePartRepository.findById(id);
        return part.filter(p -> p.isActive()).orElse(null);
    }

    public List<SparePart> searchParts(String partName, String category, String brand, String location) {
        return sparePartRepository.findByPartNameContainingIgnoreCaseAndCategoryAndBrandAndLocation(
                partName != null ? partName : "",
                category,
                brand,
                location
        );
    }

    public Object listActiveSpareParts() {
        return false;

    }
}
