package com.gym.management.service;

/*
 * =============================================
 * WHY THIS CLASS EXISTS (MemberService)
 * =============================================
 * 
 * This is a SERVICE class. It sits BETWEEN the Controller and Repository.
 * 
 * ARCHITECTURE:
 *   Controller → Service → Repository → Database
 * 
 * WHY NOT let Controller talk to Repository directly?
 * 
 * 1. BUSINESS LOGIC: Sometimes saving a member requires extra steps
 *    (e.g., checking for duplicate phone numbers, sending a welcome email).
 *    These rules belong in the Service, not the Controller.
 * 
 * 2. REUSABILITY: Multiple controllers might need the same logic.
 *    PaymentController might need to look up a member.
 *    SubscriptionController might need to look up a member.
 *    Both call MemberService — no code duplication!
 * 
 * 3. TESTING: Services are easier to test than Controllers.
 *    You can test business logic without starting a web server.
 * 
 * REAL-LIFE ANALOGY:
 *   Restaurant:
 *   - Waiter (Controller) → Takes order from customer
 *   - Kitchen (Service) → Prepares the food, applies recipes
 *   - Storeroom (Repository) → Provides raw ingredients
 * 
 * =============================================
 * WHY THIS FOLDER EXISTS (service/)
 * =============================================
 * 
 * The "service" package holds all Service classes.
 * Each module has its own Service:
 * - MemberService → business logic for members
 * - PaymentService → business logic for payments
 */

import com.gym.management.entity.Member;
import com.gym.management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * @Service
 * ────────
 * WHY: Tells Spring "this class contains business logic."
 * Spring creates ONE instance of this class and keeps it in memory.
 * Whenever a Controller needs it, Spring injects it automatically.
 * 
 * This is called "Dependency Injection" — one of Spring's superpowers.
 */
@Service
public class MemberService {

    /*
     * @Autowired
     * ──────────
     * WHY: Tells Spring "inject the MemberRepository instance here."
     * 
     * Spring automatically:
     * 1. Creates a MemberRepository implementation (remember, we only wrote an interface!)
     * 2. Stores it in its container
     * 3. Injects it into this field
     * 
     * Without @Autowired, memberRepository would be null, and we'd get NullPointerException.
     * 
     * This is DEPENDENCY INJECTION:
     * Instead of us creating: MemberRepository repo = new MemberRepository();
     * Spring creates it and gives it to us. We just declare we need it.
     */
    @Autowired
    private MemberRepository memberRepository;

    // ─── CREATE & UPDATE ───

    /*
     * Save a new member OR update an existing one.
     * 
     * HOW IT WORKS:
     * - If member.getId() is null → INSERT (new member)
     * - If member.getId() is not null → UPDATE (existing member)
     * 
     * JpaRepository.save() handles both cases automatically!
     * 
     * @param member The member object to save
     * @return The saved member (with generated ID if new)
     */
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    // ─── READ (ALL) ───

    /*
     * Get all members from the database.
     * 
     * EQUIVALENT SQL: SELECT * FROM member
     * 
     * @return List of all members
     */
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // ─── READ (ONE) ───

    /*
     * Find a member by their ID.
     * 
     * WHY Optional<Member>?
     * ─────────────────────
     * What if someone requests member with id=999, but it doesn't exist?
     * Without Optional, we'd return null, and the Controller might crash.
     * 
     * Optional is like a box that may or may not contain a Member.
     * - Optional.isPresent() → true if member exists
     * - Optional.get() → returns the member
     * - Optional.orElse(null) → returns member if exists, null if not
     * 
     * This forces us to HANDLE the "not found" case explicitly.
     * 
     * @param id The member's ID
     * @return Optional containing the member, or empty if not found
     */
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // ─── DELETE ───

    /*
     * Delete a member by their ID.
     * 
     * EQUIVALENT SQL: DELETE FROM member WHERE id = ?
     * 
     * @param id The member's ID to delete
     */
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // ─── SEARCH ───

    /*
     * Search members by name or phone number.
     * 
     * This uses the custom query method we defined in MemberRepository.
     * Spring generates the SQL: WHERE name LIKE '%keyword%' OR phone LIKE '%keyword%'
     * 
     * @param keyword The search term
     * @return List of matching members
     */
    public List<Member> searchMembers(String keyword) {
        return memberRepository.findByNameContainingIgnoreCaseOrPhoneContaining(keyword, keyword);
    }

    // ─── COUNT ───

    /*
     * Get total number of members.
     * 
     * EQUIVALENT SQL: SELECT COUNT(*) FROM member
     * 
     * Used by: Dashboard → "Total Members" card
     */
    public long getTotalMembers() {
        return memberRepository.count();
    }

    // ─── RECENT MEMBERS ───

    /*
     * Get the 5 most recently joined members.
     * 
     * Used by: Dashboard → "Recent Members" table
     */
    public List<Member> getRecentMembers() {
        return memberRepository.findTop5ByOrderByJoinDateDesc();
    }
}
