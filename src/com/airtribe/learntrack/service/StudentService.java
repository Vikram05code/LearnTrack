package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.repository.StudentRepository;
import com.airtribe.learntrack.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // Overloaded addStudent: with and without email
    public Student addStudent(String firstName, String lastName, String batch) {
        Student student = new Student(IdGenerator.getNextStudentId(), firstName, lastName, batch);
        return repository.save(student);
    }

    public Student addStudent(String firstName, String lastName, String email, String batch) {
        Student student = new Student(IdGenerator.getNextStudentId(), firstName, lastName, email, batch);
        return repository.save(student);
    }

    public List<Student> listStudents() {
        return repository.findAll();
    }

    public List<Student> listActiveStudents() {
        List<Student> result = new ArrayList<>();
        for (Student s : repository.findAll()) {
            if (s.isActive()) {
                result.add(s);
            }
        }
        return result;
    }

    public Student findStudentById(int id) {
        Student s = repository.findById(id);
        if (s == null) {
            throw new EntityNotFoundException("Student", id);
        }
        return s;
    }

    public Student updateStudent(int id, String firstName, String lastName, String email, String batch) {
        Student s = findStudentById(id);
        if (firstName != null) s.setFirstName(firstName);
        if (lastName != null) s.setLastName(lastName);
        if (email != null) s.setEmail(email);
        if (batch != null) s.setBatch(batch);
        return s;
    }

    public void deactivateStudent(int id) {
        Student s = findStudentById(id);
        s.setActive(false);
    }

    public void activateStudent(int id) {
        Student s = findStudentById(id);
        s.setActive(true);
    }

    public boolean removeStudent(int id) {
        findStudentById(id); // ensures it exists, throws otherwise
        return repository.delete(id);
    }

    public int count() {
        return repository.count();
    }
}
