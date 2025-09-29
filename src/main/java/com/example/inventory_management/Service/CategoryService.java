package com.example.inventory_management.Service;

import com.example.inventory_management.Models.Category;
import com.example.inventory_management.Models.SparePart;
import com.example.inventory_management.Repository.CategoryRepository;
import com.example.inventory_management.Repository.SparePartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SparePartRepository sparePartRepository;

    //Add Category
    public Category addCategory(Category category){
        category.setActive(true);
        return categoryRepository.save(category);
    }

    //Update Category
    public Category updateCategory(Long id, Category updatedCategory){
        Optional<Category> existingOpt = categoryRepository.findById(id);
        if(existingOpt.isPresent()){
            Category existing = existingOpt.get();

            // Only update if updatedCategory is not null
            if (updatedCategory != null) {
                existing.setCategoryName(updatedCategory.getCategoryName());
                existing.setParentCategory(updatedCategory.getParentCategory());
                existing.setActive(updatedCategory.isActive());
                return categoryRepository.save(existing);
            }
            return existing; // Return existing if updatedCategory is null
        }
        return null;
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return false; // Category not found
        }

        Category category = categoryOpt.get();

        // Check if category has any active parts
        List<SparePart> partsWithCategory = sparePartRepository.findByCategoryAndIsActive(category, true);
        if (!partsWithCategory.isEmpty()) {
            String partNames = partsWithCategory.stream()
                    .map(SparePart::getPartName)
                    .limit(5)
                    .collect(Collectors.joining(", "));

            if (partsWithCategory.size() > 5) {
                partNames += ", and " + (partsWithCategory.size() - 5) + " more";
            }

            throw new IllegalStateException("Cannot delete category '" + category.getCategoryName() +
                    "' because it has " + partsWithCategory.size() +
                    " associated parts including: " + partNames +
                    ". Please delete or reassign these parts first.");
        }

        // Check for active child categories
        List<Category> childCategories = categoryRepository.findByParentCategoryAndIsActive(category, true);
        if (!childCategories.isEmpty()) {
            String childNames = childCategories.stream()
                    .map(Category::getCategoryName)
                    .limit(3)
                    .collect(Collectors.joining(", "));

            throw new IllegalStateException("Cannot delete category '" + category.getCategoryName() +
                    "' because it has " + childCategories.size() +
                    " active child categories including: " + childNames +
                    ". Please delete or reassign these categories first.");
        }

        // Perform HARD DELETE
        categoryRepository.delete(category); // or deleteById(id)
        return true;
    }


    // Add validation for parent category assignment
    public boolean isValidParentCategory(Long categoryId, Long potentialParentId) {
        if (potentialParentId == null) return true; // null parent is allowed

        // A category can't be its own parent
        if (categoryId != null && categoryId.equals(potentialParentId)) {
            return false;
        }

        // Check for circular references
        Category potentialParent = categoryRepository.findById(potentialParentId).orElse(null);
        while (potentialParent != null) {
            if (potentialParent.getCategoryId().equals(categoryId)) {
                return false; // Circular reference detected
            }
            potentialParent = potentialParent.getParentCategory();
        }

        return true;
    }
    //Get all categories
    public List<Category> listCategories(){
        return categoryRepository.findAll();
    }

    //Get active categories
    public List<Category> listActiveCategories() {
        return categoryRepository.findByIsActive(true);
    }


    //Get categories by parent category ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }


}
