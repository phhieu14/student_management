package com.example.classroom_management.repository;

import com.example.classroom_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    public List<Student> findByNameContaining(String code);
    public List<Student> findByCodeContaining(String code);
    boolean findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);
    // Tìm theo ID
    Optional<Student> findById(Long id);

    // Kiểm tra trùng code khi UPDATE (bỏ qua chính nó)
    boolean existsByCodeAndIdNot(String code, Long id);

    // Search name hoặc code
    List<Student> searchByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);
    boolean existsByCode(String code);

}
