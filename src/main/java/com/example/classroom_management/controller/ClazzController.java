package com.example.classroom_management.controller;

import com.example.classroom_management.entity.Clazz;
import com.example.classroom_management.service.ClazzService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequiredArgsConstructor
@RequestMapping("/classes")

public class ClazzController {
    private final ClazzService clazzService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", clazzService.getAll());
        return "class-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("clazz", new Clazz());
        return "class-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("clazz") Clazz clazz,
                       BindingResult result) {

        if (result.hasErrors()) {
            return "class-form";
        }

        clazzService.save(clazz);
        return "redirect:/classes";
    }
}
