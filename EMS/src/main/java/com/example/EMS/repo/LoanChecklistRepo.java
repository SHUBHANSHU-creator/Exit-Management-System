package com.example.EMS.repo;

import com.example.EMS.entity.LoanChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanChecklistRepo extends JpaRepository<LoanChecklist, Integer> {
    LoanChecklist findByChecklistId(Integer checklistId);
}
