package com.gym.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "trainer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Trainer name is required")
    private String name;

    @Column(nullable = false, length = 15, unique = true)
    @NotBlank(message = "Phone number is required")
    private String phone;

    @Column(nullable = false)
    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary cannot be negative")
    private Double salary;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Specialization is required")
    private String specialization;
}
