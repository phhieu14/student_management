package com.example.classroom_management.service;

import com.example.classroom_management.entity.Clazz;
import com.example.classroom_management.entity.Student;
import com.example.classroom_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class StudentService {
    private final StudentRepository studentRepository;
    private final ClazzService clazzService;
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
    public int importExcel(MultipartFile file) throws Exception {
        List<Student> students = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        boolean isFirstRow = true;

        for (Row row : sheet) {

            if (isFirstRow) { // skip header
                isFirstRow = false;
                continue;
            }

            String name = row.getCell(0).getStringCellValue();
            String code = row.getCell(1).getStringCellValue();
            String email = row.getCell(2).getStringCellValue();
            String status = row.getCell(3).getStringCellValue();
            Long classId = (long) row.getCell(4).getNumericCellValue();

            Student s = new Student();
            s.setName(name);
            s.setCode(code);
            s.setEmail(email);
            s.setStatus(status);



            students.add(s);
        }

        workbook.close();
        studentRepository.saveAll(students);

        return students.size();
    }
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public boolean existsByCode(String code, String name) {
        return studentRepository.existsByCode(code);
    }

}
