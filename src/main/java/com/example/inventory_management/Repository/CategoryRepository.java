package com.example.inventory_management.Repository;

import com.example.inventory_management.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByParentCategory_CategoryId(Long parentId);
    List<Category> findByIsActive(boolean isActive);
    List<Category> findByParentCategoryAndIsActive(Category parent, boolean isActive);


}
