import java.util.LinkedHashMap;
import java.util.Map;

public class Student {
   /** Unique student identifier */
   private String id;
   /** Student's full name */
   private String name;
   /** Map of enrolled courses to assigned grades (null if pending) */
   private Map<Course, Double> enrolledCourses = new LinkedHashMap<>();


   /**
    * Constructs a Student with the given ID and name.
    * @param id    unique student ID
    * @param name  full name of the student
    */
   public Student(String id, String name) {
       this.id = id;
       this.name = name;
   }


   // --- Accessors & Mutators ---


   /** @return student ID */
   public String getId() { return id; }
   /** @param id new student ID */
   public void setId(String id) { this.id = id; }


   /** @return student name */
   public String getName() { return name; }
   /** @param name new student name */
   public void setName(String name) { this.name = name; }


   /**
    * Enrolls this student in the specified course.
    * Delegates capacity check to <code>Course.addStudent()</code>.
    * @param course the Course to enroll in
    * @throws IllegalStateException if the course is full
    */
   public void enrollInCourse(Course course) {
       if (enrolledCourses.containsKey(course)) {
           System.out.println("[INFO] Already enrolled in " + course.getCode());
           return;
       }
       course.addStudent();
       enrolledCourses.put(course, null);
   }


   /**
    * Assigns a numeric grade to this student for a given course.
    * @param course the Course in which to assign a grade
    * @param grade  numeric grade (0–100)
    * @throws IllegalArgumentException if not enrolled in the course
    */
   public void assignGrade(Course course, double grade) {
       if (!enrolledCourses.containsKey(course)) {
           throw new IllegalArgumentException("Not enrolled in course: " + course.getCode());
       }
       enrolledCourses.put(course, grade);
   }


   /**
    * @return unmodifiable view of enrolled courses and grades
    */
   public Map<Course, Double> getEnrolledCourses() {
       return Map.copyOf(enrolledCourses);
   }
}
