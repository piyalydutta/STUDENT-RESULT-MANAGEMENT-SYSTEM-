import java.util.*;

public class StudentResultManagementSystem {
    public static void main(String[] args) {
        ResultSystem system = new ResultSystem();

        // Sample data
        Student s1 = new Student(101, "Alice");
        s1.addSubject("Math", 85);
        s1.addSubject("Science", 78);
        s1.addSubject("English", 92);

        Student s2 = new Student(102, "Bob");
        s2.addSubject("Math", 55);
        s2.addSubject("Science", 35);  // fail
        s2.addSubject("English", 60);

        Student s3 = new Student(103, "Charlie");
        s3.addSubject("Math", 95);
        s3.addSubject("Science", 88);
        s3.addSubject("English", 90);

        system.addStudent(s1);
        system.addStudent(s2);
        system.addStudent(s3);

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Student Result Management System");
        System.out.print("Enter your Student ID to login: ");

        if (sc.hasNextInt()) {
            int id = sc.nextInt();
            system.login(id, sc);
        } else {
            System.out.println("Invalid Student ID! Exiting...");
        }

        // sc.close(); // Optional, depending on use
    }
}

class ResultSystem {
    Map<Integer, Student> students = new HashMap<>();

    void addStudent(Student student) {
        students.put(student.id, student);
    }

    void displayRankingsWithHighlight(int currentStudentId) {
        List<Student> rankedList = new ArrayList<>(students.values());
        rankedList.sort((s1, s2) -> s2.getTotalMarks() - s1.getTotalMarks());

        System.out.println("\n--- Student Rankings ---");
        int rank = 1;
        for (Student s : rankedList) {
            String highlight = (s.id == currentStudentId) ? " <-- You" : "";
            System.out.println("Rank " + rank + ": " + s.name + " (Total: " + s.getTotalMarks() + ")" + highlight);
            rank++;
        }
        System.out.println("-------------------------");
    }

    void login(int studentId, Scanner sc) {
        if (students.containsKey(studentId)) {
            Student s = students.get(studentId);
            System.out.println("\nWelcome, " + s.name + "!");

            int choice = -1;
            while (choice != 0) {
                System.out.println("\nMenu:");
                System.out.println("1. Show Marks and Grades");
                System.out.println("2. Check Pass/Fail");
                System.out.println("3. Subjects Yet to Pass");
                System.out.println("4. Generate Performance Report");
                System.out.println("5. Apply for Recheck");
                System.out.println("6. View Rank");
                System.out.println("0. Logout");
                System.out.print("Enter your choice: ");

                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                } else {
                    System.out.println("Invalid input! Please enter a number.");
                    sc.next(); // Consume invalid input
                    continue;
                }

                switch (choice) {
                    case 1 -> s.showMarksAndGrades();
                    case 2 -> System.out.println("Status: " + (s.isPassed() ? "Passed" : "Failed"));
                    case 3 -> {
                        int pending = s.subjectsYetToPass();
                        System.out.println("Subjects Yet to Pass: " + pending + (pending == 1 ? " subject" : " subjects"));
                    }
                    case 4 -> s.generateReport();
                    case 5 -> s.applyForRecheck();
                    case 6 -> displayRankingsWithHighlight(s.id);
                    case 0 -> System.out.println("Logged out.");
                    default -> System.out.println("Invalid choice! Try again.");
                }
            }
        } else {
            System.out.println("Student ID not found!");
        }
    }
}

class Student {
    int id;
    String name;
    List<Subject> subjects = new ArrayList<>();
    boolean appliedForRecheck = false;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    void addSubject(String subjectName, int marks) {
        subjects.add(new Subject(subjectName, marks));
    }

    void showMarksAndGrades() {
        System.out.println("\nSubject-wise Marks and Grades:");
        for (Subject sub : subjects) {
            System.out.println(sub.name + ": " + sub.marks + " Marks, Grade: " + sub.getGrade());
        }
    }

    boolean isPassed() {
        for (Subject sub : subjects) {
            if (!sub.isPassed()) return false;
        }
        return true;
    }

    int subjectsYetToPass() {
        int count = 0;
        for (Subject sub : subjects) {
            if (!sub.isPassed()) count++;
        }
        return count;
    }

    int getTotalMarks() {
        int total = 0;
        for (Subject sub : subjects) {
            total += sub.marks;
        }
        return total;
    }

    void generateReport() {
        System.out.println("\n--- Performance Report for " + name + " ---");
        showMarksAndGrades();
        System.out.println("Total Marks: " + getTotalMarks());
        System.out.println("Status: " + (isPassed() ? "Passed" : "Failed"));
        int pending = subjectsYetToPass();
        System.out.println("Subjects Yet to Pass: " + pending + (pending == 1 ? " subject" : " subjects"));
        System.out.println("Recheck Applied: " + (appliedForRecheck ? "Yes" : "No"));
        System.out.println("-------------------------------------------");
    }

    void applyForRecheck() {
        if (appliedForRecheck) {
            System.out.println("You have already applied for recheck.");
        } else {
            appliedForRecheck = true;
            System.out.println("Recheck applied successfully!");
        }
    }
}

class Subject {
    String name;
    int marks;

    Subject(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    char getGrade() {
        if (marks >= 90) return 'A';
        else if (marks >= 80) return 'B';
        else if (marks >= 70) return 'C';
        else if (marks >= 60) return 'D';
        else if (marks >= 50) return 'E';
        else return 'F';
    }

    boolean isPassed() {
        return marks>=40;
    }
}