package com.gym.management.service;

import com.gym.management.entity.Subscription;
import com.gym.management.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /*
     * Save subscription with auto-calculated end date and status.
     * Business Logic:
     *   end_date = start_date + plan.durationInMonths
     *   status = "Active" if end_date >= today, else "Expired"
     */
    public Subscription saveSubscription(Subscription subscription) {
        // Auto-calculate end date from start date + plan duration
        if (subscription.getStartDate() != null && subscription.getPlan() != null) {
            LocalDate endDate = subscription.getStartDate()
                    .plusMonths(subscription.getPlan().getDurationInMonths());
            subscription.setEndDate(endDate);
        }

        // Auto-set status based on end date
        updateStatus(subscription);

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        // Update status for all subscriptions (in case dates have passed)
        subscriptions.forEach(this::updateStatus);
        return subscriptions;
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public List<Subscription> getSubscriptionsByMember(Long memberId) {
        return subscriptionRepository.findByMemberId(memberId);
    }

    public long countByStatus(String status) {
        return subscriptionRepository.countByStatus(status);
    }

    public List<Subscription> getSubscriptionsByStatus(String status) {
        return subscriptionRepository.findByStatus(status);
    }

    /*
     * Determine subscription status based on current date.
     * If end date is today or in the future → Active
     * If end date is in the past → Expired
     */
    private void updateStatus(Subscription subscription) {
        if (subscription.getEndDate() != null) {
            if (subscription.getEndDate().isBefore(LocalDate.now())) {
                subscription.setStatus("Expired");
            } else {
                subscription.setStatus("Active");
            }
        }
    }
}
