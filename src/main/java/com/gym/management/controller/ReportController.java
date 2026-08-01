package com.gym.management.controller;

import com.gym.management.service.MemberService;
import com.gym.management.service.PaymentService;
import com.gym.management.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("")
    public String reports(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        model.addAttribute("monthlyRevenue", paymentService.getMonthlyRevenue());
        model.addAttribute("payments", paymentService.getPaymentsBetweenDates(startDate, endDate));
        model.addAttribute("activeSubscriptions", subscriptionService.getSubscriptionsByStatus("Active"));
        model.addAttribute("expiredSubscriptions", subscriptionService.getSubscriptionsByStatus("Expired"));

        model.addAttribute("pageTitle", "Reports");
        model.addAttribute("activePage", "reports");

        return "reports/reports";
    }
}
