package com.example.classroom_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Column(unique = true)
    private String code;

    private String status; // ACTIVE / INACTIVE

    @Email
    private String email;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Clazz clazz;

}

