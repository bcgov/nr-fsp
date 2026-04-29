package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp500StockingStandardsDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.bean.OrgUnitArrayElement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Fsp500StockingStandardsDaoImpl extends AbstractStoredProcedureDao implements Fsp500StockingStandardsDao {

  private static final int PARAM_COUNT = 17;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public Fsp500StockingStandardsDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pFspId, String pNewFspId, String pFspPlanName,
      @Nullable List<OrgUnitArrayElement> pFspOrgUnits,
      String pFspStatusCode, String pFspStatusDesc,
      String pNewFspAmendmentNumber, String pFspAmendmentNumber,
      String pUserClientNumber, String pUserRole, String pSubmissionId,
      String pFspExpiryDate, String pAmendmentName, String pAmendmentEfftvDate) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pFspId);
          setInOutString(cs, 3, pNewFspId);
          setInOutString(cs, 4, pFspPlanName);
          setInOutOrgUnits(cs, 5, pFspOrgUnits);
          setInOutString(cs, 6, pFspStatusCode);
          setInOutString(cs, 7, pFspStatusDesc);
          setInOutString(cs, 8, pNewFspAmendmentNumber);
          setInOutString(cs, 9, pFspAmendmentNumber);
          setInOutString(cs, 10, pUserClientNumber);
          setInOutString(cs, 11, pUserRole);
          setInOutString(cs, 12, pSubmissionId);
          setInOutString(cs, 13, pFspExpiryDate);
          setInOutString(cs, 14, pAmendmentName);
          setInOutString(cs, 15, pAmendmentEfftvDate);
          registerOutCursor(cs, 16);     // P_FSP_SS_RESULTS
          setInOutString(cs, 17, null);   // P_ERROR_MESSAGE
        },
        cs -> {
          List<Row> rows = readCursor(cs, 16, rs -> new Row(
              rs.getString(1),  // standards_regime_id
              rs.getString(2),  // standards_regime_name
              rs.getString(3),  // standards_objective
              rs.getString(4),  // standards_amnd_number
              rs.getString(5),  // standards_bgc
              rs.getString(6),  // standards_regime_status
              rs.getString(7),  // standards_effective_date
              rs.getString(8)   // default_standard_ind
          ));
          Header header = new Header(
              cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4),
              readOrgUnits(cs, 5),
              cs.getString(6), cs.getString(7), cs.getString(8), cs.getString(9),
              cs.getString(10), cs.getString(11), cs.getString(12), cs.getString(13),
              cs.getString(14), cs.getString(15),
              cs.getString(17));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, header.pErrorMessage());
          return new Result(header, rows);
        });
  }
}
