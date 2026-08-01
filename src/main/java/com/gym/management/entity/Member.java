package com.gym.management.entity;

/*
 * =============================================
 * WHY THIS CLASS EXISTS (Member.java - Entity)
 * =============================================
 * 
 * This is an ENTITY class. It represents the "member" table in MySQL.
 * 
 * HOW IT WORKS:
 * - Each FIELD in this class → becomes a COLUMN in the database table
 * - Each OBJECT of this class → becomes a ROW in the database table
 * 
 * EXAMPLE:
 *   Member member = new Member();
 *   member.setName("Rahul");
 *   member.setPhone("9876543210");
 *   memberRepository.save(member);
 *   
 *   → This creates a new ROW in the "member" table with name="Rahul"
 * 
 * =============================================
 * WHY THIS FOLDER EXISTS (entity/)
 * =============================================
 * 
 * The "entity" package holds all Entity classes (database models).
 * Each table in our database has one Entity class:
 * - Member.java        → member table
 * - MembershipPlan.java → membership_plan table
 * - Payment.java       → payment table
 * 
 * We keep them separate from Controllers and Services because
 * Entities have a SINGLE responsibility: represent database structure.
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/*
 * ═══════════════════════════════════════
 * ANNOTATION EXPLANATIONS
 * ═══════════════════════════════════════
 * 
 * @Entity
 * ───────
 * WHY: Tells Hibernate "this class maps to a database table."
 * Without this, Hibernate ignores this class completely.
 * 
 * @Table(name = "member")
 * ───────────────────────
 * WHY: Specifies the EXACT table name in MySQL.
 * If we don't use this, Hibernate creates a table called "Member" (class name).
 * We want lowercase "member" to follow database naming conventions.
 * 
 * @Data (from Lombok)
 * ────────────────────
 * WHY: Automatically generates:
 *   - Getters for all fields (getName(), getPhone(), etc.)
 *   - Setters for all fields (setName(), setPhone(), etc.)
 *   - toString() method
 *   - equals() and hashCode() methods
 * Without Lombok, we'd have to write 50+ lines of boilerplate code!
 * 
 * @NoArgsConstructor (from Lombok)
 * ─────────────────────────────────
 * WHY: Creates a no-argument constructor: public Member() { }
 * Hibernate REQUIRES a no-arg constructor to create objects when reading from DB.
 * 
 * @AllArgsConstructor (from Lombok)
 * ──────────────────────────────────
 * WHY: Creates a constructor with ALL fields as parameters.
 * Useful when creating a Member with all values at once.
 */
@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    /*
     * @Id
     * ────
     * WHY: Marks this field as the PRIMARY KEY of the table.
     * Every table MUST have a primary key — a unique identifier for each row.
     * 
     * @GeneratedValue(strategy = GenerationType.IDENTITY)
     * ───────────────────────────────────────────────────
     * WHY: Tells MySQL to AUTO_INCREMENT this field.
     * First member gets id=1, second gets id=2, and so on.
     * We don't set the ID manually — the database generates it.
     * 
     * GenerationType.IDENTITY → Uses MySQL's AUTO_INCREMENT feature.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @Column(nullable = false, length = 100)
     * ────────────────────────────────────────
     * WHY: Customizes the database column.
     * - nullable = false → This column CANNOT be empty (NOT NULL in SQL)
     * - length = 100     → Maximum 100 characters (VARCHAR(100) in SQL)
     * 
     * @NotBlank(message = "Name is required")
     * ────────────────────────────────────────
     * WHY: This is a VALIDATION annotation (from Jakarta Validation).
     * It checks the value BEFORE saving to the database.
     * If name is blank/null, it shows the error message on the form.
     * 
     * DIFFERENCE between @Column(nullable=false) and @NotBlank:
     * - @Column(nullable=false) → Database-level check (SQL throws error)
     * - @NotBlank → Application-level check (shows friendly error on form)
     * We use BOTH for double safety.
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    private String name;

    /*
     * unique = true → No two members can have the same phone number.
     * In SQL, this creates a UNIQUE constraint on the column.
     */
    @Column(nullable = false, length = 15, unique = true)
    @NotBlank(message = "Phone number is required")
    private String phone;

    @Column(length = 100, unique = true)
    @Email(message = "Please enter a valid email address")
    private String email;

    /*
     * @NotNull vs @NotBlank:
     * - @NotBlank → For Strings (checks not null AND not empty AND not just spaces)
     * - @NotNull  → For numbers, dates (checks not null only)
     * 
     * @Min(10) and @Max(100) → Age must be between 10 and 100.
     * A gym member can't be 5 years old or 150 years old!
     */
    @Column(nullable = false)
    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    /*
     * Gender is stored as a simple String: "Male", "Female", or "Other"
     * We could use an Enum, but a String keeps it simpler for learning.
     */
    @Column(nullable = false, length = 10)
    @NotBlank(message = "Gender is required")
    private String gender;

    @Column(length = 255)
    private String address;

    /*
     * LocalDate → Java 8+ date type (year-month-day, no time).
     * Better than the old java.util.Date because it's simpler and immutable.
     * 
     * In MySQL, this maps to the DATE type.
     * Example: 2026-07-26
     */
    @Column(name = "join_date", nullable = false)
    @NotNull(message = "Join date is required")
    private LocalDate joinDate;
}
