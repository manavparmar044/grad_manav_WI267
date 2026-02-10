import java.util.*;
import java.util.stream.Collectors;

class Employee {

    private String name;
    private String gender;
    private int salary;
    private String designation;
    private String department;

    public Employee(String name, String gender, int salary,
                    String designation, String department) {
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.designation = designation;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getSalary() {
        return salary;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return name + " | " + gender + " | " + salary +
               " | " + designation + " | " + department;
    }
}

public class StreamApiAssignment {

    public static void main(String[] args) {

        List<Employee> employees = Arrays.asList(
                new Employee("Amit", "Male", 90000, "Manager", "IT"),
                new Employee("Riya", "Female", 60000, "Developer", "IT"),
                new Employee("Rahul", "Male", 55000, "Developer", "IT"),
                new Employee("Neha", "Female", 70000, "Lead", "HR"),
                new Employee("Suresh", "Male", 80000, "Manager", "HR"),
                new Employee("Pooja", "Female", 45000, "Executive", "HR"),
                new Employee("Karan", "Male", 50000, "Analyst", "Finance"),
                new Employee("Anjali", "Female", 52000, "Analyst", "Finance"),
                new Employee("Vikas", "Male", 65000, "Lead", "Finance"),
                new Employee("Sneha", "Female", 48000, "Executive", "Finance"),
                new Employee("Arjun", "Male", 75000, "Manager", "Sales"),
                new Employee("Kavita", "Female", 58000, "Executive", "Sales"),
                new Employee("Rohit", "Male", 62000, "Salesman", "Sales"),
                new Employee("Meena", "Female", 54000, "Salesman", "Sales"),
                new Employee("Deepak", "Male", 72000, "Lead", "IT"),
                new Employee("Nisha", "Female", 68000, "Lead", "IT"),
                new Employee("Manoj", "Male", 47000, "Clerk", "Admin"),
                new Employee("Sunita", "Female", 49000, "Clerk", "Admin"),
                new Employee("Ajay", "Male", 53000, "Officer", "Admin"),
                new Employee("Reena", "Female", 56000, "Officer", "Admin"),
                new Employee("Sanjay", "Male", 88000, "Senior Manager", "Operations"),
                new Employee("Priya", "Female", 61000, "Coordinator", "Operations"),
                new Employee("Alok", "Male", 59000, "Coordinator", "Operations"),
                new Employee("Divya", "Female", 64000, "Lead", "Operations"),
                new Employee("Nitin", "Male", 70000, "Supervisor", "Operations")
        );

        Employee highestPaid = employees.stream()
                .max(Comparator.comparingInt(Employee::getSalary))
                .orElse(null);

        System.out.println("Highest Paid Employee:");
        System.out.println(highestPaid);

        Map<String, Long> genderCount = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getGender,
                        Collectors.counting()
                ));

        System.out.println("\nGender Count:");
        genderCount.forEach((gender, count) ->
                System.out.println(gender + " : " + count));

        Map<String, Long> departmentExpense = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingLong(Employee::getSalary)
                ));

        System.out.println("\nDepartment Wise Expense:");
        departmentExpense.forEach((dept, expense) ->
                System.out.println(dept + " : " + expense));

        System.out.println("\nTop 5 Senior Employees:");
        employees.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .limit(5)
                .forEach(System.out::println);

 
        System.out.println("\nManagers:");
        employees.stream()
                .filter(e -> e.getDesignation().toLowerCase().contains("manager"))
                .map(Employee::getName)
                .forEach(System.out::println);

        employees.stream()
                .filter(e -> !e.getDesignation().toLowerCase().contains("manager"))
                .forEach(e -> e.setSalary(e.getSalary() * 120 / 100));

        System.out.println("\nAfter 20% Salary Hike (Except Managers):");
        employees.forEach(System.out::println);

        long totalEmployees = employees.stream().count();
        System.out.println("\nTotal Number of Employees: " + totalEmployees);
    }
}
