public class Course {
   private final String code;     // Unique course code (e.g., "CS101")
   private final String title;    // Descriptive course title


   /**
    * Constructs a new Course with the specified details.
    * @param code the unique course identifier (cannot be empty)
    * @param title the descriptive course title (cannot be empty)
    * @throws IllegalArgumentException if any parameter is null or empty
    */
   public Course(String code, String title) {
       this.code = code;
       this.title = title;
   }


   /**
    * Gets the course code.
    * @return the course code string
    */
   public String getCode() {
       return code;
   }


   /**
    * Gets the course title.
    * @return the course title string
    */
   public String getTitle() {
       return title;
   }


   /**
    * Returns a string representation of the course including code and title.
    * @return string in format "Code: Title"
    */
   @Override
   public String toString() {
       return code + ": " + title;
   }
}
