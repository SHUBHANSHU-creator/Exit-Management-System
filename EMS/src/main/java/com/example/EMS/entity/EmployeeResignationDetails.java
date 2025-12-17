package com.example.EMS.entity;

import com.example.EMS.enums.ResignationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "EMPLOYEE_RESIGNATION_DETAILS")
@Data
public class EmployeeResignationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ResignationId;
    private Date ResignationDate;
    private String ResignationReason;
    private Date lwd;
    private boolean isRetained;
    private ResignationStatus status;

}
