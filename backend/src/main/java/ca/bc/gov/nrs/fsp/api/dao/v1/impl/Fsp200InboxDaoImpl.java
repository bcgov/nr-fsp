package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp200InboxDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Fsp200InboxDaoImpl extends AbstractStoredProcedureDao implements Fsp200InboxDao {

  private static final int PARAM_COUNT = 8;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public Fsp200InboxDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pOrgUnitNo, String pFspId, String pFspPlanName,
      String pFspStatusCode, String pAhClientNumber) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pOrgUnitNo);
          setInOutString(cs, 3, pFspId);
          setInOutString(cs, 4, pFspPlanName);
          setInOutString(cs, 5, pFspStatusCode);
          setInOutString(cs, 6, pAhClientNumber);
          registerOutCursor(cs, 7);            // P_FSP_SEARCH_RESULT
          setInOutString(cs, 8, null);          // P_ERROR_MESSAGE
        },
        cs -> {
          List<Row> rows = readCursor(cs, 7, rs -> new Row(
              rs.getString(1),  // fsp_id
              rs.getString(2),  // plan_name
              rs.getString(3),  // fsp_amendment_number
              rs.getString(4),  // extension_number
              rs.getString(5),  // update_userid
              rs.getString(6),  // fsp_status_desc
              rs.getString(7),  // plan_submission_date
              rs.getString(8),  // agreement_holder
              rs.getInt(9)      // number_of_fdu (legacy: rs.getInt)
          ));
          Header header = new Header(
              cs.getString(1), cs.getString(2), cs.getString(3),
              cs.getString(4), cs.getString(5), cs.getString(6),
              cs.getString(8));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, header.pErrorMessage());
          return new Result(header, rows);
        });
  }
}
