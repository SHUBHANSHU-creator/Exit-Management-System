package com.example.EMS.dto;

import com.example.EMS.entity.Employee;
import com.example.EMS.entity.EmployeeResignationDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResignedEmployeesDTO {
    private Employee employeeInfo;
    private EmployeeResignationDetails employeeResignationDetailsInfo;
}
