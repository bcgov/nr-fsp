package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp550SubLayersDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Fsp550SubLayersDaoImpl extends AbstractStoredProcedureDao implements Fsp550SubLayersDao {

  private static final int PARAM_COUNT = 18;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public Fsp550SubLayersDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pStandardsRegimeId, String pStandardsRegimeLayerId,
      String pStockingLayerCode, String pTargetStocking, String pMinHorizontalDistance,
      String pMinPrefStockingStandard, String pMinStockingStandard,
      String pResidualBasalArea, String pMinPostSpacing, String pMaxPostSpacing,
      String pMaxConifer, String pHghtRelativeToComp, String pTreeSizeUnitCode,
      String pUpdateUserid, String pRevisionCount, String pStandardsRevisionCount) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pStandardsRegimeId);
          setInOutString(cs, 3, pStandardsRegimeLayerId);
          setInOutString(cs, 4, pStockingLayerCode);
          setInOutString(cs, 5, pTargetStocking);
          setInOutString(cs, 6, pMinHorizontalDistance);
          setInOutString(cs, 7, pMinPrefStockingStandard);
          setInOutString(cs, 8, pMinStockingStandard);
          setInOutString(cs, 9, pResidualBasalArea);
          setInOutString(cs, 10, pMinPostSpacing);
          setInOutString(cs, 11, pMaxPostSpacing);
          setInOutString(cs, 12, pMaxConifer);
          setInOutString(cs, 13, pHghtRelativeToComp);
          setInOutString(cs, 14, pTreeSizeUnitCode);
          setInOutString(cs, 15, pUpdateUserid);
          setInOutString(cs, 16, pRevisionCount);
          setInOutString(cs, 17, pStandardsRevisionCount);
          setInOutString(cs, 18, null);  // P_ERROR_MESSAGE
        },
        cs -> {
          Result r = new Result(
              cs.getString(1),  cs.getString(2),  cs.getString(3),  cs.getString(4),
              cs.getString(5),  cs.getString(6),  cs.getString(7),  cs.getString(8),
              cs.getString(9),  cs.getString(10), cs.getString(11), cs.getString(12),
              cs.getString(13), cs.getString(14), cs.getString(15), cs.getString(16),
              cs.getString(17), cs.getString(18));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, r.pErrorMessage());
          return r;
        });
  }
}
