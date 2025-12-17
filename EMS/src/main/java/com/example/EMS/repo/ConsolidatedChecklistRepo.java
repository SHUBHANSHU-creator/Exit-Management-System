package com.example.EMS.repo;

import com.example.EMS.entity.ConsolidatedChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsolidatedChecklistRepo extends JpaRepository<ConsolidatedChecklist, Integer> {
    ConsolidatedChecklist findByResignationId(Integer resignationId);

    List<ConsolidatedChecklist> findByItChecklistClosedFalse();

    List<ConsolidatedChecklist> findByLoanChecklistClosedFalse();
}
