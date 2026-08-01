package com.gym.management.config;

import com.gym.management.entity.Admin;
import com.gym.management.entity.MembershipPlan;
import com.gym.management.repository.AdminRepository;
import com.gym.management.repository.MembershipPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MembershipPlanRepository planRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed default Admin if none exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gympro.com");
            adminRepository.save(admin);
            System.out.println(">>> Default Admin created: admin / admin123");
        }

        // Seed default Membership Plans if none exist
        if (planRepository.count() == 0) {
            planRepository.save(new MembershipPlan(null, "Monthly Plan", 1, 1000.0));
            planRepository.save(new MembershipPlan(null, "Quarterly Plan", 3, 2500.0));
            planRepository.save(new MembershipPlan(null, "Half-Yearly Plan", 6, 4500.0));
            planRepository.save(new MembershipPlan(null, "Yearly Plan", 12, 8000.0));
            System.out.println(">>> Default Membership Plans pre-seeded.");
        }
    }
}
