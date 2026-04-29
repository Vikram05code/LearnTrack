package com.airtribe.learntrack.repository;

import com.airtribe.learntrack.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory storage for Course records.
 */
public class CourseRepository {

    private final List<Course> courses = new ArrayList<>();

    public Course save(Course course) {
        courses.add(course);
        return course;
    }

    public List<Course> findAll() {
        return new ArrayList<>(courses);
    }

    public Course findById(int id) {
        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public boolean delete(int id) {
        Course found = findById(id);
        if (found == null) {
            return false;
        }
        return courses.remove(found);
    }

    public int count() {
        return courses.size();
    }
}
