package com.gym.management.controller;

import com.gym.management.service.MemberService;
import com.gym.management.service.PaymentService;
import com.gym.management.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");

        model.addAttribute("totalMembers", memberService.getTotalMembers());
        model.addAttribute("activeMembers", subscriptionService.countByStatus("Active"));
        model.addAttribute("expiredMembers", subscriptionService.countByStatus("Expired"));
        model.addAttribute("monthlyRevenue", paymentService.getMonthlyRevenue());

        model.addAttribute("recentMembers", memberService.getRecentMembers());
        model.addAttribute("recentPayments", paymentService.getRecentPayments());

        return "dashboard";
    }
}
