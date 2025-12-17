package com.example.EMS.dto;

import com.example.EMS.enums.ChecklistType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistCloseRequest {
    private Integer employeeNumber;
    private ChecklistType checklistType;

    private Boolean loanTaken;
    private Boolean repaid;

    private Boolean submitted;
    private Boolean damaged;
    private Boolean paid;
}
