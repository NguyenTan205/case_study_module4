package com.codegym.service;

import com.codegym.model.Student;
import com.codegym.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {

    @Autowired
    private IStudentRepository studentRepository;

    @Override
    public Page<Student> findAll(String name, String className, Pageable pageable) {
        if ((name == null || name.isEmpty()) && (className == null || className.isEmpty())) {
            return studentRepository.findAll(pageable);
        }
        if (name != null && !name.isEmpty() && (className == null || className.isEmpty())) {
            return studentRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        if ((name == null || name.isEmpty()) && className != null && !className.isEmpty()) {
            return studentRepository.findByClassName(className, pageable);
        }
        return studentRepository.findByNameContainingIgnoreCaseAndClassName(name, className, pageable);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<String> getAllClasses() {
        return studentRepository.findAllClassNames();
    }

    @Override
    public Page<Student> findByNameContainingIgnoreCaseAndClassName(String name, String className, Pageable pageable) {
        return studentRepository.findByNameContainingIgnoreCaseAndClassName(name, className, pageable);
    }

    @Override
    public Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return studentRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Student> findByClassName(String className, Pageable pageable) {
        return studentRepository.findByClassName(className, pageable);
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
}
