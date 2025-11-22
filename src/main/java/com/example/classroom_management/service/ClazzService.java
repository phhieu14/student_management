package com.example.classroom_management.service;

import com.example.classroom_management.entity.Clazz;
import com.example.classroom_management.repository.ClazzRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClazzService {
    private final ClazzRepository clazzRepository;

    // Lấy tất cả lớp học
    public List<Clazz> getAll() {
        return clazzRepository.findAll();
    }

    // Lưu lớp mới
    public void save(Clazz clazz) {
        clazzRepository.save(clazz);
    }
}
