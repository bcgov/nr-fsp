package ca.bc.gov.nrs.fsp.api.repository.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.Fsp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FspRepository extends JpaRepository<Fsp, Long> {

  List<Fsp> findByClientNumber(String clientNumber);

  List<Fsp> findByOrgUnitNoAndFspStatusCode(Long orgUnitNo, String statusCode);

  @Query("SELECT f FROM Fsp f WHERE " +
      "(:clientNumber IS NULL OR f.clientNumber = :clientNumber) AND " +
      "(:orgUnitNo IS NULL OR f.orgUnitNo = :orgUnitNo) AND " +
      "(:statusCode IS NULL OR f.fspStatusCode = :statusCode)")
  List<Fsp> search(
      @Param("clientNumber") String clientNumber,
      @Param("orgUnitNo") Long orgUnitNo,
      @Param("statusCode") String statusCode
  );
}