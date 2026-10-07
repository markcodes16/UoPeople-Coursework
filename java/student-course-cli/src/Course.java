public class Course {
   /** Course code (e.g., "CS101") */
   private String code;
   /** Human-readable course name */
   private String name;
   /** Maximum allowed students in this course */
   private int maxCapacity;
   /** Current number of enrolled students in this instance */
   private int enrolledCount = 0;


   /**
    * Static counter of total enrollments across all Course instances.
    */
   private static int totalEnrolled = 0;


   /**
    * Constructs a Course with the given parameters.
    * @param code        unique course code
    * @param name        course title
    * @param maxCapacity max number of students
    */
   public Course(String code, String name, int maxCapacity) {
       this.code = code;
       this.name = name;
       this.maxCapacity = maxCapacity;
   }


   // --- Accessors ---
   public String getCode() { return code; }
   public String getName() { return name; }
   public int getMaxCapacity() { return maxCapacity; }
   public int getEnrolledCount() { return enrolledCount; }


   /**
    * Increments enrollment for this course if capacity allows.
    * Also increments the static <code>totalEnrolled</code> counter.
    * @throws IllegalStateException if course is already at maxCapacity
    */
   public void addStudent() {
       if (enrolledCount >= maxCapacity) {
           throw new IllegalStateException("Course " + code + " is full.");
       }
       enrolledCount++;
       totalEnrolled++;
   }


   /**
    * @return total number of student enrollments across ALL courses
    */
   public static int getTotalEnrolled() {
       return totalEnrolled;
   }
}
