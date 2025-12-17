package com.example.EMS.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "EMPLOYEE")
@Data
public class Employee {

    @Id
    @Column(name = "employee_number")
    private Integer employeeNumber;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_email")
    private String employeeEmail;

    @Column(name = "employee_phone")
    private String employeePhone;

    @Column(name = "designation")
    private String designation;

    @Column(name = "band")
    private String band;

    @Column(name = "rm_employee_number")
    private Integer rmEmployeeNumber;

    @Column(name = "hr_employee_number")
    private Integer hrEmployeeNumber;

    @Column(name = "resignation_id")
    private Integer resignationId;

    @Column(name = "is_admin")
    private boolean admin;
}
