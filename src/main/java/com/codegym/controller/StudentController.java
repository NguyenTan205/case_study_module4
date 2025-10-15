package com.codegym.controller;

import com.codegym.model.Student;
import com.codegym.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/home")
    public String studentHome() {
        return "student/home";
    }

    // ✅ Danh sách sinh viên + lọc + phân trang
//    @GetMapping
//    public String listStudents(@RequestParam(defaultValue = "") String name,
//                               @RequestParam(defaultValue = "") String className,
//                               @RequestParam(defaultValue = "0") int page,
//                               @RequestParam(defaultValue = "5") int size,
//                               Model model) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Student> students = studentService.findAll(name, className, pageable);
//
//        model.addAttribute("students", students);
//        model.addAttribute("name", name);
//        model.addAttribute("className", className);
//        model.addAttribute("classes", studentService.getAllClasses());
//        return "student/list";
//    }

    @GetMapping
    public String listStudents(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String className,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Student> students;

        if (name != null && !name.isEmpty() && className != null && !className.isEmpty()) {
            students = studentService.findByNameContainingIgnoreCaseAndClassName(name, className, pageable);
        } else if (name != null && !name.isEmpty()) {
            students = studentService.findByNameContainingIgnoreCase(name, pageable);
        } else if (className != null && !className.isEmpty()) {
            students = studentService.findByClassName(className, pageable);
        } else {
            students = studentService.findAll(pageable);
        }

        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        return "student/list";
    }

    // ✅ Tìm kiếm theo tên
    @GetMapping("/search")
    public String searchByName(@RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.findAll(name, "", pageable);

        model.addAttribute("students", students);
        model.addAttribute("name", name);
        model.addAttribute("className", "");
        model.addAttribute("classes", studentService.getAllClasses());
        return "student/list";
    }

    // ✅ Lọc theo lớp
    @GetMapping("/filter")
    public String filterByClass(@RequestParam(defaultValue = "") String className,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.findAll("", className, pageable);

        model.addAttribute("students", students);
        model.addAttribute("name", "");
        model.addAttribute("className", className);
        model.addAttribute("classes", studentService.getAllClasses());
        return "student/list";
    }

    // ✅ Chỉ ADMIN & TEACHER được thêm mới
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        return "student/form";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "student/form";
        }
        studentService.save(student);
        redirect.addFlashAttribute("success", "✅ Lưu sinh viên thành công!");
        return "redirect:/students";
    }

    // ✅ Chỉ ADMIN & TEACHER được sửa
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên ID: " + id));
        model.addAttribute("student", student);
        return "student/form";
    }

    // ✅ Chỉ ADMIN & TEACHER được xoá
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirect) {
        studentService.delete(id);
        redirect.addFlashAttribute("success", "🗑️ Xoá sinh viên thành công!");
        return "redirect:/students";
    }
}
