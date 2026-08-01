package com.gym.management.controller;

import com.gym.management.entity.MembershipPlan;
import com.gym.management.service.MembershipPlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/plans")
public class MembershipPlanController {

    @Autowired
    private MembershipPlanService planService;

    @GetMapping("")
    public String listPlans(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        model.addAttribute("pageTitle", "Membership Plans");
        model.addAttribute("activePage", "plans");
        return "plans/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("plan", new MembershipPlan());
        model.addAttribute("pageTitle", "Add Plan");
        model.addAttribute("activePage", "plans");
        return "plans/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<MembershipPlan> plan = planService.getPlanById(id);
        if (plan.isPresent()) {
            model.addAttribute("plan", plan.get());
            model.addAttribute("pageTitle", "Edit Plan");
            model.addAttribute("activePage", "plans");
            return "plans/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Plan not found!");
        return "redirect:/plans";
    }

    @PostMapping("/save")
    public String savePlan(@Valid @ModelAttribute("plan") MembershipPlan plan,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", plan.getId() != null ? "Edit Plan" : "Add Plan");
            model.addAttribute("activePage", "plans");
            return "plans/form";
        }
        boolean isNew = (plan.getId() == null);
        planService.savePlan(plan);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Plan added successfully!" : "Plan updated successfully!");
        return "redirect:/plans";
    }

    @GetMapping("/delete/{id}")
    public String deletePlan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            planService.deletePlan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Plan deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete plan. It may be assigned to subscriptions.");
        }
        return "redirect:/plans";
    }
}
