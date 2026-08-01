package com.gym.management.repository;

import com.gym.management.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Find all subscriptions for a specific member
    List<Subscription> findByMemberId(Long memberId);

    // Count subscriptions by status
    long countByStatus(String status);

    // Find subscriptions by status
    List<Subscription> findByStatus(String status);
}
