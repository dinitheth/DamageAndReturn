package com.example.inventory_management.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spare_part")
public class SparePart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;
    private String partName;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String brand;
    private BigDecimal unitPrice;
    private Integer minimumStockLevel;
    private Integer currentStockLevel;
    private String storageLocation;


    public SparePart() {
    }

    @Override
    public String getEntityName() {
        return this.partName;
    }

    public SparePart(Long partId, String partName, Category category, String brand, BigDecimal unitPrice, Integer minimumStockLevel, Integer currentStockLevel, String storageLocation) {
        this.partId = partId;
        this.partName = partName;
        this.category = category;
        this.brand = brand;
        this.unitPrice = unitPrice;
        this.minimumStockLevel = minimumStockLevel;
        this.currentStockLevel = currentStockLevel;
        this.storageLocation = storageLocation;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public void setMinimumStockLevel(Integer minimumStockLevel) {
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock cannot be negative");
        }
        this.minimumStockLevel = minimumStockLevel;
    }

    public Integer getCurrentStockLevel() {
        return currentStockLevel;
    }

    public void setCurrentStockLevel(Integer currentStockLevel) {
        this.currentStockLevel = currentStockLevel;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }


}
