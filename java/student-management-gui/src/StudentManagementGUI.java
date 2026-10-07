import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StudentManagementGUI extends JFrame {
   // Data storage structures
   private final List<Student> students = new ArrayList<>();  // List of all student records
   private final List<Course> courses = new ArrayList<>(Arrays.asList(  // Predefined course list
           new Course("CS101", "Introduction to Programming"),
           new Course("MATH201", "Calculus I"),
           new Course("ENG105", "Academic Writing")
   ));
   private final Map<Student, Map<Course, String>> grades = new HashMap<>();  // Nested map for grades (Student → (Course → Grade))


   // GUI Components - Student Management
   private DefaultTableModel studentTableModel;  // Data model for student table
   private JTable studentTable;                 // Table displaying student list


   // GUI Components - Course Enrollment
   private JComboBox<Course> enrollCourseCombo;  // Dropdown for course selection
   private JList<Student> enrollStudentList;     // List of students available for enrollment
   private DefaultListModel<Student> enrollStudentListModel;  // Data model for available students
   private JList<Student> enrolledStudentList;   // List of currently enrolled students
   private DefaultListModel<Student> enrolledStudentListModel;  // Data model for enrolled students


   // GUI Components - Grade Management
   private JComboBox<Student> gradeStudentCombo;  // Dropdown for student selection
   private JComboBox<Course> gradeCourseCombo;    // Dropdown for course selection
   private JTextField gradeField;                 // Input field for grade entry
   private DefaultTableModel gradeTableModel;     // Data model for grade display table
   private JTable gradeTable;                     // Table displaying assigned grades


   // Additional UI Components
   private DefaultTableModel enrollmentTableModel;  // Data model for enrollment summary
   private JTable enrollmentTable;                 // Table showing enrollment counts
   private JLabel statusLabel;                    // Status bar at bottom of window


   // Layout Management
   private CardLayout cardLayout;  // Layout manager for view switching
   private JPanel mainPanel;       // Main container panel using CardLayout


   /**
    * Constructs the main application window and initializes all components.
    * Sets up the menu bar, main panel with CardLayout, and status bar.
    */
   public StudentManagementGUI() {
       setTitle("Student Management System");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(1100, 750);
       setLocationRelativeTo(null);


       initMenuBar();
       initMainPanel();
       initStatusBar();


       setVisible(true);
   }


   /**
    * Initializes the menu bar with File, Navigation, and Help menus.
    * File menu contains exit functionality.
    * Navigation menu allows switching between views.
    * Help menu contains about information.
    */
   private void initMenuBar() {
       JMenuBar menuBar = new JMenuBar();


       // File Menu
       JMenu fileMenu = new JMenu("File");
       JMenuItem exitItem = new JMenuItem("Exit");
       exitItem.addActionListener(e -> System.exit(0));
       fileMenu.add(exitItem);


       // Navigation Menu
       JMenu navMenu = new JMenu("Navigation");
       JMenuItem studentMenu = new JMenuItem("Student Management");
       studentMenu.addActionListener(e -> {
           cardLayout.show(mainPanel, "students");
           updateStudentTable();
           updateStatus("Student management view");
       });


       JMenuItem enrollMenu = new JMenuItem("Course Enrollment");
       enrollMenu.addActionListener(e -> {
           cardLayout.show(mainPanel, "enrollment");
           updateEnrollmentLists();
           updateStatus("Course enrollment view");
       });


       JMenuItem gradeMenu = new JMenuItem("Grade Management");
       gradeMenu.addActionListener(e -> {
           cardLayout.show(mainPanel, "grades");
           updateGradeComponents();
           updateStatus("Grade management view");
       });


       navMenu.add(studentMenu);
       navMenu.add(enrollMenu);
       navMenu.add(gradeMenu);


       // Help Menu
       JMenu helpMenu = new JMenu("Help");
       JMenuItem aboutItem = new JMenuItem("About");
       aboutItem.addActionListener(e -> showAboutDialog());
       helpMenu.add(aboutItem);


       menuBar.add(fileMenu);
       menuBar.add(navMenu);
       menuBar.add(helpMenu);
       setJMenuBar(menuBar);
   }


   /**
    * Initializes the main panel with CardLayout containing three views:
    * 1. Student Management
    * 2. Course Enrollment
    * 3. Grade Management
    */
   private void initMainPanel() {
       mainPanel = new JPanel();
       cardLayout = new CardLayout();
       mainPanel.setLayout(cardLayout);


       mainPanel.add(createStudentPanel(), "students");
       mainPanel.add(createEnrollmentPanel(), "enrollment");
       mainPanel.add(createGradePanel(), "grades");


       add(mainPanel, BorderLayout.CENTER);
       cardLayout.show(mainPanel, "students");
   }


   /**
    * Initializes the status bar at the bottom of the window.
    * Displays system status messages.
    */
   private void initStatusBar() {
       statusLabel = new JLabel("Ready");
       statusLabel.setBorder(BorderFactory.createEtchedBorder());
       add(statusLabel, BorderLayout.SOUTH);
   }


   /**
    * Updates the status bar message.
    * @param message the status message to display
    */
   private void updateStatus(String message) {
       statusLabel.setText(message);
   }


   /**
    * Creates and configures the Student Management panel.
    * @return the configured JPanel containing student management components
    */
   private JPanel createStudentPanel() {
       JPanel panel = new JPanel(new BorderLayout(10, 10));
       panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


       // Button panel
       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
       JButton addBtn = new JButton("Add Student");
       addBtn.setToolTipText("Add a new student to the system");
       addBtn.addActionListener(e -> showAddStudentDialog());


       JButton updateBtn = new JButton("Update Student");
       updateBtn.setToolTipText("Update selected student's information");
       updateBtn.addActionListener(e -> showUpdateStudentDialog());


       JButton viewBtn = new JButton("View Details");
       viewBtn.setToolTipText("View detailed information about selected student");
       viewBtn.addActionListener(e -> showStudentDetails());


       JButton deleteBtn = new JButton("Delete Student");
       deleteBtn.setToolTipText("Remove selected student from the system");
       deleteBtn.addActionListener(e -> deleteStudent());


       buttonPanel.add(addBtn);
       buttonPanel.add(updateBtn);
       buttonPanel.add(viewBtn);
       buttonPanel.add(deleteBtn);


       // Table setup
       String[] columns = {"ID", "Name", "Enrollments", "GPA"};
       studentTableModel = new DefaultTableModel(columns, 0) {
           @Override
           public boolean isCellEditable(int row, int column) {
               return false;
           }
       };


       studentTable = new JTable(studentTableModel);
       studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       studentTable.getTableHeader().setReorderingAllowed(false);


       // Search panel
       JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       JTextField searchField = new JTextField(20);
       JButton searchBtn = new JButton("Search");
       searchBtn.addActionListener(e -> searchStudents(searchField.getText()));
       searchPanel.add(new JLabel("Search:"));
       searchPanel.add(searchField);
       searchPanel.add(searchBtn);


       // Add components to main panel
       panel.add(buttonPanel, BorderLayout.NORTH);
       panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
       panel.add(searchPanel, BorderLayout.SOUTH);


       updateStudentTable();
       return panel;
   }


   /**
    * Displays a dialog for adding a new student.
    * Validates input and adds the student to the system if valid.
    */
   private void showAddStudentDialog() {
       JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
       JTextField idField = new JTextField();
       JTextField firstNameField = new JTextField();
       JTextField lastNameField = new JTextField();


       inputPanel.add(new JLabel("Student ID:"));
       inputPanel.add(idField);
       inputPanel.add(new JLabel("First Name:"));
       inputPanel.add(firstNameField);
       inputPanel.add(new JLabel("Last Name:"));
       inputPanel.add(lastNameField);


       int result = JOptionPane.showConfirmDialog(
               this,
               inputPanel,
               "Add New Student",
               JOptionPane.OK_CANCEL_OPTION,
               JOptionPane.PLAIN_MESSAGE
       );


       if (result == JOptionPane.OK_OPTION) {
           String id = idField.getText().trim();
           String firstName = firstNameField.getText().trim();
           String lastName = lastNameField.getText().trim();


           // Validation
           if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
               showError("All fields are required");
               return;
           }


           if (!isValidStudentId(id)) {
               showError("Student ID must start with a letter and contain only letters and numbers");
               return;
           }


           if (students.stream().anyMatch(s -> s.getId().equals(id))) {
               showError("Student ID already exists");
               return;
           }


           Student student = new Student(id, firstName, lastName);
           students.add(student);
           updateStudentTable();
           updateGradeComponents();
           updateEnrollmentLists();
           showSuccess("Student added successfully");
           updateStatus("Added new student: " + student.getFullName());
       }
   }


   /**
    * Displays a dialog for updating an existing student's information.
    * Validates input and updates the student if valid.
    */
   private void showUpdateStudentDialog() {
       int selectedRow = studentTable.getSelectedRow();
       if (selectedRow < 0) {
           showError("Please select a student to update");
           return;
       }


       Student student = students.get(selectedRow);
       JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
       JTextField firstNameField = new JTextField(student.getFirstName());
       JTextField lastNameField = new JTextField(student.getLastName());


       inputPanel.add(new JLabel("First Name:"));
       inputPanel.add(firstNameField);
       inputPanel.add(new JLabel("Last Name:"));
       inputPanel.add(lastNameField);


       int result = JOptionPane.showConfirmDialog(
               this,
               inputPanel,
               "Update Student: " + student.getId(),
               JOptionPane.OK_CANCEL_OPTION,
               JOptionPane.PLAIN_MESSAGE
       );


       if (result == JOptionPane.OK_OPTION) {
           String newFirstName = firstNameField.getText().trim();
           String newLastName = lastNameField.getText().trim();


           if (newFirstName.isEmpty() || newLastName.isEmpty()) {
               showError("Both name fields are required");
               return;
           }


           student.setFirstName(newFirstName);
           student.setLastName(newLastName);
           updateStudentTable();
           showSuccess("Student updated successfully");
           updateStatus("Updated student: " + student.getFullName());
       }
   }


   /**
    * Deletes the currently selected student after confirmation.
    * Removes all associated enrollment and grade records.
    */
   private void deleteStudent() {
       int selectedRow = studentTable.getSelectedRow();
       if (selectedRow < 0) {
           showError("Please select a student to delete");
           return;
       }


       Student student = students.get(selectedRow);
       if (!confirmAction("Delete student " + student.getFullName() + "?")) {
           return;
       }


       students.remove(student);
       grades.remove(student);
       updateStudentTable();
       updateGradeComponents();
       updateEnrollmentLists();
       showSuccess("Student deleted successfully");
       updateStatus("Deleted student: " + student.getFullName());
   }


   /**
    * Displays detailed information about the selected student.
    * Shows ID, name, enrolled courses, and assigned grades.
    */
   private void showStudentDetails() {
       int selectedRow = studentTable.getSelectedRow();
       if (selectedRow < 0) {
           showError("Please select a student");
           return;
       }


       Student student = students.get(selectedRow);
       StringBuilder details = new StringBuilder();
       details.append("ID: ").append(student.getId()).append("\n");
       details.append("Name: ").append(student.getFullName()).append("\n\n");
       details.append("Enrolled Courses:\n");


       if (grades.containsKey(student)) {
           for (Course course : grades.get(student).keySet()) {
               String grade = grades.get(student).get(course);
               details.append("- ").append(course.getCode())
                       .append(" (").append(course.getTitle()).append(")");
               if (!grade.isEmpty()) {
                   details.append(" - Grade: ").append(grade);
               }
               details.append("\n");
           }
       } else {
           details.append("No course enrollments\n");
       }


       JOptionPane.showMessageDialog(
               this,
               details.toString(),
               "Student Details: " + student.getFullName(),
               JOptionPane.INFORMATION_MESSAGE
       );
       updateStatus("Viewing details for: " + student.getFullName());
   }


   /**
    * Filters students based on search query.
    * Searches ID, first name, last name, and full name.
    * @param query the search string to match against student records
    */
   private void searchStudents(String query) {
       if (query == null || query.trim().isEmpty()) {
           updateStudentTable();
           updateStatus("Showing all students");
           return;
       }


       String q = query.toLowerCase();
       studentTableModel.setRowCount(0);
       int matches = 0;


       for (Student student : students) {
           if (student.getId().toLowerCase().contains(q) ||
                   student.getFirstName().toLowerCase().contains(q) ||
                   student.getLastName().toLowerCase().contains(q) ||
                   student.getFullName().toLowerCase().contains(q)) {
               int enrollments = grades.containsKey(student) ? grades.get(student).size() : 0;
               double gpa = calculateGPA(student);
               studentTableModel.addRow(new Object[]{
                       student.getId(),
                       student.getFullName(),
                       enrollments,
                       gpa > 0 ? String.format("%.2f", gpa) : "N/A"
               });
               matches++;
           }
       }
       updateStatus("Found " + matches + " students matching '" + query + "'");
   }


   /**
    * Updates the student table with current student data.
    * Includes ID, name, enrollment count, and GPA for each student.
    */
   private void updateStudentTable() {
       studentTableModel.setRowCount(0);
       for (Student student : students) {
           int enrollments = grades.containsKey(student) ? grades.get(student).size() : 0;
           double gpa = calculateGPA(student);
           studentTableModel.addRow(new Object[]{
                   student.getId(),
                   student.getFullName(),
                   enrollments,
                   gpa > 0 ? String.format("%.2f", gpa) : "N/A"
           });
       }
   }


   /**
    * Creates and configures the Course Enrollment panel.
    * @return the configured JPanel containing enrollment components
    */
   private JPanel createEnrollmentPanel() {
       JPanel panel = new JPanel(new BorderLayout(10, 10));
       panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


       // Top controls
       JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));


       // Left panel for enrollment
       JPanel enrollPanel = new JPanel(new BorderLayout());
       enrollCourseCombo = new JComboBox<>(courses.toArray(new Course[0]));
       enrollCourseCombo.setToolTipText("Select a course to manage enrollments");
       enrollCourseCombo.addActionListener(e -> updateEnrollmentLists());


       enrollStudentListModel = new DefaultListModel<>();
       enrollStudentList = new JList<>(enrollStudentListModel);
       enrollStudentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       enrollStudentList.setToolTipText("Students available for enrollment");


       JButton enrollBtn = new JButton("Enroll Selected");
       enrollBtn.setToolTipText("Enroll selected student in selected course");
       enrollBtn.addActionListener(e -> enrollStudent());


       enrollPanel.add(new JLabel("Available Students:"), BorderLayout.NORTH);
       enrollPanel.add(new JScrollPane(enrollStudentList), BorderLayout.CENTER);
       enrollPanel.add(enrollBtn, BorderLayout.SOUTH);


       // Right panel for dropping
       JPanel dropPanel = new JPanel(new BorderLayout());
       enrolledStudentListModel = new DefaultListModel<>();
       enrolledStudentList = new JList<>(enrolledStudentListModel);
       enrolledStudentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       enrolledStudentList.setToolTipText("Students currently enrolled in course");


       JButton dropBtn = new JButton("Drop Selected");
       dropBtn.setToolTipText("Drop selected student from selected course");
       dropBtn.addActionListener(e -> dropCourse());


       dropPanel.add(new JLabel("Enrolled Students:"), BorderLayout.NORTH);
       dropPanel.add(new JScrollPane(enrolledStudentList), BorderLayout.CENTER);
       dropPanel.add(dropBtn, BorderLayout.SOUTH);


       controlPanel.add(enrollPanel);
       controlPanel.add(dropPanel);


       // Add course selection at the very top
       JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       topPanel.add(new JLabel("Select Course:"));
       topPanel.add(enrollCourseCombo);


       // Initialize enrollment table model and table
       String[] columns = {"Course", "Enrollments"};
       enrollmentTableModel = new DefaultTableModel(columns, 0) {
           @Override
           public boolean isCellEditable(int row, int column) {
               return false;
           }
       };
       enrollmentTable = new JTable(enrollmentTableModel);
       enrollmentTable.setToolTipText("Course enrollment summary");


       // Create a panel for the table
       JPanel tablePanel = new JPanel(new BorderLayout());
       tablePanel.add(new JLabel("Enrollment Summary:"), BorderLayout.NORTH);
       tablePanel.add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);


       // Main layout
       panel.add(topPanel, BorderLayout.NORTH);
       panel.add(controlPanel, BorderLayout.CENTER);
       panel.add(tablePanel, BorderLayout.SOUTH);


       updateEnrollmentLists();
       return panel;
   }


   /**
    * Updates the enrollment lists based on the selected course.
    * Shows available students and currently enrolled students.
    */
   private void updateEnrollmentLists() {
       Course selectedCourse = (Course) enrollCourseCombo.getSelectedItem();


       // Clear both lists
       enrollStudentListModel.clear();
       enrolledStudentListModel.clear();


       if (selectedCourse != null) {
           for (Student student : students) {
               if (isEnrolled(student, selectedCourse)) {
                   enrolledStudentListModel.addElement(student);
               } else {
                   enrollStudentListModel.addElement(student);
               }
           }
       }


       // Update enrollment table
       if (enrollmentTableModel != null) {
           enrollmentTableModel.setRowCount(0);
           for (Course course : courses) {
               int count = 0;
               for (Map<Course, String> studentGrades : grades.values()) {
                   if (studentGrades.containsKey(course)) {
                       count++;
                   }
               }
               enrollmentTableModel.addRow(new Object[]{
                       course.getCode() + ": " + course.getTitle(),
                       count + " students"
               });
           }
       }
   }


   /**
    * Enrolls the selected student in the selected course.
    * Validates selections and updates data structures.
    */
   private void enrollStudent() {
       Student student = enrollStudentList.getSelectedValue();
       Course course = (Course) enrollCourseCombo.getSelectedItem();


       if (student == null || course == null) {
           showError("Please select both a student and a course");
           return;
       }


       if (isEnrolled(student, course)) {
           showError("Student is already enrolled in this course");
           return;
       }


       grades.computeIfAbsent(student, k -> new HashMap<>()).put(course, "");
       updateEnrollmentLists();
       updateStudentTable();
       updateGradeComponents();
       showSuccess(student.getFullName() + " enrolled in " + course.getCode());
       updateStatus("Enrolled " + student.getFullName() + " in " + course.getCode());
   }


   /**
    * Drops the selected student from the selected course.
    * Validates selections and updates data structures.
    */
   private void dropCourse() {
       Student student = enrolledStudentList.getSelectedValue();
       Course course = (Course) enrollCourseCombo.getSelectedItem();


       if (student == null || course == null) {
           showError("Please select both a student and a course");
           return;
       }


       if (!isEnrolled(student, course)) {
           showError("Student is not enrolled in this course");
           return;
       }


       if (!confirmAction("Drop " + student.getFullName() + " from " + course.getCode() + "?")) {
           return;
       }


       grades.get(student).remove(course);
       updateEnrollmentLists();
       updateStudentTable();
       updateGradeComponents();
       showSuccess("Course dropped successfully");
       updateStatus("Dropped " + student.getFullName() + " from " + course.getCode());
   }


   /**
    * Creates and configures the Grade Management panel.
    * @return the configured JPanel containing grade management components
    */
   private JPanel createGradePanel() {
       JPanel panel = new JPanel(new BorderLayout(10, 10));
       panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


       // Grade assignment controls
       JPanel controlPanel = new JPanel(new GridLayout(2, 3, 10, 10));


       gradeStudentCombo = new JComboBox<>();
       gradeStudentCombo.setToolTipText("Select student to assign grade");
       gradeStudentCombo.addActionListener(e -> updateGradeCourseCombo());


       gradeCourseCombo = new JComboBox<>();
       gradeCourseCombo.setToolTipText("Select course to assign grade for");
       gradeField = new JTextField(5);
       gradeField.setToolTipText("Enter grade (A-F)");


       JButton assignBtn = new JButton("Assign Grade");
       assignBtn.setToolTipText("Assign grade to selected student for selected course");
       assignBtn.addActionListener(e -> assignGrade());


       controlPanel.add(new JLabel("Student:"));
       controlPanel.add(new JLabel("Course:"));
       controlPanel.add(new JLabel("Grade (A-F):"));
       controlPanel.add(gradeStudentCombo);
       controlPanel.add(gradeCourseCombo);
       controlPanel.add(gradeField);


       // Grade table
       String[] columns = {"Student", "Course", "Grade"};
       gradeTableModel = new DefaultTableModel(columns, 0) {
           @Override
           public boolean isCellEditable(int row, int column) {
               return false;
           }
       };


       gradeTable = new JTable(gradeTableModel);
       gradeTable.setToolTipText("List of assigned grades");


       // Add components
       panel.add(controlPanel, BorderLayout.NORTH);
       panel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);
       panel.add(assignBtn, BorderLayout.SOUTH);


       updateGradeComponents();
       return panel;
   }


   /**
    * Updates the grade management components with current data.
    * Populates student dropdown and refreshes grade table.
    */
   private void updateGradeComponents() {
       // Update student combo
       gradeStudentCombo.removeAllItems();
       for (Student student : students) {
           gradeStudentCombo.addItem(student);
       }


       updateGradeCourseCombo();
       updateGradeTable();
   }


   /**
    * Updates the course dropdown based on the selected student's enrollments.
    */
   private void updateGradeCourseCombo() {
       gradeCourseCombo.removeAllItems();
       Student student = (Student) gradeStudentCombo.getSelectedItem();


       if (student != null && grades.containsKey(student)) {
           for (Course course : grades.get(student).keySet()) {
               gradeCourseCombo.addItem(course);
           }
       }
   }


   /**
    * Assigns a grade to the selected student for the selected course.
    * Validates inputs and updates data structures.
    */
   private void assignGrade() {
       Student student = (Student) gradeStudentCombo.getSelectedItem();
       Course course = (Course) gradeCourseCombo.getSelectedItem();
       String grade = gradeField.getText().trim().toUpperCase();


       if (student == null || course == null || grade.isEmpty()) {
           showError("Please select student, course, and enter a grade");
           return;
       }


       if (!grade.matches("[A-F]")) {
           showError("Grade must be between A and F");
           return;
       }


       if (!isEnrolled(student, course)) {
           showError("Student is not enrolled in this course");
           return;
       }


       grades.get(student).put(course, grade);
       updateGradeTable();
       updateStudentTable();
       gradeField.setText("");
       showSuccess("Grade assigned successfully");
       updateStatus("Assigned grade " + grade + " to " + student.getFullName() + " for " + course.getCode());
   }


   /**
    * Updates the grade table with current grade assignments.
    */
   private void updateGradeTable() {
       gradeTableModel.setRowCount(0);


       for (Map.Entry<Student, Map<Course, String>> entry : grades.entrySet()) {
           for (Map.Entry<Course, String> gradeEntry : entry.getValue().entrySet()) {
               if (!gradeEntry.getValue().isEmpty()) {
                   gradeTableModel.addRow(new Object[]{
                           entry.getKey().getFullName(),
                           gradeEntry.getKey().getCode(),
                           gradeEntry.getValue()
                   });
               }
           }
       }
   }


   /**
    * Displays the About dialog with application information.
    */
   private void showAboutDialog() {
       String aboutText = "Student Management System\n" +
               "Version 1.0\n\n" +
               "A GUI application for managing student records,\n" +
               "course enrollments, and grades.\n\n" +
               "© 2023 University Administration";


       JOptionPane.showMessageDialog(
               this,
               aboutText,
               "About Student Management System",
               JOptionPane.INFORMATION_MESSAGE
       );
   }


   /**
    * Calculates the GPA for a student based on assigned grades.
    * @param student the student to calculate GPA for
    * @return the calculated GPA (0.0 if no grades assigned)
    */
   private double calculateGPA(Student student) {
       if (!grades.containsKey(student)) return 0.0;


       double totalPoints = 0;
       int count = 0;


       for (String grade : grades.get(student).values()) {
           if (!grade.isEmpty()) {
               totalPoints += convertGradeToPoints(grade);
               count++;
           }
       }


       return count > 0 ? totalPoints / count : 0.0;
   }


   /**
    * Converts a letter grade to GPA points.
    * @param grade the letter grade (A-F)
    * @return the corresponding GPA points (4.0 for A, 3.0 for B, etc.)
    */
   private double convertGradeToPoints(String grade) {
       switch (grade.toUpperCase()) {
           case "A": return 4.0;
           case "B": return 3.0;
           case "C": return 2.0;
           case "D": return 1.0;
           default: return 0.0;
       }
   }


   /**
    * Validates a student ID format.
    * @param id the student ID to validate
    * @return true if valid (starts with letter, contains only alphanumeric chars)
    */
   private boolean isValidStudentId(String id) {
       return id != null && id.matches("^[A-Za-z][A-Za-z0-9]*$");
   }


   /**
    * Checks if a student is enrolled in a course.
    * @param student the student to check
    * @param course the course to check
    * @return true if the student is enrolled in the course
    */
   private boolean isEnrolled(Student student, Course course) {
       return grades.containsKey(student) && grades.get(student).containsKey(course);
   }


   /**
    * Displays a confirmation dialog with the specified message.
    * @param message the confirmation message to display
    * @return true if user confirms, false otherwise
    */
   private boolean confirmAction(String message) {
       return JOptionPane.showConfirmDialog(
               this,
               message,
               "Confirm Action",
               JOptionPane.YES_NO_OPTION
       ) == JOptionPane.YES_OPTION;
   }


   /**
    * Displays an error dialog with the specified message.
    * @param message the error message to display
    */
   private void showError(String message) {
       JOptionPane.showMessageDialog(
               this,
               message,
               "Error",
               JOptionPane.ERROR_MESSAGE
       );
   }


   /**
    * Displays a success dialog with the specified message.
    * @param message the success message to display
    */
   private void showSuccess(String message) {
       JOptionPane.showMessageDialog(
               this,
               message,
               "Success",
               JOptionPane.INFORMATION_MESSAGE
       );
   }


   /**
    * Main method to launch the application.
    * @param args command line arguments (not used)
    */
   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
           } catch (Exception e) {
               e.printStackTrace();
           }
           new StudentManagementGUI();
       });
   }
}
