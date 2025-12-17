package com.example.EMS.entity;

import com.example.EMS.enums.ResignationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "EMPLOYEE_RESIGNATION_DETAILS")
@Data
public class EmployeeResignationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resignation_id")
    private Integer resignationId;

    @Column(name = "resignation_date")
    private Date resignationDate;

    @Column(name = "resignation_reason")
    private String resignationReason;

    @Column(name = "lwd")
    private Date lwd;

    @Column(name = "is_retained")
    private boolean retained;

    private ResignationStatus status;

}
