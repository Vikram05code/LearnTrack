#  LearnTrack – Student & Course Management System (Core Java)

##  Project Overview

LearnTrack is a console-based application built using Core Java to manage Students, Courses, and Enrollments.

This project focuses on mastering Java fundamentals such as OOP, collections, exception handling, and clean code structure without relying on advanced frameworks.

---

##  Objectives

* Practice Core Java concepts
* Understand OOP (Encapsulation, Inheritance, Polymorphism)
* Implement a layered architecture (Entity → Repository → Service → UI)
* Build a menu-driven console application
* Handle exceptions gracefully

---

##  Tech Stack

* Java (JDK 8+)
* Core Java Concepts
* ArrayList (Collections Framework)
* Exception Handling

---

##  Features

###  Student Management

* Add new student
* View all students
* Search student by ID
* Deactivate student

###  Course Management

* Add new course
* View all courses
* Activate/Deactivate course

###  Enrollment Management

* Enroll student into course
* View enrollments by student
* Update enrollment status (ACTIVE / COMPLETED / CANCELLED)

---

## Project Structure

```
learntrack/
├── README.md
├── docs/
│   ├── Setup_Instructions.md
│   ├── JVM_Basics.md
│   └── Design_Notes.md
└── src/
    └── com/
        └── airtribe/
            └── learntrack/
                ├── Main.java                       // Menu & application entry point
                │
                ├── entity/
                │   ├── Person.java
                │   ├── Student.java
                │   ├── Trainer.java
                │   ├── Course.java
                │   └── Enrollment.java
                │
                ├── repository/                     // Data storage layer (in-memory)
                │   ├── StudentRepository.java
                │   ├── CourseRepository.java
                │   └── EnrollmentRepository.java
                │
                ├── service/                        // Business logic layer
                │   ├── StudentService.java
                │   ├── CourseService.java
                │   └── EnrollmentService.java
                │
                ├── exception/
                │   ├── EntityNotFoundException.java
                │   └── InvalidInputException.java
                │
                ├── util/
                │   ├── IdGenerator.java
                │   └── InputValidator.java
                │
                ├── constants/                      // Application-wide constants
                │   ├── MenuOptions.java
                │   └── AppConstants.java
                │
                └── enums/                          // Fixed values / states
                    ├── EnrollmentStatus.java
                    └── CourseStatus.java
```

Layered design:

- **entity** — plain data classes with private fields, getters/setters, constructors
- **repository** — pure in-memory storage on top of `ArrayList` (no business rules)
- **service** — business logic operating on top of repositories
- **Main.java** — menu loop, reads input, calls services
- **exception** — domain-specific custom exceptions
- **util** — small helpers (`IdGenerator`, `InputValidator`)
- **constants** — menu option codes and app-wide constants
- **enums** — fixed states for enrollment and course lifecycle

---

---

##  Core Concepts Used

###  OOP Concepts

* Encapsulation (private fields + getters/setters)
* Inheritance (Person → Student, Trainer)
* Polymorphism (method overriding)

###  Java Basics

* Variables & data types
* Control flow (if-else, switch, loops)
* Static vs Instance members

###  Collections

* ArrayList for dynamic data storage

###  Exception Handling

* try-catch blocks
* Custom exceptions (EntityNotFoundException, InvalidInputException)

---

## ▶ How to Compile & Run

### Compile

```bash
javac com/airtribe/learntrack/Main.java
```

### Run

```bash
java com.airtribe.learntrack.Main
```

---

## Class Diagram

```
                       +-------------------+
                       |      Person       |
                       +-------------------+
                       | - id : int        |
                       | - firstName       |
                       | - lastName        |
                       | - email           |
                       +-------------------+
                       | + getDisplayName()|
                       +-------------------+
                          ^             ^
                          |             |
              extends     |             | extends
                          |             |
             +-------------------+   +----------------------+
             |     Student       |   |      Trainer         |
             +-------------------+   +----------------------+
             | - batch           |   | - specialization     |
             | - active          |   +----------------------+
             +-------------------+   | + getDisplayName()  |
             | + getDisplayName()|   +----------------------+
             +-------------------+

   +-------------------+        +-----------------------+       
   |      Course       |        |      Enrollment       |
   +-------------------+        +-----------------------+
   | - id              |        | - id                  |
   | - courseName      |        | - studentId  ---------|---> Student.id
   | - description     |        | - courseId   ---------|---> Course.id
   | - durationInWeeks |        | - enrollmentDate      |
   | - status (enum)   |        | - status (enum)       |
   +-------------------+        +-----------------------+
            |                              |
   uses CourseStatus              uses EnrollmentStatus


   Repositories (ArrayList storage)
   +------------------------+ +-----------------------+ +----------------------------+
   |  StudentRepository     | |  CourseRepository     | |  EnrollmentRepository      |
   +------------------------+ +-----------------------+ +----------------------------+

   Services (Business Logic)
   +------------------------+ +-----------------------+ +----------------------------+
   |  StudentService        | |  CourseService        | |  EnrollmentService         |
   +------------------------+ +-----------------------+ +----------------------------+

                    Main (UI - Menu Driven Application)

   Utilities & Support
   +---------------------+      +---------------------------+      +------------------+
   |   IdGenerator       |      |  EntityNotFoundException  |      |  AppConstants    |
   |   InputValidator    |      |  InvalidInputException    |      |  MenuOptions     |
   +---------------------+      +---------------------------+      +------------------+
```

Relationships:

- `Student` **extends** `Person` (inheritance + `getDisplayName()` overriding)
- `Enrollment` references a `Student` and a `Course` by their `id` fields
- Each `Service` **uses** its corresponding `Repository`
- `EnrollmentService` **uses** `StudentService` and `CourseService` to validate IDs
- `Main` **uses** all three services
- `Course` uses `CourseStatus` enum; `Enrollment` uses `EnrollmentStatus` enum

---

---

## 🧩 Design Decisions

### Why ArrayList instead of Array?

ArrayList is dynamic in size and provides built-in methods for adding, removing, and searching elements, making it more flexible than arrays.

### Where Static is Used

* IdGenerator class uses static variables to maintain unique IDs across the application.

### Use of Inheritance

* Person is a base class.
* Student extends Person.
* This avoids code duplication and improves reusability.

### Use of Trainer Class

A Trainer class is introduced to further demonstrate inheritance and polymorphism.

* Trainer extends Person
* Overrides getDisplayName() to include specialization
* Shows how multiple subclasses can extend a common base class

This helps reinforce real-world OOP hierarchy design.

---

## ⚠️ Assumptions

* Data is stored in-memory (no database)
* Application is single-user console-based
* No concurrency handling

---

## 📌 Future Enhancements

* Add database integration (MySQL)
* Build REST APIs using Spring Boot
* Add file persistence
* Implement user authentication

---

## 👨‍💻 Author

Vikram Kumar
