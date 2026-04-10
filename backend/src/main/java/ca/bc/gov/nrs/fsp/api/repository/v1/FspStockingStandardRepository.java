package ca.bc.gov.nrs.fsp.api.repository.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspStockingStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FspStockingStandardRepository extends JpaRepository<FspStockingStandard, Long> {
  List<FspStockingStandard> findByFspId(Long fspId);
  void deleteByFspId(Long fspId);
}