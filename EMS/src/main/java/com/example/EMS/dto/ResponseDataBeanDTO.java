package com.example.EMS.dto;


import com.example.EMS.entity.Employee;
import com.example.EMS.entity.EmployeeResignationDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDataBeanDTO {
    private EmployeeResignationDetails employeeResignationDetails;
    private List<ResignedEmployeesDTO> resignedEmployeesList;
}
