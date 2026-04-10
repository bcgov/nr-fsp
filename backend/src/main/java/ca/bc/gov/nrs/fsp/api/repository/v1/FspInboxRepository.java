package ca.bc.gov.nrs.fsp.api.repository.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspInbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FspInboxRepository extends JpaRepository<FspInbox, Long> {
  List<FspInbox> findByOrgUnitNo(Long orgUnitNo);
  List<FspInbox> findByClientNumber(String clientNumber);
}