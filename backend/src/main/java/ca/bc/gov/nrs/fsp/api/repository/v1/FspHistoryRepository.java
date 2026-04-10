package ca.bc.gov.nrs.fsp.api.repository.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FspHistoryRepository extends JpaRepository<FspHistory, Long> {
  List<FspHistory> findByFspIdOrderByActionTimestampDesc(Long fspId);
}