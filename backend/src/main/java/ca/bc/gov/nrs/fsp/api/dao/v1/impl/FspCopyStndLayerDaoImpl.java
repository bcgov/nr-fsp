package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.FspCopyStndLayerDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FspCopyStndLayerDaoImpl extends AbstractStoredProcedureDao implements FspCopyStndLayerDao {

  private static final int PARAM_COUNT = 8;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public FspCopyStndLayerDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pStockingStandardUnitId, String pStockingLayerId,
      String pStandardsRegimeLayerId, String pStandardsRegimeId,
      String pStockingLayerList, String pUpdateUserid) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pStockingStandardUnitId);
          setInOutString(cs, 3, pStockingLayerId);
          setInOutString(cs, 4, pStandardsRegimeLayerId);
          setInOutString(cs, 5, pStandardsRegimeId);
          setInOutString(cs, 6, pStockingLayerList);
          setInOutString(cs, 7, pUpdateUserid);
          setInOutString(cs, 8, null);  // P_ERROR_MESSAGE
        },
        cs -> {
          Result r = new Result(
              cs.getString(1), cs.getString(2), cs.getString(3),
              cs.getString(4), cs.getString(5), cs.getString(6),
              cs.getString(7), cs.getString(8));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, r.pErrorMessage());
          return r;
        });
  }
}
