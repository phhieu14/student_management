package com.example.classroom_management.service;

import com.example.classroom_management.entity.Student;
import com.example.classroom_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor

public class StudentService {
    private final StudentRepository studentRepository;

    // Tạo sinh viên
    public void create(Student student) {
        studentRepository.save(student);
    }

    // Kiểm tra trùng mã sinh viên
    public boolean existsByCode(String code, String name) {
        return studentRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(code, name) ;
    }

    // Lọc theo name hoặc code


    // Update trạng thái nhiều sinh viên cùng lúc

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Kiểm tra trùng mã khi đang UPDATE (bỏ qua chính nó)
    public boolean existsByCodeExceptId(String code, Long id) {
        return studentRepository.existsByCodeAndIdNot(code, id);
    }

    // Update sinh viên
    public Student update(Long id, Student updated) {

        Student existing = getById(id);

        existing.setName(updated.getName());
        existing.setCode(updated.getCode());
        existing.setStatus(updated.getStatus());
        existing.setClazz(updated.getClazz());

        return studentRepository.save(existing);
    }


    // Search theo name / code
    public List<Student> filter(String name, String code) {

        if ((name == null || name.isEmpty()) && (code == null || code.isEmpty())) {
            return studentRepository.findAll();
        }

        return studentRepository
                .searchByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(name, code);
    }

    // Update status hàng loạt
    public void updateStatus(List<Long> ids, String status) {

        for (Long id : ids) {
            Student s = getById(id);
            s.setStatus(status);
            studentRepository.save(s);
        }
    }
}