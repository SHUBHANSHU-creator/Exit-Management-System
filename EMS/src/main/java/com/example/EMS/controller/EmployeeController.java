package com.example.EMS.controller;

import com.example.EMS.entity.Employee;
import com.example.EMS.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/addUser")
    public ResponseEntity<?> AddEmployee(Employee emp) {
        log.info("Add Employee {}", emp);
        if (emp == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        employeeService.createUser(emp);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> LogIn(Employee emp) {
        log.info("LogIn {}", emp);
        if (emp == null && emp.getEmployeeNumber() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        employeeService.login(emp);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
