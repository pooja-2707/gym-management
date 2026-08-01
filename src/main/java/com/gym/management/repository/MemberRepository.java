package com.gym.management.repository;

/*
 * =============================================
 * WHY THIS CLASS EXISTS (MemberRepository)
 * =============================================
 * 
 * This is a REPOSITORY interface. It handles all DATABASE OPERATIONS
 * for the Member entity (member table).
 * 
 * WHAT PROBLEM IT SOLVES:
 * Without Spring Data JPA, you'd have to write code like this:
 * 
 *   Connection conn = DriverManager.getConnection(url, user, password);
 *   PreparedStatement ps = conn.prepareStatement("SELECT * FROM member WHERE id = ?");
 *   ps.setLong(1, id);
 *   ResultSet rs = ps.executeQuery();
 *   // ... manually map each column to a Java field
 *   // ... handle exceptions
 *   // ... close connection
 * 
 * That's 20+ lines of boring, repetitive code for EVERY query!
 * 
 * WITH Spring Data JPA, we just write:
 *   memberRepository.findById(id);
 * 
 * Spring generates ALL the SQL and database code automatically.
 * We don't write a single line of SQL!
 * 
 * =============================================
 * HOW IT WORKS
 * =============================================
 * 
 * JpaRepository<Member, Long>
 *   - Member → The Entity class this repository manages
 *   - Long   → The data type of the Primary Key (id)
 * 
 * By extending JpaRepository, we AUTOMATICALLY get these methods:
 * 
 *   findAll()         → SELECT * FROM member
 *   findById(id)      → SELECT * FROM member WHERE id = ?
 *   save(member)      → INSERT INTO member (...) VALUES (...)
 *                        OR UPDATE member SET ... WHERE id = ?
 *   deleteById(id)    → DELETE FROM member WHERE id = ?
 *   count()           → SELECT COUNT(*) FROM member
 *   existsById(id)    → SELECT EXISTS(SELECT 1 FROM member WHERE id = ?)
 * 
 * We get ALL of these for FREE just by extending JpaRepository!
 * 
 * =============================================
 * WHY THIS FOLDER EXISTS (repository/)
 * =============================================
 * 
 * The "repository" package holds all Repository interfaces.
 * Each entity has its own repository:
 * - MemberRepository → database operations for Member
 * - PaymentRepository → database operations for Payment
 * 
 * This separation follows the REPOSITORY PATTERN:
 * "Keep database logic separate from business logic."
 */

import com.gym.management.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @Repository
 * ───────────
 * WHY: Tells Spring "this is a data access component."
 * Spring will create an implementation of this interface at runtime.
 * Yes — we write an INTERFACE, and Spring creates the CLASS automatically!
 * 
 * That's the magic of Spring Data JPA.
 * 
 * NOTE: @Repository is optional here because JpaRepository 
 * already tells Spring this is a repository. But we add it 
 * for clarity and best practice.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
     * ═══════════════════════════════════════
     * CUSTOM QUERY METHODS
     * ═══════════════════════════════════════
     * 
     * Spring Data JPA can CREATE queries from METHOD NAMES!
     * This is called "Query Derivation" or "Method Name Magic."
     * 
     * HOW IT WORKS:
     * Method name: findByNameContainingIgnoreCase(String name)
     * Spring reads: find + By + Name + Containing + IgnoreCase
     * Spring generates: SELECT * FROM member WHERE LOWER(name) LIKE LOWER('%name%')
     * 
     * We don't write ANY SQL! Spring creates it from the method name!
     * 
     * Common keywords:
     * - findBy___           → WHERE ___ = ?
     * - findBy___Containing → WHERE ___ LIKE '%?%'
     * - findBy___IgnoreCase → case-insensitive comparison
     * - findBy___Or___      → WHERE ___ = ? OR ___ = ?
     * - findBy___OrderBy___ → ORDER BY ___
     * - countBy___          → COUNT(*) WHERE ___ = ?
     */

    /*
     * Search members by name (partial match, case-insensitive)
     * 
     * Example: findByNameContainingIgnoreCase("rah")
     * → Returns members named "Rahul", "RAHUL", "Surahman", etc.
     * 
     * Used by: Search feature on the Members list page
     */
    List<Member> findByNameContainingIgnoreCaseOrPhoneContaining(String name, String phone);

    /*
     * Find top 5 members ordered by join date (newest first)
     * 
     * Used by: Dashboard → "Recent Members" table
     */
    List<Member> findTop5ByOrderByJoinDateDesc();
}
