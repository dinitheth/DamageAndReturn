package com.example.inventory_management.Controller;

import com.example.inventory_management.Models.Category;
import com.example.inventory_management.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //Get all categories
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.listActiveCategories());
        return "categories/list";
    }
    //Show add form
    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("category", new Category());
        model.addAttribute("allCategories", categoryService.listCategories());
        return "categories/add"; // Thymeleaf template: templates/categories/add.html
    }

    //Add category
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/inventory/categories"; // Redirect to categories list after adding
    }

    //Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return "redirect:/inventory/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("allCategories", categoryService.listActiveCategories());
        return "categories/edit";
    }

    //Update category
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute Category updatedCategory,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (!categoryService.isValidParentCategory(id,
                    updatedCategory.getParentCategory() != null ?
                            updatedCategory.getParentCategory().getCategoryId() : null)) {
                redirectAttributes.addFlashAttribute("error",
                        "Invalid parent category assignment (possible circular reference)");
                return "redirect:/inventory/categories/edit/" + id;
            }

            categoryService.updateCategory(id, updatedCategory);
            return "redirect:/inventory/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inventory/categories/edit/" + id;
        }
    }
    //Soft delete category
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = categoryService.deleteCategory(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Category deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Category not found");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventory/categories";
    }


}
