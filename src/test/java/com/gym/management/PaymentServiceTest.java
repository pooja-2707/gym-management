package com.gym.management;

import com.gym.management.entity.Payment;
import com.gym.management.repository.PaymentRepository;
import com.gym.management.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Should return total revenue sum")
    void testGetTotalRevenue() {
        when(paymentRepository.getTotalRevenue()).thenReturn(15000.0);

        Double totalRevenue = paymentService.getTotalRevenue();

        assertThat(totalRevenue).isEqualTo(15000.0);
    }

    @Test
    @DisplayName("Should calculate monthly revenue for current month")
    void testGetMonthlyRevenue() {
        when(paymentRepository.getRevenueByMonthAndYear(anyInt(), anyInt())).thenReturn(5000.0);

        Double monthlyRevenue = paymentService.getMonthlyRevenue();

        assertThat(monthlyRevenue).isEqualTo(5000.0);
    }
}
