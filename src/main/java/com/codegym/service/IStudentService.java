package com.codegym.service;

import com.codegym.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    Page<Student> findAll(String name, String className, Pageable pageable);

    Optional<Student> findById(Long id);

    void save(Student student);

    void delete(Long id);

    List<String> getAllClasses();


    Page<Student> findByNameContainingIgnoreCaseAndClassName(String name, String className, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByClassName(String className, Pageable pageable);

    Page<Student> findAll(Pageable pageable);
}
