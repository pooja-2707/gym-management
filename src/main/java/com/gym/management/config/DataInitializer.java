package com.gym.management.config;

import com.gym.management.entity.*;
import com.gym.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MembershipPlanRepository planRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=================================================");
        System.out.println(">>> CHECKING & SEEDING DEMO DATA IN MYSQL DB <<<");
        System.out.println("=================================================");

        // 1. Seed Admin
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gympro.com");
            adminRepository.save(admin);
            System.out.println("✔ Default Admin created: admin / admin123");
        }

        // 2. Seed Membership Plans
        if (planRepository.count() == 0) {
            planRepository.save(new MembershipPlan(null, "Basic Monthly", 1, 1500.0));
            planRepository.save(new MembershipPlan(null, "Quarterly Pro", 3, 4000.0));
            planRepository.save(new MembershipPlan(null, "Half-Yearly Elite", 6, 7500.0));
            planRepository.save(new MembershipPlan(null, "Annual VIP Champion", 12, 14000.0));
            System.out.println("✔ Membership Plans seeded.");
        }

        // 3. Seed Trainers
        if (trainerRepository.count() == 0) {
            trainerRepository.save(new Trainer(null, "Marcus Vance", "+1 555-0101", 45000.0, "Bodybuilding & Hypertrophy"));
            trainerRepository.save(new Trainer(null, "Elena Rostova", "+1 555-0102", 42000.0, "HIIT & Functional Fitness"));
            trainerRepository.save(new Trainer(null, "Darius Cole", "+1 555-0103", 38000.0, "Boxing & Conditioning"));
            trainerRepository.save(new Trainer(null, "Sophia Chen", "+1 555-0104", 39000.0, "Yoga, Mobility & Rehab"));
            trainerRepository.save(new Trainer(null, "Lucas Thorne", "+1 555-0105", 46000.0, "Powerlifting & Strength"));
            System.out.println("✔ Trainers seeded.");
        }

        // 4. Seed Members
        if (memberRepository.count() == 0) {
            Member m1 = memberRepository.save(new Member(null, "Alex Mercer", "+1 555-1101", "alex.mercer@gmail.com", 27, "Male", "742 Evergreen Terrace", LocalDate.of(2026, 6, 1)));
            Member m2 = memberRepository.save(new Member(null, "Samantha Hayes", "+1 555-1102", "sam.hayes@outlook.com", 24, "Female", "1042 Elm Street", LocalDate.of(2026, 7, 10)));
            Member m3 = memberRepository.save(new Member(null, "David Miller", "+1 555-1103", "d.miller@techcorp.io", 34, "Male", "52 Ocean Avenue", LocalDate.of(2026, 7, 20)));
            Member m4 = memberRepository.save(new Member(null, "Olivia Taylor", "+1 555-1104", "olivia.taylor@gmail.com", 29, "Female", "303 Sunset Boulevard", LocalDate.of(2026, 8, 1)));
            Member m5 = memberRepository.save(new Member(null, "Liam Walker", "+1 555-1105", "liam.w@fitness.net", 22, "Male", "18 Baker Street", LocalDate.of(2026, 8, 5)));
            Member m6 = memberRepository.save(new Member(null, "Emma Wilson", "+1 555-1106", "emma.wilson@yahoo.com", 31, "Female", "88 Pine Crest Way", LocalDate.of(2026, 8, 10)));
            Member m7 = memberRepository.save(new Member(null, "Noah Jenkins", "+1 555-1107", "noah.jenkins@gmail.com", 38, "Male", "412 Maple Ridge", LocalDate.of(2026, 8, 12)));
            Member m8 = memberRepository.save(new Member(null, "Ava Robinson", "+1 555-1108", "ava.robinson@icloud.com", 26, "Female", "95 Cedar Lane", LocalDate.of(2026, 8, 14)));
            System.out.println("✔ Members seeded.");

            // 5. Seed Subscriptions
            if (subscriptionRepository.count() == 0) {
                List<MembershipPlan> plans = planRepository.findAll();
                if (!plans.isEmpty()) {
                    MembershipPlan monthly = plans.stream().filter(p -> p.getDurationInMonths() == 1).findFirst().orElse(plans.get(0));
                    MembershipPlan quarterly = plans.stream().filter(p -> p.getDurationInMonths() == 3).findFirst().orElse(plans.get(0));
                    MembershipPlan halfYearly = plans.stream().filter(p -> p.getDurationInMonths() == 6).findFirst().orElse(plans.get(0));
                    MembershipPlan annual = plans.stream().filter(p -> p.getDurationInMonths() == 12).findFirst().orElse(plans.get(0));

                    // Expired subscription
                    subscriptionRepository.save(new Subscription(null, m1, monthly, LocalDate.of(2026, 6, 1), LocalDate.of(2026, 7, 1), "Expired"));
                    
                    // Active subscriptions
                    subscriptionRepository.save(new Subscription(null, m2, quarterly, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 10, 10), "Active"));
                    subscriptionRepository.save(new Subscription(null, m3, annual, LocalDate.of(2026, 7, 20), LocalDate.of(2027, 7, 20), "Active"));
                    subscriptionRepository.save(new Subscription(null, m4, halfYearly, LocalDate.of(2026, 8, 1), LocalDate.of(2027, 2, 1), "Active"));
                    subscriptionRepository.save(new Subscription(null, m5, monthly, LocalDate.of(2026, 8, 5), LocalDate.of(2026, 9, 5), "Active"));
                    subscriptionRepository.save(new Subscription(null, m6, quarterly, LocalDate.of(2026, 8, 10), LocalDate.of(2026, 11, 10), "Active"));
                    subscriptionRepository.save(new Subscription(null, m7, monthly, LocalDate.of(2026, 8, 12), LocalDate.of(2026, 9, 12), "Active"));
                    subscriptionRepository.save(new Subscription(null, m8, annual, LocalDate.of(2026, 8, 14), LocalDate.of(2027, 8, 14), "Active"));
                    System.out.println("✔ Subscriptions seeded.");
                }
            }

            // 6. Seed Payments
            if (paymentRepository.count() == 0) {
                paymentRepository.save(new Payment(null, m1, 1500.0, LocalDate.of(2026, 6, 1), "Cash"));
                paymentRepository.save(new Payment(null, m2, 4000.0, LocalDate.of(2026, 7, 10), "UPI"));
                paymentRepository.save(new Payment(null, m3, 14000.0, LocalDate.of(2026, 7, 20), "Card"));
                paymentRepository.save(new Payment(null, m4, 7500.0, LocalDate.of(2026, 8, 1), "Online"));
                paymentRepository.save(new Payment(null, m5, 1500.0, LocalDate.of(2026, 8, 5), "UPI"));
                paymentRepository.save(new Payment(null, m6, 4000.0, LocalDate.of(2026, 8, 10), "Card"));
                paymentRepository.save(new Payment(null, m7, 1500.0, LocalDate.of(2026, 8, 12), "Cash"));
                paymentRepository.save(new Payment(null, m8, 14000.0, LocalDate.of(2026, 8, 14), "Online"));
                System.out.println("✔ Payments seeded.");
            }

            // 7. Seed Attendance
            if (attendanceRepository.count() == 0) {
                LocalDate today = LocalDate.now();
                attendanceRepository.save(new Attendance(null, m2, today, "Present"));
                attendanceRepository.save(new Attendance(null, m3, today, "Present"));
                attendanceRepository.save(new Attendance(null, m4, today, "Present"));
                attendanceRepository.save(new Attendance(null, m5, today, "Present"));
                attendanceRepository.save(new Attendance(null, m6, today, "Absent"));
                attendanceRepository.save(new Attendance(null, m7, today, "Present"));
                attendanceRepository.save(new Attendance(null, m8, today, "Present"));
                
                attendanceRepository.save(new Attendance(null, m2, today.minusDays(1), "Present"));
                attendanceRepository.save(new Attendance(null, m3, today.minusDays(1), "Absent"));
                attendanceRepository.save(new Attendance(null, m4, today.minusDays(1), "Present"));
                attendanceRepository.save(new Attendance(null, m5, today.minusDays(1), "Present"));
                System.out.println("✔ Attendance records seeded.");
            }
        }

        System.out.println("=================================================");
        System.out.println(">>> MYSQL SEEDING COMPLETED SUCCESSFULLY! <<<");
        System.out.println("=================================================");
    }
}
