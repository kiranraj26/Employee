import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String department;
    
    // Getters and Setters
}

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String department,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.getFilteredEmployees(location, department, pageable);

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("location", location);
        model.addAttribute("department", department);
        
        return "employees";
    }

    @GetMapping("/employees/add")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        employee.ifPresent(e -> model.addAttribute("employee", e));
        return "employee-form";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> getFilteredEmployees(String location, String department, Pageable pageable) {
        if (location != null && department != null) {
            return employeeRepository.findByLocationAndDepartment(location, department, pageable);
        } else if (location != null) {
            return employeeRepository.findByLocation(location, pageable);
        } else if (department != null) {
            return employeeRepository.findByDepartment(department, pageable);
        } else {
            return employeeRepository.findAll(pageable);
        }
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByLocation(String location, Pageable pageable);
    Page<Employee> findByDepartment(String department, Pageable pageable);
    Page<Employee> findByLocationAndDepartment(String location, String department, Pageable pageable);
}

<!-- Thymeleaf Template: employees.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Employee List</title>
</head>
<body>
    <h2>Employee List</h2>
    <a href="/employees/add">Add Employee</a>
    <form method="get" action="/employees">
        <label>Location: <input type="text" name="location" th:value="${location}"/></label>
        <label>Department: <input type="text" name="department" th:value="${department}"/></label>
        <button type="submit">Filter</button>
    </form>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Location</th>
            <th>Department</th>
            <th>Actions</th>
        </tr>
        <tr th:each="employee : ${employeePage.content}">
            <td th:text="${employee.id}"></td>
            <td th:text="${employee.name}"></td>
            <td th:text="${employee.location}"></td>
            <td th:text="${employee.department}"></td>
            <td>
                <a th:href="@{/employees/edit/{id}(id=${employee.id})}">Edit</a>
                <a th:href="@{/employees/delete/{id}(id=${employee.id})}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </table>
    <div>
        <a th:if="${currentPage > 0}" th:href="@{/employees(page=${currentPage - 1}, size=${size}, location=${location}, department=${department})}">Previous</a>
        <span th:text="'Page ' + (${currentPage + 1}) + ' of ' + ${totalPages}"></span>
        <a th:if="${currentPage + 1 < totalPages}" th:href="@{/employees(page=${currentPage + 1}, size=${size}, location=${location}, department=${department})}">Next</a>
    </div>
</body>
</html>

<!-- Thymeleaf Template: employee-form.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Employee Form</title>
</head>
<body>
    <h2>Employee Form</h2>
    <form th:action="@{/employees/save}" th:object="${employee}" method="post">
        <input type="hidden" th:field="*{id}"/>
        <label>Name: <input type="text" th:field="*{name}"/></label>
        <label>Location: <input type="text" th:field="*{location}"/></label>
        <label>Department: <input type="text" th:field="*{department}"/></label>
        <button type="submit">Save</button>
    </form>
</body>
</html>


I've removed sorting from the Employee Management System while keeping pagination and filtering by location and department. Let me know if you need any further modifications!
