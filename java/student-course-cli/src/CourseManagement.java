import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManagement {
   /** Master list of courses in the system */
   private static List<Course> courses = new ArrayList<>();
   /** Registry of students by ID to ensure single instance per student */
   private static Map<String, Student> studentRegistry = new HashMap<>();
   /** Records each student's course-to-grade mapping */
   private static Map<Student, Map<Course, Double>> studentGrades = new HashMap<>();


   // Prevent instantiation
   private CourseManagement() {}


   /**
    * Retrieves a registered Student by ID.
    * @param id student ID
    * @return Student instance or null if not found
    */
   public static Student getStudentById(String id) {
       return studentRegistry.get(id);
   }


   /**
    * Registers a new student and initializes their grade record.
    * @param id   student ID
    * @param name student name
    * @return the newly created Student
    */
   public static Student registerStudent(String id, String name) {
       Student s = new Student(id, name);
       studentRegistry.put(id, s);
       studentGrades.put(s, new HashMap<>());
       return s;
   }


   /**
    * Adds a new Course to the system.
    * @param code     course code
    * @param name     course name
    * @param capacity maximum students
    */
   public static void addCourse(String code, String name, int capacity) {
       courses.add(new Course(code, name, capacity));
       System.out.println("[INFO] Course added: " + code + " - " + name);
   }


   /** @return list of all available courses */
   public static List<Course> getCourses() {
       return List.copyOf(courses);
   }


   /**
    * Enrolls a student in a specified course.
    * @param s student to enroll
    * @param c course to enroll in
    */
   public static void enrollStudent(Student s, Course c) {
       s.enrollInCourse(c);
       studentGrades.get(s).put(c, null);
       System.out.println("[INFO] " + s.getName() + " enrolled in " + c.getCode());
   }


   /**
    * Assigns a grade to a student for a course.
    * @param s     student
    * @param c     course
    * @param grade numeric grade
    */
   public static void assignGrade(Student s, Course c, double grade) {
       s.assignGrade(c, grade);
       studentGrades.get(s).put(c, grade);
       System.out.printf("[INFO] Assigned grade %.2f to %s in %s%n", grade, s.getName(), c.getCode());
   }


   /**
    * Calculates the average of all assigned grades for a student.
    * @param s student whose grades to average
    * @return average grade or 0.0 if none assigned
    */
   public static double calculateOverallGrade(Student s) {
       Map<Course, Double> grades = studentGrades.get(s);
       if (grades == null || grades.isEmpty()) return 0.0;
       double sum = 0;
       int count = 0;
       for (Double g : grades.values()) {
           if (g != null) {
               sum += g;
               count++;
           }
       }
       return count == 0 ? 0.0 : sum / count;
   }
}
