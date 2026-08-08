package com.gym.management.controller;

import com.gym.management.entity.Payment;
import com.gym.management.service.MemberService;
import com.gym.management.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        model.addAttribute("monthlyRevenue", paymentService.getMonthlyRevenue());
        model.addAttribute("pageTitle", "Payments");
        model.addAttribute("activePage", "payments");
        return "payments/list";
    }

    @GetMapping({"/add", "/new"})
    public String showAddForm(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("pageTitle", "Record Payment");
        model.addAttribute("activePage", "payments");
        return "payments/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        if (payment.isPresent()) {
            model.addAttribute("payment", payment.get());
            model.addAttribute("members", memberService.getAllMembers());
            model.addAttribute("pageTitle", "Edit Payment");
            model.addAttribute("activePage", "payments");
            return "payments/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Payment not found!");
        return "redirect:/payments";
    }

    @PostMapping("/save")
    public String savePayment(@Valid @ModelAttribute("payment") Payment payment,
                              BindingResult result, Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.getAllMembers());
            model.addAttribute("pageTitle", "Record Payment");
            model.addAttribute("activePage", "payments");
            return "payments/form";
        }
        boolean isNew = (payment.getId() == null);
        paymentService.savePayment(payment);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Payment recorded successfully!" : "Payment updated successfully!");
        return "redirect:/payments";
    }

    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paymentService.deletePayment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Payment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete payment.");
        }
        return "redirect:/payments";
    }
}
