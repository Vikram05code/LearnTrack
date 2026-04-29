package com.airtribe.learntrack.repository;

import com.airtribe.learntrack.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory storage for Student records.
 * The repository is responsible ONLY for storing and retrieving data;
 * business rules live in the service layer.
 */
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public Student save(Student student) {
        students.add(student);
        return student;
    }

    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    public Student findById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public boolean delete(int id) {
        Student found = findById(id);
        if (found == null) {
            return false;
        }
        return students.remove(found);
    }

    public int count() {
        return students.size();
    }
}
