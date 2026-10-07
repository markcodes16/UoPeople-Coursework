import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmployeeService {
   private final List<Employee> employees;


   /**
    * Initialize with a pre-built list (for testing/demo).
    * In a real application, you might load this list from a file or database.
    */
   public EmployeeService(List<Employee> employees) {
       // Defensive copy so external modifications won't affect internal list
       this.employees = new ArrayList<>(employees);
   }


   /**
    * Load employees from a CSV file with lines like "name,age,department,salary".
    * @param filename the path to the CSV file
    * @return a List of Employee objects parsed from that file
    * @throws IOException if the file cannot be read
    */
   public static List<Employee> loadFromCsv(String filename) throws IOException {
       return Files.lines(Paths.get(filename))
               .map(line -> {
                   // Example line: "John Doe,28,Engineering,75000"
                   String[] tokens = line.split(",");
                   String name = tokens[0].trim();
                   int age = Integer.parseInt(tokens[1].trim());
                   String dept = tokens[2].trim();
                   double salary = Double.parseDouble(tokens[3].trim());
                   return new Employee(name, age, dept, salary);
               })
               .collect(Collectors.toList());
   }


   /**
    * Returns a List<String> where each entry is "<name> - <department>".
    * Demonstrates the Function<Employee, String> interface:
    *   - Function<T,R> is a functional interface whose single method is R apply(T t).
    *   - In streams, .map(...) accepts a Function<T,U> to transform each element.
    */
   public List<String> getNameDeptList() {
       Function<Employee, String> nameDeptFn =
               e -> e.getName() + " - " + e.getDepartment();


       return employees.stream()
               .map(nameDeptFn)
               .collect(Collectors.toList());
   }


   /** Returns the average salary of all employees, or 0.0 if the list is empty. */
   public double getAverageSalary() {
       return employees.stream()
               .mapToDouble(Employee::getSalary)
               .average()
               .orElse(0.0);
   }


   /**
    * Returns a list of employees whose age is greater than the given threshold.
    * @param threshold the age threshold
    * @return a List<Employee> with age > threshold
    */
   public List<Employee> filterByAge(int threshold) {
       return employees.stream()
               .filter(e -> e.getAge() > threshold)
               .collect(Collectors.toList());
   }


   /**
    * Groups employees by department. The returned map's key is the department name,
    * and the value is the List<Employee> in that department.
    */
   public Map<String, List<Employee>> groupByDepartment() {
       return employees.stream()
               .collect(Collectors.groupingBy(Employee::getDepartment));
   }


   /**
    * (Bonus) Returns a map from department -> average salary in that department,
    * using a downstream collector: groupingBy + averagingDouble.
    */
   public Map<String, Double> getAvgSalaryByDepartment() {
       return employees.stream()
               .collect(Collectors.groupingBy(
                       Employee::getDepartment,
                       Collectors.averagingDouble(Employee::getSalary)
               ));
   }
}
