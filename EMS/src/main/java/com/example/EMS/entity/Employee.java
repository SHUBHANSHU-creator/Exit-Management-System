package com.example.EMS.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "EMPLOYEE")
@Data
public class Employee {

    @Id
    private Integer EmployeeNumber;
    private String EmployeeName;
    private String EmployeeEmail;
    private String EmployeePhone;
    private String Designation;
    private String Band;
    private Integer RmEmployeeNumber;
    private Integer HrEmployeeNumber;
    private Integer ResignationId;
    private boolean isAdmin;
}
