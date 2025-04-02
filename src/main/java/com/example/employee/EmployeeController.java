package com.example.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.*;



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
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String order,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String department,
        Model model) {
    
    Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    Page<Employee> employeePage = employeeService.getFilteredEmployees(location, department, pageable);

    model.addAttribute("employeePage", employeePage);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", employeePage.getTotalPages());
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("order", order);
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