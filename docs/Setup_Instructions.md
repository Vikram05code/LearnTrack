#  Setup Instructions

## JDK Version

JDK 8 or above is recommended.

To check installed version:

```bash
java -version
```

---

## Installation Steps

1. Download JDK from Oracle or OpenJDK
2. Install and set environment variables:

    * JAVA_HOME
    * PATH

---

##  Running Hello World

### Step 1: Create File

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

### Step 2: Compile

```bash
javac HelloWorld.java
```

### Step 3: Run

```bash
java HelloWorld
```

---

## Notes

* Ensure Java is added to PATH
* Use any IDE like IntelliJ IDEA or Eclipse for development
