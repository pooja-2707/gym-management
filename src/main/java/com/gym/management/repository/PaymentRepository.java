package com.gym.management.repository;

import com.gym.management.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Get 5 most recent payments
    List<Payment> findTop5ByOrderByPaymentDateDesc();

    // Calculate total revenue (sum of all payments)
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
    Double getTotalRevenue();

    // Calculate revenue for a specific month and year
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year")
    Double getRevenueByMonthAndYear(@Param("month") int month, @Param("year") int year);

    // Get payments between dates (for reports)
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    // Find payments by member
    List<Payment> findByMemberId(Long memberId);

    // Monthly revenue for chart - get revenue per month for current year
    @Query("SELECT MONTH(p.paymentDate), COALESCE(SUM(p.amount), 0) FROM Payment p WHERE YEAR(p.paymentDate) = :year GROUP BY MONTH(p.paymentDate) ORDER BY MONTH(p.paymentDate)")
    List<Object[]> getMonthlyRevenueByYear(@Param("year") int year);

    // Sum revenue between start and end dates
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Double getRevenueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
