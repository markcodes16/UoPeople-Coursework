public class Student {
   private final String id;        // Unique identifier for the student (format: starts with letter, alphanumeric)
   private String firstName;      // Student's first name (cannot be empty)
   private String lastName;       // Student's last name (cannot be empty)


   /**
    * Constructs a new Student with the specified details.
    * @param id the unique student identifier (must start with letter and be alphanumeric)
    * @param firstName the student's first name (cannot be empty)
    * @param lastName the student's last name (cannot be empty)
    * @throws IllegalArgumentException if any parameter is null or empty
    */
   public Student(String id, String firstName, String lastName) {
       this.id = id;
       this.firstName = firstName;
       this.lastName = lastName;
   }


   /**
    * Gets the student's unique ID.
    * @return the student ID string
    */
   public String getId() {
       return id;
   }


   /**
    * Gets the student's first name.
    * @return the first name string
    */
   public String getFirstName() {
       return firstName;
   }


   /**
    * Gets the student's last name.
    * @return the last name string
    */
   public String getLastName() {
       return lastName;
   }


   /**
    * Sets the student's first name.
    * @param firstName the new first name (cannot be empty)
    * @throws IllegalArgumentException if firstName is null or empty
    */
   public void setFirstName(String firstName) {
       this.firstName = firstName;
   }


   /**
    * Sets the student's last name.
    * @param lastName the new last name (cannot be empty)
    * @throws IllegalArgumentException if lastName is null or empty
    */
   public void setLastName(String lastName) {
       this.lastName = lastName;
   }


   /**
    * Gets the student's full name in "FirstName LastName" format.
    * @return concatenated full name string
    */
   public String getFullName() {
       return firstName + " " + lastName;
   }


   /**
    * Returns a string representation of the student including full name and ID.
    * @return string in format "FullName (ID)"
    */
   @Override
   public String toString() {
       return getFullName() + " (" + id + ")";
   }
}
