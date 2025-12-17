package com.example.EMS.repo;


import com.example.EMS.entity.EmployeeResignationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeResignationDirectoryRepo extends JpaRepository<EmployeeResignationDetails, Integer> {
    EmployeeResignationDetails findByResignationId(Integer resignationId);
}
