package com.example.EMS.service;

import com.example.EMS.dto.ResignationDetailsDTO;
import com.example.EMS.entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeResignationService {
    ResponseEntity<?> processEmployeeActions(ResignationDetailsDTO resignationDetails);
    ResponseEntity<?> processRmActions(ResignationDetailsDTO resignationDetails);
    ResponseEntity<?> processHrActions(ResignationDetailsDTO resignationDetails);

}
