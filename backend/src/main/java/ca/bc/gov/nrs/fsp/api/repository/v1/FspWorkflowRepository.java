package ca.bc.gov.nrs.fsp.api.repository.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FspWorkflowRepository extends JpaRepository<FspWorkflow, Long> {
  List<FspWorkflow> findByFspIdOrderByActionTimestampDesc(Long fspId);
}