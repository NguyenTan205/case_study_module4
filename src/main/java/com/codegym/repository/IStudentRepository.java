package com.codegym.repository;

import com.codegym.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByClassName(String className, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCaseAndClassName(String name, String className, Pageable pageable);

    @Query("SELECT DISTINCT s.className FROM Student s")
    List<String> findAllClassNames();

}
