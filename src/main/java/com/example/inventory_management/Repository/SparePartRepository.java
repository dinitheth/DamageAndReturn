package com.example.inventory_management.Repository;

import com.example.inventory_management.Models.Category;
import com.example.inventory_management.Models.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SparePartRepository extends JpaRepository<SparePart,Long> {
    List<SparePart> findByCategory(Category category);
    List<SparePart> findByBrand(String brand);
    List<SparePart> findByIsActive(boolean isActive);
    List<SparePart> findByPartNameContainingIgnoreCase(String keyword);
    List<SparePart> findByCategoryAndIsActive(Category category, boolean isActive);

    // Custom query: find parts below minimum stock level
    @Query("SELECT s FROM SparePart s WHERE s.currentStockLevel < s.minimumStockLevel AND s.isActive = true")
    List<SparePart> findPartsBelowMinimumStockLevel();
    List<SparePart> findByPartNameContainingIgnoreCaseAndIsActive(String keyword, boolean isActive);

    @Query("SELECT p FROM SparePart p WHERE " +
            "(LOWER(p.partName) LIKE LOWER(CONCAT('%', :partName, '%')) OR :partName IS NULL) AND " +
            "(p.category.categoryName = :category OR :category IS NULL) AND " +
            "(p.brand = :brand OR :brand IS NULL) AND " +
            "(p.storageLocation = :location OR :location IS NULL) AND " +
            "p.isActive = true")
    List<SparePart> findByPartNameContainingIgnoreCaseAndCategoryAndBrandAndLocation(
            @Param("partName") String partName,
            @Param("category") String category,
            @Param("brand") String brand,
            @Param("location") String location);}
