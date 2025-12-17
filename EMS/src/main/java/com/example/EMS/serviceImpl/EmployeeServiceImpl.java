package com.example.EMS.serviceImpl;


import com.example.EMS.dto.ResignationDetailsDTO;
import com.example.EMS.dto.ResignedEmployeesDTO;
import com.example.EMS.dto.ResponseDataBeanDTO;
import com.example.EMS.entity.Employee;
import com.example.EMS.entity.EmployeeResignationDetails;
import com.example.EMS.enums.ResignationStatus;
import com.example.EMS.repo.EmployeeDirectoryRepo;
import com.example.EMS.repo.EmployeeResignationDirectoryRepo;
import com.example.EMS.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDirectoryRepo employeeDirectoryRepo;
    private final EmployeeResignationDirectoryRepo employeeResignationDirectoryRepo;

    @Override
    public ResponseEntity<?> createUser(Employee employee){
        log.info("Inside createUser - {}", employee);
        try{
            employeeDirectoryRepo.save(employee);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<?> login(Employee employee) {
        log.info("Inside login - {}", employee);
        try{
            Employee existingUser = employeeDirectoryRepo.existsByEmployeeNumber(employee.getEmployeeNumber());
            if(existingUser != null){
                EmployeeResignationDetails employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(existingUser.getResignationId());

                //check for the RM first, if he is an RM then cannot be a HR
                List<Employee> reporteeList = new ArrayList<>();
                boolean isRM = false;
                List<Employee> rmReporteeList = employeeDirectoryRepo.findByRmEmployeeNumber(existingUser.getEmployeeNumber());
                if(rmReporteeList.isEmpty()){
                    List<Employee> hrReporteeList = employeeDirectoryRepo.findByHrEmployeeNumber(existingUser.getEmployeeNumber());
                    if(!hrReporteeList.isEmpty()){
                        reporteeList =  hrReporteeList;
                    }
                }else{
                    reporteeList = rmReporteeList;
                    isRM = true;
                }
                List<ResignedEmployeesDTO> resignedEmployeesList = new ArrayList<>();
                ResignationStatus status = isRM
                        ? ResignationStatus.SUBMITTTED
                        : ResignationStatus.APPROVED_BY_RM;
                for(Employee reportee: reporteeList){
                    if(reportee.getResignationId() != null){
                        EmployeeResignationDetails reporteeResignationDetails =
                                employeeResignationDirectoryRepo
                                        .findByResignationIdAndStatusEquals(reportee.getResignationId(), status);
                        if (reporteeResignationDetails != null){
                            ResignedEmployeesDTO resignedEmployeesDTO = new ResignedEmployeesDTO(reportee, reporteeResignationDetails);
                            resignedEmployeesList.add(resignedEmployeesDTO);
                        }
                    }
                }

                return ResponseEntity.ok(new ResponseDataBeanDTO(
                        employeeResignationDetails,
                        resignedEmployeesList
                ));

            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
