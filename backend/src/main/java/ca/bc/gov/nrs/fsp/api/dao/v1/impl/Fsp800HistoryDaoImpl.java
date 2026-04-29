package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp800HistoryDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.bean.OrgUnitArrayElement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Fsp800HistoryDaoImpl extends AbstractStoredProcedureDao implements Fsp800HistoryDao {

  private static final int PARAM_COUNT = 18;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public Fsp800HistoryDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pFspId, String pNewFspId, String pFspPlanName,
      @Nullable List<OrgUnitArrayElement> pFspOrgUnits,
      String pFspStatusCode, String pFspStatusDesc,
      String pFspAmendmentNumber, String pNewFspAmendmentNumber,
      String pUserClientNumber, String pUserRole, String pSubmissionId,
      String pFspExpiryDate, String pAmendmentName, String pAmendmentEfftvDate,
      String pSortOrder) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pFspId);
          setInOutString(cs, 3, pNewFspId);
          setInOutString(cs, 4, pFspPlanName);
          setInOutOrgUnits(cs, 5, pFspOrgUnits);
          setInOutString(cs, 6, pFspStatusCode);
          setInOutString(cs, 7, pFspStatusDesc);
          setInOutString(cs, 8, pFspAmendmentNumber);
          setInOutString(cs, 9, pNewFspAmendmentNumber);
          setInOutString(cs, 10, pUserClientNumber);
          setInOutString(cs, 11, pUserRole);
          setInOutString(cs, 12, pSubmissionId);
          setInOutString(cs, 13, pFspExpiryDate);
          setInOutString(cs, 14, pAmendmentName);
          setInOutString(cs, 15, pAmendmentEfftvDate);
          setInOutString(cs, 16, pSortOrder);
          registerOutCursor(cs, 17);     // P_FSP_SEARCH_RESULTS
          setInOutString(cs, 18, null);   // P_ERROR_MESSAGE
        },
        cs -> {
          List<Row> rows = readCursor(cs, 17, rs -> new Row(
              rs.getString(1),  // amendment_number
              rs.getString(2),  // extension_number
              rs.getString(3),  // event_date_time
              rs.getString(4),  // user_id
              rs.getString(5),  // event
              rs.getString(6),  // description
              rs.getString(7),  // approval_request_indicator
              rs.getString(8)   // submission_id
          ));
          Header header = new Header(
              cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4),
              readOrgUnits(cs, 5),
              cs.getString(6), cs.getString(7), cs.getString(8), cs.getString(9),
              cs.getString(10), cs.getString(11), cs.getString(12), cs.getString(13),
              cs.getString(14), cs.getString(15), cs.getString(16),
              cs.getString(18));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, header.pErrorMessage());
          return new Result(header, rows);
        });
  }
}
