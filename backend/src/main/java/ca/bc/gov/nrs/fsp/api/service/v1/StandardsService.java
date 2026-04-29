package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp500StockingStandardsDao;
import ca.bc.gov.nrs.fsp.api.struct.v1.StandardRequest;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Wraps FSP_500_STOCKING_STANDARDS.MAINLINE. The legacy app dispatches
 * INSERT/UPDATE/DELETE/FETCH via P_ACTION, with the cursor returning the
 * post-mutation regime list.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StandardsService {

  private static final String ACTION_FETCH = "FETCH";
  private static final String ACTION_DELETE = "DELETE";

  private final Fsp500StockingStandardsDao stockingStandardsDao;

  public List<StandardRequest> getByFspId(String fspId) {
    Fsp500StockingStandardsDao.Result result = call(ACTION_FETCH, fspId);
    return result.rows().stream().map(StandardsService::toDto).toList();
  }

  @Transactional
  public List<StandardRequest> saveAll(String fspId, List<StandardRequest> requests) {
    // FSP_500 doesn't expose a bulk-replace as a single proc call — each
    // standard is its own INSERT/UPDATE per-row. Until per-row save semantics
    // are confirmed with the DBAs, return the current list.
    return getByFspId(fspId);
  }

  @Transactional
  public void delete(String standardId) {
    call(ACTION_DELETE, standardId);
  }

  private Fsp500StockingStandardsDao.Result call(String action, String fspId) {
    return stockingStandardsDao.mainline(
        action,
        fspId,
        "",   // P_NEW_FSP_ID
        "",   // P_FSP_PLAN_NAME
        null, // P_FSP_ORG_UNITS
        "", "",   // status code/desc
        "", "",   // amendment number / new amendment number
        RequestUtil.getCurrentUserName(),  // P_USER_CLIENT_NUMBER (proxy)
        "",   // P_USER_ROLE
        "",   // P_SUBMISSION_ID
        "",   // P_FSP_EXPIRY_DATE
        "",   // P_AMENDMENT_NAME
        ""    // P_AMENDMENT_EFFTV_DATE
    );
  }

  private static StandardRequest toDto(Fsp500StockingStandardsDao.Row row) {
    return StandardRequest.builder()
        .standardsRegimeId(row.standardsRegimeId())
        .standardsRegimeName(row.standardsRegimeName())
        .standardsObjective(row.standardsObjective())
        .standardsAmndNumber(row.standardsAmndNumber())
        .standardsBgc(row.standardsBgc())
        .standardsRegimeStatus(row.standardsRegimeStatus())
        .standardsEffectiveDate(row.standardsEffectiveDate())
        .defaultStandardInd(row.defaultStandardInd())
        .build();
  }
}
