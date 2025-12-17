package com.example.EMS.dto;

import com.example.EMS.enums.ChecklistType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistViewRequest {
    private ChecklistType checklistType;
}
