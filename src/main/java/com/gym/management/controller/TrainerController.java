package com.gym.management.controller;

import com.gym.management.entity.Trainer;
import com.gym.management.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping("")
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        model.addAttribute("pageTitle", "Trainers");
        model.addAttribute("activePage", "trainers");
        return "trainers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        model.addAttribute("pageTitle", "Add Trainer");
        model.addAttribute("activePage", "trainers");
        return "trainers/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<Trainer> trainer = trainerService.getTrainerById(id);
        if (trainer.isPresent()) {
            model.addAttribute("trainer", trainer.get());
            model.addAttribute("pageTitle", "Edit Trainer");
            model.addAttribute("activePage", "trainers");
            return "trainers/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Trainer not found!");
        return "redirect:/trainers";
    }

    @PostMapping("/save")
    public String saveTrainer(@Valid @ModelAttribute("trainer") Trainer trainer,
                              BindingResult result, Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", trainer.getId() != null ? "Edit Trainer" : "Add Trainer");
            model.addAttribute("activePage", "trainers");
            return "trainers/form";
        }
        boolean isNew = (trainer.getId() == null);
        trainerService.saveTrainer(trainer);
        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Trainer added successfully!" : "Trainer updated successfully!");
        return "redirect:/trainers";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            trainerService.deleteTrainer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Trainer deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete trainer.");
        }
        return "redirect:/trainers";
    }
}
