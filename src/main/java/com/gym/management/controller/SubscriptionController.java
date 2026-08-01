package com.gym.management.controller;

import com.gym.management.entity.Subscription;
import com.gym.management.service.MemberService;
import com.gym.management.service.MembershipPlanService;
import com.gym.management.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MembershipPlanService planService;

    @GetMapping("")
    public String listSubscriptions(@RequestParam(value = "status", required = false) String status,
                                     Model model) {
        if (status != null && !status.isEmpty()) {
            model.addAttribute("subscriptions", subscriptionService.getSubscriptionsByStatus(status));
            model.addAttribute("filterStatus", status);
        } else {
            model.addAttribute("subscriptions", subscriptionService.getAllSubscriptions());
        }
        model.addAttribute("pageTitle", "Subscriptions");
        model.addAttribute("activePage", "subscriptions");
        return "subscriptions/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("subscription", new Subscription());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("plans", planService.getAllPlans());
        model.addAttribute("pageTitle", "Add Subscription");
        model.addAttribute("activePage", "subscriptions");
        return "subscriptions/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<Subscription> subscription = subscriptionService.getSubscriptionById(id);
        if (subscription.isPresent()) {
            model.addAttribute("subscription", subscription.get());
            model.addAttribute("members", memberService.getAllMembers());
            model.addAttribute("plans", planService.getAllPlans());
            model.addAttribute("pageTitle", "Edit Subscription");
            model.addAttribute("activePage", "subscriptions");
            return "subscriptions/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Subscription not found!");
        return "redirect:/subscriptions";
    }

    @PostMapping("/save")
    public String saveSubscription(@Valid @ModelAttribute("subscription") Subscription subscription,
                                    BindingResult result, Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.getAllMembers());
            model.addAttribute("plans", planService.getAllPlans());
            model.addAttribute("pageTitle", "Add Subscription");
            model.addAttribute("activePage", "subscriptions");
            return "subscriptions/form";
        }
        boolean isNew = (subscription.getId() == null);
        subscriptionService.saveSubscription(subscription);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Subscription created successfully!" : "Subscription updated successfully!");
        return "redirect:/subscriptions";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.deleteSubscription(id);
            redirectAttributes.addFlashAttribute("successMessage", "Subscription deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete subscription.");
        }
        return "redirect:/subscriptions";
    }
}
