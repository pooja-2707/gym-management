package com.gym.management.service;

import com.gym.management.entity.MembershipPlan;
import com.gym.management.repository.MembershipPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipPlanService {

    @Autowired
    private MembershipPlanRepository planRepository;

    public MembershipPlan savePlan(MembershipPlan plan) {
        return planRepository.save(plan);
    }

    public List<MembershipPlan> getAllPlans() {
        return planRepository.findAll();
    }

    public Optional<MembershipPlan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    public long getTotalPlans() {
        return planRepository.count();
    }
}
