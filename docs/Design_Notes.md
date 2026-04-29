#  Design Notes

##  Why ArrayList Instead of Array?

ArrayList allows dynamic resizing and provides useful methods like add(), remove(), and contains(), making it more flexible than arrays.

---

##  Where Static is Used and Why

The IdGenerator class uses static variables and methods to generate unique IDs for Students, Courses, and Enrollments. This ensures consistency across the application.

---

##  Use of Inheritance

The Person class acts as a base class for Student.

Benefits:

* Code reuse
* Cleaner design
* Avoids duplication of fields like name and email

---

##  Separation of Concerns

### Entity Layer

Contains data models (Student, Course, Enrollment)

### Repository Layer

Handles data storage (ArrayList)

### Service Layer

Contains business logic

### UI Layer (Main.java)

Handles user interaction

---

##  Exception Handling Strategy

* Custom exception for missing entities
* try-catch used for input validation
* Prevents application crash and shows user-friendly messages

---

##  Clean Code Practices

* Meaningful method names (addStudent, findById)
* Small and focused methods
* Clear package structure
