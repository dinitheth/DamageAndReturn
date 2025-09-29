package com.example.inventory_management.Controller;

import com.example.inventory_management.Models.SparePart;
import com.example.inventory_management.Service.CategoryService;
import com.example.inventory_management.Service.SparePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inventory/parts")
public class InventoryController {
    @Autowired
    private SparePartService sparePartService;

    @Autowired
    private CategoryService categoryService;

    //Show list of parts
    @GetMapping
    public String listParts(Model model) {
        List<SparePart> parts = sparePartService.getAllSpareParts();
        List<SparePart> lowStockParts = sparePartService.getLowStockParts();
        model.addAttribute("parts", parts);
        model.addAttribute("lowStockParts", lowStockParts);
        return "parts/list";
    }

    //Show parts details
    @GetMapping("/{id}")
    public String partDetails(@PathVariable Long id, Model model){
        SparePart part = sparePartService.getPartById(id);
        if (part == null || !part.isActive()) {
            return "redirect:/inventory/parts";
        }
        model.addAttribute("part", part);
        return "parts/details";
    }

    //Show add form
    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("part", new SparePart());
        model.addAttribute("categories", categoryService.listActiveCategories());
        return "parts/add";
    }

    // Add missing POST handler for adding parts
    @PostMapping("/add")
    public String addPart(@ModelAttribute SparePart part) {
        sparePartService.addSparePart(part);
        return "redirect:/inventory/parts";
    }

    // Add missing edit form handler
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SparePart part = sparePartService.getPartById(id);
        if (part == null) {
            return "redirect:/inventory/parts";
        }
        model.addAttribute("part", part);
        model.addAttribute("categories", categoryService.listActiveCategories());
        return "parts/edit"; // This template needs to be created
    }

    //Update part
    @PostMapping("/update/{id}")
    public String updatePart(@PathVariable Long id, @ModelAttribute SparePart updatedPart){
        sparePartService.updateSparePart(id, updatedPart);
        return "redirect:/inventory/parts";
    }

    //Soft delete part
    @PostMapping("/delete/{id}")
    public String deletePart(@PathVariable Long id){
        sparePartService.deleteSparePart(id);
        return "redirect:/inventory/parts";
    }

    //Update stock manually (add/subtract)
    @PostMapping("/{id}/stock")
    public String updateStock(@PathVariable Long id, @RequestParam("quantityChange") int quantityChange, @RequestParam("updatedBy") Long updatedBy) {
        sparePartService.updateStock(id,quantityChange,updatedBy);
        return "redirect:/inventory/parts/" + id;
    }
}