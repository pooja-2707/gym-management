package com.gym.management.controller;

import com.gym.management.entity.Attendance;
import com.gym.management.service.AttendanceService;
import com.gym.management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public String listAttendance(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "memberId", required = false) Long memberId,
            Model model) {

        List<Attendance> attendanceList;

        if (date != null) {
            attendanceList = attendanceService.getAttendanceByDate(date);
            model.addAttribute("filterDate", date);
        } else if (memberId != null) {
            attendanceList = attendanceService.getAttendanceByMember(memberId);
            model.addAttribute("filterMemberId", memberId);
        } else {
            attendanceList = attendanceService.getAllAttendance();
        }

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("pageTitle", "Attendance");
        model.addAttribute("activePage", "attendance");
        return "attendance/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.now());
        model.addAttribute("attendance", attendance);
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("pageTitle", "Mark Attendance");
        model.addAttribute("activePage", "attendance");
        return "attendance/form";
    }

    @PostMapping("/save")
    public String saveAttendance(@Valid @ModelAttribute("attendance") Attendance attendance,
                                  BindingResult result, Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.getAllMembers());
            model.addAttribute("pageTitle", "Mark Attendance");
            model.addAttribute("activePage", "attendance");
            return "attendance/form";
        }
        attendanceService.saveAttendance(attendance);
        redirectAttributes.addFlashAttribute("successMessage", "Attendance marked successfully!");
        return "redirect:/attendance";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            attendanceService.deleteAttendance(id);
            redirectAttributes.addFlashAttribute("successMessage", "Attendance record deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete attendance record.");
        }
        return "redirect:/attendance";
    }
}
