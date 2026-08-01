package com.gym.management;

/*
 * Unit tests for SubscriptionService business rules:
 * - End date calculation: startDate + plan.durationInMonths
 * - Status calculation: Active if end date >= today, Expired if end date < today
 */

import com.gym.management.entity.Member;
import com.gym.management.entity.MembershipPlan;
import com.gym.management.entity.Subscription;
import com.gym.management.repository.SubscriptionRepository;
import com.gym.management.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private Member sampleMember;
    private MembershipPlan quarterlyPlan;

    @BeforeEach
    void setUp() {
        sampleMember = new Member();
        sampleMember.setId(1L);
        sampleMember.setName("Amit Kumar");

        quarterlyPlan = new MembershipPlan();
        quarterlyPlan.setId(1L);
        quarterlyPlan.setPlanName("Quarterly Plan");
        quarterlyPlan.setDurationInMonths(3);
        quarterlyPlan.setPrice(2500.0);
    }

    @Test
    @DisplayName("Should calculate end date and set Active status when start date is today")
    void testSaveSubscriptionActive() {
        Subscription subscription = new Subscription();
        subscription.setMember(sampleMember);
        subscription.setPlan(quarterlyPlan);
        subscription.setStartDate(LocalDate.now());

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription saved = subscriptionService.saveSubscription(subscription);

        assertThat(saved.getEndDate()).isEqualTo(LocalDate.now().plusMonths(3));
        assertThat(saved.getStatus()).isEqualTo("Active");
    }

    @Test
    @DisplayName("Should set Expired status when subscription end date is in the past")
    void testSaveSubscriptionExpired() {
        Subscription subscription = new Subscription();
        subscription.setMember(sampleMember);
        subscription.setPlan(quarterlyPlan);
        // Start date 6 months ago → end date 3 months ago (Expired)
        subscription.setStartDate(LocalDate.now().minusMonths(6));

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription saved = subscriptionService.saveSubscription(subscription);

        assertThat(saved.getStatus()).isEqualTo("Expired");
    }
}
