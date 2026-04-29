package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.enums.CourseStatus;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.repository.CourseRepository;
import com.airtribe.learntrack.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course addCourse(String courseName, String description, int durationInWeeks) {
        Course course = new Course(IdGenerator.getNextCourseId(), courseName, description, durationInWeeks);
        return repository.save(course);
    }

    public List<Course> listCourses() {
        return repository.findAll();
    }

    public List<Course> listActiveCourses() {
        List<Course> result = new ArrayList<>();
        for (Course c : repository.findAll()) {
            if (c.isActive()) {
                result.add(c);
            }
        }
        return result;
    }

    public Course findCourseById(int id) {
        Course c = repository.findById(id);
        if (c == null) {
            throw new EntityNotFoundException("Course", id);
        }
        return c;
    }

    public Course updateCourse(int id, String courseName, String description, Integer durationInWeeks) {
        Course c = findCourseById(id);
        if (courseName != null) c.setCourseName(courseName);
        if (description != null) c.setDescription(description);
        if (durationInWeeks != null) c.setDurationInWeeks(durationInWeeks);
        return c;
    }

    public void deactivateCourse(int id) {
        Course c = findCourseById(id);
        c.setStatus(CourseStatus.INACTIVE);
    }

    public void activateCourse(int id) {
        Course c = findCourseById(id);
        c.setStatus(CourseStatus.ACTIVE);
    }

    public int count() {
        return repository.count();
    }
}
