package com.example.EMS.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "IT_CHECKLIST")
@Data
public class ItChecklist {

    @Id
    private Integer checklistId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "checklist_id")
    private ConsolidatedChecklist consolidatedChecklist;

    private boolean submitted;
    private boolean damaged;
    private boolean paid;
}
