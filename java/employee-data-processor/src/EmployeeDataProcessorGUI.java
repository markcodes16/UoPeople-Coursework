import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EmployeeDataProcessorGUI extends JFrame {
   private final EmployeeService service;
   private final JTextArea outputArea;
   private final JTextField ageThresholdField;


   public EmployeeDataProcessorGUI() {
       setTitle("Employee Data Processor");
       setSize(700, 500);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLayout(new BorderLayout());


       // Attempt to load employees from "employees.csv"; fallback to hard-coded data
       List<Employee> employeeList;
       try {
           employeeList = EmployeeService.loadFromCsv("employees.csv");
       } catch (IOException e) {
           employeeList = List.of(
                   new Employee("John Doe", 28, "Engineering", 75000),
                   new Employee("Jane Smith", 35, "Marketing", 82000),
                   new Employee("Mike Johnson", 42, "Engineering", 95000),
                   new Employee("Sarah Williams", 31, "HR", 68000),
                   new Employee("David Brown", 29, "Marketing", 78000),
                   new Employee("Emily Davis", 45, "Executive", 120000)
           );
           JOptionPane.showMessageDialog(
                   this,
                   "Could not load employees.csv; using sample data instead.",
                   "Data Load Warning",
                   JOptionPane.WARNING_MESSAGE
           );
       }
       this.service = new EmployeeService(employeeList);


       // Create UI components
       outputArea = new JTextArea();
       outputArea.setEditable(false);
       JScrollPane scrollPane = new JScrollPane(outputArea);


       JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
       JButton showAllButton    = new JButton("1) Show All Employees");
       JButton nameDeptButton   = new JButton("2) Name-Dept Strings");
       JButton avgSalaryButton  = new JButton("3) Average Salary");
       ageThresholdField        = new JTextField("30", 4);
       JButton filterButton     = new JButton("4) Filter by Age");
       JButton groupDeptButton  = new JButton("5) Group by Dept");
       JButton avgByDeptButton  = new JButton("6) Avg Salary by Dept");


       controlPanel.add(showAllButton);
       controlPanel.add(nameDeptButton);
       controlPanel.add(avgSalaryButton);
       controlPanel.add(new JLabel("Age >"));
       controlPanel.add(ageThresholdField);
       controlPanel.add(filterButton);
       controlPanel.add(groupDeptButton);
       controlPanel.add(avgByDeptButton);


       add(controlPanel, BorderLayout.NORTH);
       add(scrollPane, BorderLayout.CENTER);


       // Hook up action listeners
       showAllButton.addActionListener(e -> showAllEmployees());
       nameDeptButton.addActionListener(e -> showNameDeptStrings());
       avgSalaryButton.addActionListener(e -> showAverageSalary());
       filterButton.addActionListener(e -> filterByAge());
       groupDeptButton.addActionListener(e -> groupByDepartment());
       avgByDeptButton.addActionListener(e -> showAvgSalaryByDept());


       // Initial display
       showAllEmployees();
   }


   /** Display every employee's toString() in the text area. */
   private void showAllEmployees() {
       outputArea.setText("All Employees:\n");
       // Calling filterByAge(-1) just returns everyone (age > -1)
       List<Employee> all = service.filterByAge(-1);
       all.forEach(e -> outputArea.append(e + "\n"));
   }


   /**
    * Use Function<Employee, String> to map each Employee to "name - department".
    * Demonstrates how a Function<T,R> is used in streams via .map(...).
    */
   private void showNameDeptStrings() {
       List<String> nameDeptList = service.getNameDeptList();
       outputArea.setText("Employee Name-Department:\n");
       nameDeptList.forEach(s -> outputArea.append(s + "\n"));
   }


   /** Compute and display the average salary of all employees. */
   private void showAverageSalary() {
       double avg = service.getAverageSalary();
       outputArea.setText(String.format("Average Salary (All): $%,.2f\n", avg));
   }


   /** Filter employees by the age threshold (parsed from the text field). */
   private void filterByAge() {
       try {
           int threshold = Integer.parseInt(ageThresholdField.getText().trim());
           List<Employee> filtered = service.filterByAge(threshold);
           outputArea.setText("Employees with age > " + threshold + ":\n");
           filtered.forEach(e -> outputArea.append(e + "\n"));


           // Bonus: show average salary of this filtered group
           double avgOfGroup = filtered.stream()
                   .mapToDouble(Employee::getSalary)
                   .average()
                   .orElse(0.0);
           outputArea.append(String.format(
                   "%nAverage Salary (age > %d): $%,.2f%n",
                   threshold, avgOfGroup
           ));
       } catch (NumberFormatException ex) {
           JOptionPane.showMessageDialog(
                   this,
                   "Please enter a valid integer for age threshold.",
                   "Input Error",
                   JOptionPane.ERROR_MESSAGE
           );
       }
   }


   /** Group employees by department and display each group with its members. */
   private void groupByDepartment() {
       Map<String, List<Employee>> map = service.groupByDepartment();
       outputArea.setText("Employees Grouped by Department:\n");
       map.forEach((dept, list) -> {
           outputArea.append(String.format("%n=== %s ===%n", dept));
           list.forEach(e -> outputArea.append("  " + e + "\n"));
       });
   }


   /** Bonus: show department -> average salary using a downstream collector. */
   private void showAvgSalaryByDept() {
       Map<String, Double> avgMap = service.getAvgSalaryByDepartment();
       outputArea.setText("Average Salary by Department:\n");
       avgMap.forEach((dept, avg) -> {
           outputArea.append(String.format("  %s: $%,.2f%n", dept, avg));
       });
   }


   public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
           EmployeeDataProcessorGUI gui = new EmployeeDataProcessorGUI();
           gui.setVisible(true);
       });
   }
}
