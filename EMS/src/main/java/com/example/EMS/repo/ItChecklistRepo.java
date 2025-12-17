package com.example.EMS.repo;

import com.example.EMS.entity.ItChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItChecklistRepo extends JpaRepository<ItChecklist, Integer> {
    ItChecklist findByChecklistId(Integer checklistId);
}
