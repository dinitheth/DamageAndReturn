package com.example.inventory_management.Models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "return_record")
public class ReturnRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private SparePart item;   // The spare part being returned/damaged

    private int quantity;     // Number of parts returned

    private String returnType; // e.g. DAMAGED, DEFECTIVE, WRONG_ITEM, EXCESS

    private String reason;     // Detailed reason for the return

    private String status;     // PENDING, APPROVED, REJECTED

    private LocalDate returnDate; // Date of the return

    public ReturnRecord() {
    }

    @Override
    public String getEntityName() {
        return this.item != null ? this.item.getPartName() : "Unknown Item";
    }

    public ReturnRecord(Long returnId, SparePart item, int quantity, String returnType,
                        String reason, String status, LocalDate returnDate) {
        this.returnId = returnId;
        this.item = item;
        this.quantity = quantity;
        this.returnType = returnType;
        this.reason = reason;
        this.status = status;
        this.returnDate = returnDate;
    }

    // Getters & Setters
    public Long getReturnId() {
        return returnId;
    }

    public void setReturnId(Long returnId) {
        this.returnId = returnId;
    }

    public SparePart getItem() {
        return item;
    }

    public void setItem(SparePart item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be null or empty");
        }
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
