package com.example.classroom_management.controller;

import com.example.classroom_management.entity.Student;
import com.example.classroom_management.service.ClazzService;
import com.example.classroom_management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final ClazzService clazzService;

    // =======================
    // 1) DANH SÁCH SINH VIÊN
    // =======================
    @GetMapping
    public String listStudents(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String code,
                               Model model) {

        model.addAttribute("students", studentService.filter(name, code));
        model.addAttribute("name", name);
        model.addAttribute("code", code);

        return "student-list";
    }

    // =======================
    // 2) FORM THÊM SINH VIÊN
    // =======================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", clazzService.getAll());
        return "student-form";
    }

    // =======================
    // 3) SUBMIT FORM
    // =======================
    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            model.addAttribute("classes", clazzService.getAll());
            return "student-form";
        }

        if (studentService.existsByCode(student.getCode(), student.getName())) {
            result.rejectValue("code", "error.code", "Mã sinh viên đã tồn tại");
            model.addAttribute("classes", clazzService.getAll());
            return "student-form";
        } else {
            studentService.create(student);
            return "redirect:/students";
        }


    }

    // =======================
    // 4) UPDATE STATUS HÀNG LOẠT
    // =======================
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("ids") List<Long> ids,
                               @RequestParam("status") String status) {

        studentService.updateStatus(ids, status);
        return "redirect:/students";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        model.addAttribute("classes", clazzService.getAll());
        return "student-form";
    }
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("classes", clazzService.getAll());
            return "student-form";
        }

        // Kiểm tra trùng mã nhưng bỏ qua chính nó
        if (studentService.existsByCodeExceptId(student.getCode(), id)) {
            result.rejectValue("code", "error.code", "Mã sinh viên đã tồn tại");
            model.addAttribute("classes", clazzService.getAll());
            return "student-form";
        }

        studentService.update(id, student);
        return "redirect:/students";
    }
    @PostMapping("/import")
    public String importStudents(@RequestParam("file") MultipartFile file, Model model) {
        try {
            int count = studentService.importExcel(file);
            model.addAttribute("success", "Imported " + count + " students successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Import failed: " + e.getMessage());
        }

        model.addAttribute("students", studentService.getAll());
        return "student-list";
    }

}
