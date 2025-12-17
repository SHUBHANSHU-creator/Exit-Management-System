package com.example.EMS.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CONSOLIDATED_CHECKLIST")
@Data
public class ConsolidatedChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer checklistId;
    private Integer resignationId;
    private boolean itChecklistClosed;
    private boolean loanChecklistClosed;
}
