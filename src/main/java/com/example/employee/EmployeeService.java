package com.example.employee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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