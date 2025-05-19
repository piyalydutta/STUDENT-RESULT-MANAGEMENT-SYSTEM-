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
        s2.addSubject("Science", 35);
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
        System.out.println("1. Admin Login");
        System.out.println("2. Student Login");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();

        if (choice == 1) {
            system.adminPanel(sc);
        } else if (choice == 2) {
            System.out.print("Enter your Student ID to login: ");
            int id = sc.nextInt();
            system.login(id, sc);
        } else {
            System.out.println("Invalid Choice! Exiting...");
        }

        sc.close();
    }
}

// ================== ResultSystem ==================
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

            int choice;
            do {
                System.out.println("\nMenu:");
                System.out.println("1. Show Marks and Grades");
                System.out.println("2. Check Pass/Fail");
                System.out.println("3. Subjects Yet to Pass");
                System.out.println("4. Generate Performance Report");
                System.out.println("5. Apply for Recheck");
                System.out.println("6. View Rank");
                System.out.println("0. Logout");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

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
            } while (choice != 0);
        } else {
            System.out.println("Student ID not found!");
        }
    }

    void adminPanel(Scanner sc) {
        int choice;
        do {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student Marks");
            System.out.println("3. Delete Student");
            System.out.println("4. View All Students");
            System.out.println("5. Add Subject to Student");
            System.out.println("6. Delete Subject from Student");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();
                    addStudent(new Student(id, name));
                    System.out.println("Student added successfully.");
                }
                case 2 -> {
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    if (students.containsKey(id)) {
                        Student student = students.get(id);
                        student.showMarksAndGrades();
                        System.out.print("Enter Subject Name to Edit Marks: ");
                        String subject = sc.nextLine();
                        System.out.print("Enter New Marks: ");
                        int marks = sc.nextInt();
                        student.editSubjectMarks(subject, marks);
                    } else {
                        System.out.println("Student not found.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Student ID to Delete: ");
                    int id = sc.nextInt();
                    students.remove(id);
                    System.out.println("Student deleted successfully.");
                }
                case 4 -> {
                    System.out.println("\n--- All Students ---");
                    students.values().forEach(Student::generateReport);
                }
                case 5 -> {
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Subject Name: ");
                    String subject = sc.nextLine();
                    System.out.print("Enter Marks: ");
                    int marks = sc.nextInt();
                    students.get(id).addSubject(subject, marks);
                    System.out.println("Subject added successfully.");
                }
                case 6 -> {
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Subject Name to Delete: ");
                    String subject = sc.nextLine();
                    students.get(id).deleteSubject(subject);
                    System.out.println("Subject deleted successfully.");
                }
            }
        } while (choice != 0);
    }
}

// ================== Student ==================
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

    void editSubjectMarks(String subjectName, int marks) {
        for (Subject sub : subjects) {
            if (sub.name.equalsIgnoreCase(subjectName)) {
                sub.marks = marks;
                System.out.println("Marks updated.");
                return;
            }
        }
        System.out.println("Subject not found.");
    }

    void deleteSubject(String subjectName) {
        boolean removed = subjects.removeIf(sub -> sub.name.equalsIgnoreCase(subjectName));
        if (removed)
            System.out.println("Subject removed.");
        else
            System.out.println("Subject not found.");
    }

    void showMarksAndGrades() {
        System.out.println("\n--- Marks and Grades ---");
        for (Subject sub : subjects) {
            System.out.println(sub.name + ": " + sub.marks + " (" + sub.getGrade() + ")");
        }
    }

    boolean isPassed() {
        for (Subject sub : subjects) {
            if (sub.marks < 40) return false;
        }
        return true;
    }

    int subjectsYetToPass() {
        int count = 0;
        for (Subject sub : subjects) {
            if (sub.marks < 40) count++;
        }
        return count;
    }

    void generateReport() {
        System.out.println("\nStudent ID: " + id);
        System.out.println("Name: " + name);
        showMarksAndGrades();
        System.out.println("Total Marks: " + getTotalMarks());
        System.out.println("Status: " + (isPassed() ? "Passed" : "Failed"));
        if (appliedForRecheck) {
            System.out.println("Applied for Recheck: YES");
        }
        System.out.println("-------------------------");
    }

    void applyForRecheck() {
        if (!appliedForRecheck) {
            appliedForRecheck = true;
            System.out.println("Recheck applied successfully.");
        } else {
            System.out.println("Recheck already applied.");
        }
    }

    int getTotalMarks() {
        return subjects.stream().mapToInt(s -> s.marks).sum();
    }
}

// ================== Subject ==================
class Subject {
    String name;
    int marks;

    Subject(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    String getGrade() {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B+";
        else if (marks >= 60) return "B";
        else if (marks >= 50) return "C";
        else if (marks >= 40) return "D";
        else return "F";
    }
}
