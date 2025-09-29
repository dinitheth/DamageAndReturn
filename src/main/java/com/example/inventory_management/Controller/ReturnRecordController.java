package com.example.inventory_management.Controller;

import com.example.inventory_management.Models.ReturnRecord;
import com.example.inventory_management.Models.SparePart;
import com.example.inventory_management.Service.ReturnRecordService;
import com.example.inventory_management.Service.SparePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory/returns")
public class ReturnRecordController {

    @Autowired
    private ReturnRecordService returnRecordService;

    @Autowired
    private SparePartService sparePartService;

    // Get all return records
    @GetMapping
    public String listReturns(Model model) {
        model.addAttribute("returns", returnRecordService.listReturnRecords());
        return "returns/list"; // Thymeleaf template: templates/returns/list.html
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("returnRecord", new ReturnRecord());
        model.addAttribute("items", sparePartService.listActiveSpareParts()); // for dropdown
        return "returns/add"; // Thymeleaf template: templates/returns/add.html
    }

    // Add new return record
    @PostMapping("/add")
    public String addReturn(@ModelAttribute ReturnRecord returnRecord,
                            RedirectAttributes redirectAttributes) {
        try {
            returnRecordService.addReturnRecord(returnRecord);
            redirectAttributes.addFlashAttribute("success", "Return record added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventory/returns";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ReturnRecord record = returnRecordService.getReturnRecordById(id);
        if (record == null) {
            return "redirect:/inventory/returns";
        }
        model.addAttribute("returnRecord", record);
        model.addAttribute("items", sparePartService.listActiveSpareParts());
        return "returns/edit"; // Thymeleaf template: templates/returns/edit.html
    }

    // Update return record
    @PostMapping("/update/{id}")
    public String updateReturn(@PathVariable Long id,
                               @ModelAttribute ReturnRecord updatedRecord,
                               RedirectAttributes redirectAttributes) {
        try {
            returnRecordService.updateReturnRecord(id, updatedRecord);
            redirectAttributes.addFlashAttribute("success", "Return record updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventory/returns";
    }

    // Delete return record
    @PostMapping("/delete/{id}")
    public String deleteReturn(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = returnRecordService.deleteReturnRecord(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Return record deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Return record not found");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventory/returns";
    }
}
