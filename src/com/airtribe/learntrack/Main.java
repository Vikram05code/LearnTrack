package com.airtribe.learntrack;

import com.airtribe.learntrack.constants.AppConstants;
import com.airtribe.learntrack.constants.MenuOptions;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.repository.CourseRepository;
import com.airtribe.learntrack.repository.EnrollmentRepository;
import com.airtribe.learntrack.repository.StudentRepository;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.StudentService;
import com.airtribe.learntrack.util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class Main {

    private final Scanner scanner = new Scanner(System.in);

    private final StudentRepository studentRepository = new StudentRepository();
    private final CourseRepository courseRepository = new CourseRepository();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();

    private final StudentService studentService = new StudentService(studentRepository);
    private final CourseService courseService = new CourseService(courseRepository);
    private final EnrollmentService enrollmentService =
            new EnrollmentService(enrollmentRepository, studentService, courseService);

    public static void main(String[] args) {
        Main app = new Main();
        app.seedSampleData();
        app.run();
    }

    private void run() {
        System.out.println("============================================");
        System.out.println("   Welcome to " + AppConstants.APP_NAME);
        System.out.println("   " + AppConstants.APP_TAGLINE);
        System.out.println("============================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case MenuOptions.MAIN_STUDENT:
                        studentMenu();
                        break;
                    case MenuOptions.MAIN_COURSE:
                        courseMenu();
                        break;
                    case MenuOptions.MAIN_ENROLLMENT:
                        enrollmentMenu();
                        break;
                    case MenuOptions.MAIN_EXIT:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please pick 0-3.");
                }
            } catch (InvalidInputException | EntityNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("---------- MAIN MENU ----------");
        System.out.println(MenuOptions.MAIN_STUDENT + ". Student Management");
        System.out.println(MenuOptions.MAIN_COURSE + ". Course Management");
        System.out.println(MenuOptions.MAIN_ENROLLMENT + ". Enrollment Management");
        System.out.println(MenuOptions.MAIN_EXIT + ". Exit");
        System.out.print("Choose an option: ");
    }

    // ===================== STUDENT MENU =====================

    private void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("---- Student Management ----");
            System.out.println(MenuOptions.STUDENT_ADD + ". Add new student");
            System.out.println(MenuOptions.STUDENT_VIEW_ALL + ". View all students");
            System.out.println(MenuOptions.STUDENT_SEARCH + ". Search student by ID");
            System.out.println(MenuOptions.STUDENT_UPDATE + ". Update student");
            System.out.println(MenuOptions.STUDENT_DEACTIVATE + ". Deactivate student");
            System.out.println(MenuOptions.STUDENT_ACTIVATE + ". Activate student");
            System.out.println(MenuOptions.STUDENT_BACK + ". Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case MenuOptions.STUDENT_ADD: addStudentFlow(); break;
                    case MenuOptions.STUDENT_VIEW_ALL: viewAllStudents(); break;
                    case MenuOptions.STUDENT_SEARCH: searchStudentFlow(); break;
                    case MenuOptions.STUDENT_UPDATE: updateStudentFlow(); break;
                    case MenuOptions.STUDENT_DEACTIVATE: deactivateStudentFlow(); break;
                    case MenuOptions.STUDENT_ACTIVATE: activateStudentFlow(); break;
                    case MenuOptions.STUDENT_BACK: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (InvalidInputException | EntityNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addStudentFlow() {
        System.out.print("First name: ");
        String firstName = InputValidator.requireNonEmpty(scanner.nextLine(), "First name");
        System.out.print("Last name: ");
        String lastName = InputValidator.requireNonEmpty(scanner.nextLine(), "Last name");
        System.out.print("Email (optional, press Enter to skip): ");
        String emailRaw = scanner.nextLine();
        String email = InputValidator.validateEmail(emailRaw);
        System.out.print("Batch (e.g. BEL-C20): ");
        String batchInput = scanner.nextLine();
        String batch = (batchInput == null || batchInput.trim().isEmpty())
                ? AppConstants.DEFAULT_BATCH
                : batchInput.trim();

        Student created = (email == null)
                ? studentService.addStudent(firstName, lastName, batch)
                : studentService.addStudent(firstName, lastName, email, batch);
        System.out.println("Created: " + created);
    }

    private void viewAllStudents() {
        List<Student> all = studentService.listStudents();
        if (all.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("Total students: " + all.size());
        for (Student s : all) {
            System.out.println("  - " + s);
        }
    }

    private void searchStudentFlow() {
        System.out.print("Enter student ID: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Student ID");
        Student s = studentService.findStudentById(id);
        System.out.println("Found: " + s);
        System.out.println("Display name: " + s.getDisplayName());
    }

    private void updateStudentFlow() {
        System.out.print("Enter student ID: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Student ID");

        System.out.print("New first name (Enter to skip): ");
        String firstName = blankToNull(scanner.nextLine());
        System.out.print("New last name (Enter to skip): ");
        String lastName = blankToNull(scanner.nextLine());
        System.out.print("New email (Enter to skip): ");
        String emailRaw = blankToNull(scanner.nextLine());
        String email = (emailRaw == null) ? null : InputValidator.validateEmail(emailRaw);
        System.out.print("New batch (Enter to skip): ");
        String batch = blankToNull(scanner.nextLine());

        Student updated = studentService.updateStudent(id, firstName, lastName, email, batch);
        System.out.println("Updated: " + updated);
    }

    private void deactivateStudentFlow() {
        System.out.print("Enter student ID to deactivate: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Student ID");
        studentService.deactivateStudent(id);
        System.out.println("Student " + id + " has been deactivated.");
    }

    private void activateStudentFlow() {
        System.out.print("Enter student ID to activate: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Student ID");
        studentService.activateStudent(id);
        System.out.println("Student " + id + " has been activated.");
    }

    // ===================== COURSE MENU =====================

    private void courseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("---- Course Management ----");
            System.out.println(MenuOptions.COURSE_ADD + ". Add new course");
            System.out.println(MenuOptions.COURSE_VIEW_ALL + ". View all courses");
            System.out.println(MenuOptions.COURSE_SEARCH + ". Search course by ID");
            System.out.println(MenuOptions.COURSE_UPDATE + ". Update course");
            System.out.println(MenuOptions.COURSE_DEACTIVATE + ". Deactivate course");
            System.out.println(MenuOptions.COURSE_ACTIVATE + ". Activate course");
            System.out.println(MenuOptions.COURSE_BACK + ". Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case MenuOptions.COURSE_ADD: addCourseFlow(); break;
                    case MenuOptions.COURSE_VIEW_ALL: viewAllCourses(); break;
                    case MenuOptions.COURSE_SEARCH: searchCourseFlow(); break;
                    case MenuOptions.COURSE_UPDATE: updateCourseFlow(); break;
                    case MenuOptions.COURSE_DEACTIVATE: deactivateCourseFlow(); break;
                    case MenuOptions.COURSE_ACTIVATE: activateCourseFlow(); break;
                    case MenuOptions.COURSE_BACK: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (InvalidInputException | EntityNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addCourseFlow() {
        System.out.print("Course name: ");
        String name = InputValidator.requireNonEmpty(scanner.nextLine(), "Course name");
        System.out.print("Description: ");
        String description = InputValidator.requireNonEmpty(scanner.nextLine(), "Description");
        System.out.print("Duration in weeks: ");
        int weeks = InputValidator.parseIntOrThrow(scanner.nextLine(), "Duration");
        if (weeks <= 0) {
            throw new InvalidInputException("Duration must be a positive number of weeks.");
        }
        Course created = courseService.addCourse(name, description, weeks);
        System.out.println("Created: " + created);
    }

    private void viewAllCourses() {
        List<Course> all = courseService.listCourses();
        if (all.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println("Total courses: " + all.size());
        for (Course c : all) {
            System.out.println("  - " + c);
        }
    }

    private void searchCourseFlow() {
        System.out.print("Enter course ID: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Course ID");
        Course c = courseService.findCourseById(id);
        System.out.println("Found: " + c);
    }

    private void updateCourseFlow() {
        System.out.print("Enter course ID: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Course ID");

        System.out.print("New name (Enter to skip): ");
        String name = blankToNull(scanner.nextLine());
        System.out.print("New description (Enter to skip): ");
        String description = blankToNull(scanner.nextLine());
        System.out.print("New duration in weeks (Enter to skip): ");
        String weeksRaw = blankToNull(scanner.nextLine());
        Integer weeks = (weeksRaw == null) ? null : InputValidator.parseIntOrThrow(weeksRaw, "Duration");

        Course updated = courseService.updateCourse(id, name, description, weeks);
        System.out.println("Updated: " + updated);
    }

    private void deactivateCourseFlow() {
        System.out.print("Enter course ID to deactivate: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Course ID");
        courseService.deactivateCourse(id);
        System.out.println("Course " + id + " has been deactivated.");
    }

    private void activateCourseFlow() {
        System.out.print("Enter course ID to activate: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Course ID");
        courseService.activateCourse(id);
        System.out.println("Course " + id + " has been activated.");
    }

    // ===================== ENROLLMENT MENU =====================

    private void enrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("---- Enrollment Management ----");
            System.out.println(MenuOptions.ENROLL_ADD + ". Enroll a student in a course");
            System.out.println(MenuOptions.ENROLL_LIST_BY_STUDENT + ". View enrollments for a student");
            System.out.println(MenuOptions.ENROLL_LIST_BY_COURSE + ". View enrollments for a course");
            System.out.println(MenuOptions.ENROLL_LIST_ALL + ". View all enrollments");
            System.out.println(MenuOptions.ENROLL_MARK_COMPLETED + ". Mark enrollment as COMPLETED");
            System.out.println(MenuOptions.ENROLL_MARK_CANCELLED + ". Mark enrollment as CANCELLED");
            System.out.println(MenuOptions.ENROLL_BACK + ". Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case MenuOptions.ENROLL_ADD: enrollFlow(); break;
                    case MenuOptions.ENROLL_LIST_BY_STUDENT: listEnrollmentsForStudentFlow(); break;
                    case MenuOptions.ENROLL_LIST_BY_COURSE: listEnrollmentsForCourseFlow(); break;
                    case MenuOptions.ENROLL_LIST_ALL: listAllEnrollments(); break;
                    case MenuOptions.ENROLL_MARK_COMPLETED: markEnrollmentFlow(true); break;
                    case MenuOptions.ENROLL_MARK_CANCELLED: markEnrollmentFlow(false); break;
                    case MenuOptions.ENROLL_BACK: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (InvalidInputException | EntityNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void enrollFlow() {
        System.out.print("Student ID: ");
        int studentId = InputValidator.parseIntOrThrow(scanner.nextLine(), "Student ID");
        System.out.print("Course ID: ");
        int courseId = InputValidator.parseIntOrThrow(scanner.nextLine(), "Course ID");
        Enrollment e = enrollmentService.enrollStudent(studentId, courseId);
        System.out.println("Enrolled: " + e);
    }

    private void listEnrollmentsForStudentFlow() {
        System.out.print("Student ID: ");
        int studentId = InputValidator.parseIntOrThrow(scanner.nextLine(), "Student ID");
        List<Enrollment> list = enrollmentService.listEnrollmentsForStudent(studentId);
        printEnrollmentList(list, "for student " + studentId);
    }

    private void listEnrollmentsForCourseFlow() {
        System.out.print("Course ID: ");
        int courseId = InputValidator.parseIntOrThrow(scanner.nextLine(), "Course ID");
        List<Enrollment> list = enrollmentService.listEnrollmentsForCourse(courseId);
        printEnrollmentList(list, "for course " + courseId);
    }

    private void listAllEnrollments() {
        printEnrollmentList(enrollmentService.listEnrollments(), "(all)");
    }

    private void printEnrollmentList(List<Enrollment> list, String label) {
        if (list.isEmpty()) {
            System.out.println("No enrollments " + label + ".");
            return;
        }
        System.out.println("Enrollments " + label + ": " + list.size());
        for (Enrollment e : list) {
            System.out.println("  - " + e);
        }
    }

    private void markEnrollmentFlow(boolean completed) {
        System.out.print("Enrollment ID: ");
        int id = InputValidator.parseIntOrThrow(scanner.nextLine(), "Enrollment ID");
        Enrollment e = completed
                ? enrollmentService.markCompleted(id)
                : enrollmentService.markCancelled(id);
        System.out.println("Updated: " + e);
    }

    // ===================== HELPERS =====================

    private String blankToNull(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void seedSampleData() {
        Student s1 = studentService.addStudent("Vikram", "Sharma", "vikram@gmail.com", "BEL-C20");
        Student s2 = studentService.addStudent("Rohit", "Sharma", "BEL-C20");
        Course c1 = courseService.addCourse("Core Java", "Java basics and OOP", 6);
        Course c2 = courseService.addCourse("Spring Boot", "Build REST APIs with Spring Boot", 8);
        enrollmentService.enrollStudent(s1.getId(), c1.getId());
        enrollmentService.enrollStudent(s2.getId(), c1.getId());
        enrollmentService.enrollStudent(s1.getId(), c2.getId());
    }
}
