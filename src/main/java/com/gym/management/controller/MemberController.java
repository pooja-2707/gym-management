package com.gym.management.controller;

/*
 * =============================================
 * WHY THIS CLASS EXISTS (MemberController)
 * =============================================
 * 
 * This Controller handles ALL HTTP requests related to Members:
 * 
 * URL                    HTTP Method   Action
 * ────────────────────   ───────────   ──────────────────────
 * /members               GET           Show list of all members
 * /members/add           GET           Show "Add Member" form
 * /members/save          POST          Save new or updated member
 * /members/edit/{id}     GET           Show "Edit Member" form
 * /members/view/{id}     GET           Show member details
 * /members/delete/{id}   GET           Delete a member
 * /members/search        GET           Search members
 * 
 * FLOW EXAMPLE (Adding a new member):
 * 1. Admin clicks "Add Member" button
 * 2. Browser sends GET /members/add
 * 3. showAddForm() creates an empty Member object and sends it to form.html
 * 4. Admin fills the form and clicks "Save"
 * 5. Browser sends POST /members/save with form data
 * 6. saveMember() receives the data, validates it, saves to DB
 * 7. Redirects to /members with "Member added successfully!" message
 */

import com.gym.management.entity.Member;
import com.gym.management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/members")
/*
 * @RequestMapping("/members")
 * ───────────────────────────
 * WHY: Sets the BASE URL for all methods in this controller.
 * All URLs in this class start with "/members".
 * So @GetMapping("/add") becomes /members/add
 * And @GetMapping("") becomes /members
 */
public class MemberController {

    @Autowired
    private MemberService memberService;

    // ═══════════════════════════════════════
    // 1. LIST ALL MEMBERS
    // ═══════════════════════════════════════
    /*
     * @GetMapping("")
     * ───────────────
     * Maps to: GET /members
     * When: Admin clicks "Members" in the sidebar
     * 
     * Model model
     * ───────────
     * The Model carries data from Controller → HTML template.
     * We add the list of members to the model.
     * In HTML, we access it using th:each="member : ${members}"
     */
    @GetMapping("")
    public String listMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        
        // Add data to model (HTML can access these using ${members})
        model.addAttribute("members", members);
        model.addAttribute("pageTitle", "Members");
        model.addAttribute("activePage", "members");
        
