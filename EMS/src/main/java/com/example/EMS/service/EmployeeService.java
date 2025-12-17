package com.example.EMS.service;

import com.example.EMS.entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    ResponseEntity<?> createUser(Employee employee);
    ResponseEntity<?> login(Employee employee);
}
