package com.example.classroom_management.repository;

import com.example.classroom_management.entity.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClazzRepository extends JpaRepository<Clazz,Long> {
}