        return "members/list";
        // → Spring loads templates/members/list.html
    }

    // ═══════════════════════════════════════
    // 2. SHOW ADD FORM
    // ═══════════════════════════════════════
    /*
     * WHY do we send an EMPTY Member object to the form?
     * ──────────────────────────────────────────────────
     * Thymeleaf's form binding (th:object="${member}") requires 
     * an object to bind the form fields to. 
     * 
     * For "Add" → we send a new, empty Member
     * For "Edit" → we send the existing Member (pre-filled with data)
     * 
     * The same form.html works for both Add and Edit!
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("pageTitle", "Add Member");
        model.addAttribute("activePage", "members");
        
        return "members/form";
    }

    // ═══════════════════════════════════════
    // 3. SHOW EDIT FORM
    // ═══════════════════════════════════════
    /*
     * @PathVariable Long id
     * ─────────────────────
     * WHY: Extracts the {id} from the URL.
     * URL: /members/edit/5 → id = 5
     * 
     * We fetch the member from DB and send it to the form.
     * The form fields will be PRE-FILLED with the member's existing data.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, 
                                RedirectAttributes redirectAttributes) {
        Optional<Member> memberOptional = memberService.getMemberById(id);
        
        // Check if member exists
        if (memberOptional.isPresent()) {
            model.addAttribute("member", memberOptional.get());
            model.addAttribute("pageTitle", "Edit Member");
            model.addAttribute("activePage", "members");
            return "members/form";
        } else {
            // Member not found → redirect to list with error message
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found!");
            return "redirect:/members";
        }
    }

    // ═══════════════════════════════════════
    // 4. SAVE MEMBER (Add or Update)
    // ═══════════════════════════════════════
    /*
     * @PostMapping("/save")
     * ─────────────────────
     * WHY POST? Because we're SENDING data (form submission).
     * GET is for READING data, POST is for CREATING/UPDATING data.
     * 
     * @Valid
     * ──────
     * WHY: Triggers the validation annotations on the Member entity.
     * Remember @NotBlank, @Min, @Max, @Email? This activates them.
     * If validation fails, errors go into BindingResult.
     * 
     * @ModelAttribute Member member
     * ──────────────────────────────
     * WHY: Spring automatically fills this Member object with form data.
     * If the form has <input name="name" value="Rahul">,
     * Spring calls member.setName("Rahul") automatically.
     * This is called "Form Binding" or "Data Binding."
     * 
     * BindingResult result
     * ────────────────────
     * WHY: Contains validation errors (if any).
     * If result.hasErrors() is true, we show the form again with error messages.
     * 
     * RedirectAttributes
     * ──────────────────
     * WHY: Used to send one-time messages after a redirect.
     * "Member added successfully!" → shows once, disappears on page refresh.
     * These are called "Flash Attributes."
     */
    @PostMapping("/save")
    public String saveMember(@Valid @ModelAttribute("member") Member member,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        // If validation errors exist, show the form again with errors
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", member.getId() != null ? "Edit Member" : "Add Member");
            model.addAttribute("activePage", "members");
            return "members/form";
        }
        
        // Determine if this is a new member or an update
        boolean isNew = (member.getId() == null);
        
        // Save to database
        memberService.saveMember(member);
        
        // Set success message
        if (isNew) {
            redirectAttributes.addFlashAttribute("successMessage", "Member added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Member updated successfully!");
        }
        
        // Redirect to the members list
        // WHY redirect and not return "members/list"?
        // → To prevent duplicate form submission on browser refresh (PRG Pattern)
        // PRG = Post → Redirect → Get
        return "redirect:/members";
    }

    // ═══════════════════════════════════════
    // 5. VIEW MEMBER DETAILS
    // ═══════════════════════════════════════
    @GetMapping("/view/{id}")
    public String viewMember(@PathVariable Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        Optional<Member> memberOptional = memberService.getMemberById(id);
        
        if (memberOptional.isPresent()) {
            model.addAttribute("member", memberOptional.get());
            model.addAttribute("pageTitle", "View Member");
            model.addAttribute("activePage", "members");
            return "members/view";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found!");
            return "redirect:/members";
        }
    }

    // ═══════════════════════════════════════
    // 6. DELETE MEMBER
    // ═══════════════════════════════════════
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id, 
                               RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(id);
            redirectAttributes.addFlashAttribute("successMessage", "Member deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Cannot delete member. They may have subscriptions, payments, or attendance records.");
        }
        return "redirect:/members";
    }

    // ═══════════════════════════════════════
    // 7. SEARCH MEMBERS
    // ═══════════════════════════════════════
    /*
     * @RequestParam String keyword
     * ─────────────────────────────
     * WHY: Extracts query parameters from the URL.
     * URL: /members/search?keyword=Rahul → keyword = "Rahul"
     * 
     * This is different from @PathVariable:
     * - @PathVariable → /members/edit/5 (part of the URL path)
     * - @RequestParam → /members/search?keyword=Rahul (query string)
     */
    @GetMapping("/search")
    public String searchMembers(@RequestParam(value = "keyword", required = false) String keyword,
                                Model model) {
        List<Member> members;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            members = memberService.searchMembers(keyword.trim());
            model.addAttribute("keyword", keyword);
        } else {
            members = memberService.getAllMembers();
        }
        
        model.addAttribute("members", members);
        model.addAttribute("pageTitle", "Members");
        model.addAttribute("activePage", "members");
        
        return "members/list";
    }
}
