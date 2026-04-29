package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.enums.EnrollmentStatus;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.repository.EnrollmentRepository;
import com.airtribe.learntrack.util.IdGenerator;

import java.util.List;

public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentRepository repository,
                             StudentService studentService,
                             CourseService courseService) {
        this.repository = repository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public Enrollment enrollStudent(int studentId, int courseId) {
        Student student = studentService.findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);

        if (!student.isActive()) {
            throw new InvalidInputException("Cannot enroll an inactive student (id=" + studentId + ").");
        }
        if (!course.isActive()) {
            throw new InvalidInputException("Cannot enroll into an inactive course (id=" + courseId + ").");
        }
        if (repository.existsActive(studentId, courseId)) {
            throw new InvalidInputException("Student " + studentId
                    + " is already actively enrolled in course " + courseId + ".");
        }

        Enrollment enrollment = new Enrollment(IdGenerator.getNextEnrollmentId(), studentId, courseId);
        return repository.save(enrollment);
    }

    public List<Enrollment> listEnrollments() {
        return repository.findAll();
    }

    public List<Enrollment> listEnrollmentsForStudent(int studentId) {
        studentService.findStudentById(studentId);
        return repository.findByStudentId(studentId);
    }

    public List<Enrollment> listEnrollmentsForCourse(int courseId) {
        courseService.findCourseById(courseId);
        return repository.findByCourseId(courseId);
    }

    public Enrollment findEnrollmentById(int id) {
        Enrollment e = repository.findById(id);
        if (e == null) {
            throw new EntityNotFoundException("Enrollment", id);
        }
        return e;
    }

    public Enrollment markCompleted(int enrollmentId) {
        Enrollment e = findEnrollmentById(enrollmentId);
        if (e.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new InvalidInputException("Cannot complete a cancelled enrollment.");
        }
        e.setStatus(EnrollmentStatus.COMPLETED);
        return e;
    }

    public Enrollment markCancelled(int enrollmentId) {
        Enrollment e = findEnrollmentById(enrollmentId);
        if (e.getStatus() == EnrollmentStatus.COMPLETED) {
            throw new InvalidInputException("Cannot cancel a completed enrollment.");
        }
        e.setStatus(EnrollmentStatus.CANCELLED);
        return e;
    }
}
