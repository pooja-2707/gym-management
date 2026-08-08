package com.gym.management.service;

import com.gym.management.entity.Payment;
import com.gym.management.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> getRecentPayments() {
        return paymentRepository.findTop5ByOrderByPaymentDateDesc();
    }

    public Double getTotalRevenue() {
        return paymentRepository.getTotalRevenue();
    }

    public Double getMonthlyRevenue() {
        LocalDate now = LocalDate.now();
        return paymentRepository.getRevenueByMonthAndYear(now.getMonthValue(), now.getYear());
    }

    public List<Payment> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    public List<Payment> getPaymentsByMember(Long memberId) {
        return paymentRepository.findByMemberId(memberId);
    }

    // Get monthly revenue data for chart (current year)
    public List<Object[]> getMonthlyRevenueData() {
        return paymentRepository.getMonthlyRevenueByYear(LocalDate.now().getYear());
    }

    // Get weekly revenue for the last 6 weeks (W1 to W6)
    public List<Double> getWeeklyRevenueLast6Weeks() {
        List<Double> weeklyRevenues = new java.util.ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate start = today.minusDays((i + 1) * 7L - 1);
            LocalDate end = today.minusDays(i * 7L);
            Double rev = paymentRepository.getRevenueBetweenDates(start, end);
            weeklyRevenues.add(rev != null ? rev : 0.0);
        }
        return weeklyRevenues;
    }
}
