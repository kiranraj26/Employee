package com.example.employee;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.*;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> { 
	Page<Employee> findByLocation(String location, Pageable pageable); 
	Page<Employee> findByDepartment(String department, Pageable pageable); 
	Page<Employee> findByLocationAndDepartment(String location, String department, Pageable pageable); 
	}
