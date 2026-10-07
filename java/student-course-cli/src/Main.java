import java.util.List;
import java.util.Scanner;

public class Main {
   private static Scanner scanner = new Scanner(System.in);


   public static void main(String[] args) {
       System.out.println("=== University Course Enrollment & Grade Management ===");
       while (true) {
           printMenu();
           int choice = Integer.parseInt(scanner.nextLine());
           try {
               switch (choice) {
                   case 1: handleAddCourse(); break;
                   case 2: handleEnrollStudent(); break;
                   case 3: handleAssignGrade(); break;
                   case 4: handleCalcOverall(); break;
                   case 5:
                       System.out.println("[INFO] Total enrolled (all courses): " + Course.getTotalEnrolled());
                       break;
                   case 0:
                       System.out.println("Goodbye.");
                       System.exit(0);
                   default:
                       System.out.println("[WARN] Invalid option.");
               }
           } catch (Exception e) {
               System.err.println("[ERROR] " + e.getMessage());
           }
       }
   }


   private static void printMenu() {
       System.out.println("\nMenu:");
       System.out.println("1. Add course");
       System.out.println("2. Enroll student");
       System.out.println("3. Assign grade");
       System.out.println("4. Calculate overall grade");
       System.out.println("5. Show total enrolled across all courses");
       System.out.println("0. Exit");
       System.out.print("Select option: ");
   }


   private static void handleAddCourse() {
       System.out.print("Course code: ");
       String code = scanner.nextLine();
       System.out.print("Name: ");
       String name = scanner.nextLine();
       System.out.print("Max capacity: ");
       int cap = Integer.parseInt(scanner.nextLine());
       CourseManagement.addCourse(code, name, cap);
   }


   private static void handleEnrollStudent() {
       Student s = promptStudent();
       Course c = promptCourse();
       CourseManagement.enrollStudent(s, c);
   }


   private static void handleAssignGrade() {
       Student s = promptStudent();
       Course c = promptCourse();
       System.out.print("Grade (0–100): ");
       double g = Double.parseDouble(scanner.nextLine());
       CourseManagement.assignGrade(s, c, g);
   }


   private static void handleCalcOverall() {
       Student s = promptStudent();
       double avg = CourseManagement.calculateOverallGrade(s);
       System.out.printf("%s's overall grade: %.2f%n", s.getName(), avg);
   }


   /**
    * Prompts for student ID, then retrieves or registers the Student.
    * Ensures the same Student object is used throughout the session.
    * @return existing or newly registered Student
    */
   private static Student promptStudent() {
       System.out.print("Student ID: ");
       String id = scanner.nextLine();
       Student s = CourseManagement.getStudentById(id);
       if (s == null) {
           System.out.print("Student name: ");
           String name = scanner.nextLine();
           s = CourseManagement.registerStudent(id, name);
           System.out.println("[INFO] Registered new student: " + s.getName());
       } else {
           System.out.println("[INFO] Found existing student: " + s.getName());
       }
       return s;
   }


   /**
    * Displays all available courses, prompts selection, and returns chosen Course.
    * @return selected Course
    * @throws IllegalStateException if no courses exist
    */
   private static Course promptCourse() {
       List<Course> list = CourseManagement.getCourses();
       if (list.isEmpty()) {
           throw new IllegalStateException("No courses available.");
       }
       System.out.println("Available courses:");
       for (int i = 0; i < list.size(); i++) {
           Course c = list.get(i);
           System.out.printf("%d. %s (%s) [%d/%d]%n",
                   i+1, c.getName(), c.getCode(), c.getEnrolledCount(), c.getMaxCapacity());
       }
       System.out.print("Select course number: ");
       int idx = Integer.parseInt(scanner.nextLine()) - 1;
       return list.get(idx);
   }
}
