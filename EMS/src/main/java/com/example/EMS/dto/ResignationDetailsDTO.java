package com.example.EMS.dto;

import com.example.EMS.enums.ResignationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResignationDetailsDTO {
    private Integer employeeNumber;
    private ResignationStatus actionStatus;
    private String reason;

}
