package com.example.EMS.repo;

import com.example.EMS.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDirectoryRepo extends JpaRepository<Employee, Integer> {
    Employee existsByEmployeeNumber(Integer employeeNumber);

    Employee findByEmployeeNumber(Integer employeeNumber);

    List<Employee> findByRmEmployeeNumber(Integer employeeNumber);

    List<Employee> findByHrEmployeeNumber(Integer employeeNumber);
}
