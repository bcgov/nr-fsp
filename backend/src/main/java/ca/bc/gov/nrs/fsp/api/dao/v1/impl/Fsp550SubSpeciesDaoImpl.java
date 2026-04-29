package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp550SubSpeciesDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Fsp550SubSpeciesDaoImpl extends AbstractStoredProcedureDao implements Fsp550SubSpeciesDao {

  private static final int PARAM_COUNT = 12;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public Fsp550SubSpeciesDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pStandardsRegimeLayerId,
      String pSilvTreeSpeciesCodeNew, String pSilvTreeSpeciesCodeOld,
      String pMinHeight, String pPreferredInd,
      String pUpdateUserid, String pRevisionCount,
      String pStandardsRegimeId, String pStandardsRevisionCount) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pStandardsRegimeLayerId);
          setInOutString(cs, 3, pSilvTreeSpeciesCodeNew);
          setInOutString(cs, 4, pSilvTreeSpeciesCodeOld);
          setInOutString(cs, 5, pMinHeight);
          setInOutString(cs, 6, pPreferredInd);
          setInOutString(cs, 7, pUpdateUserid);
          setInOutString(cs, 8, pRevisionCount);
          setInOutString(cs, 9, pStandardsRegimeId);
          setInOutString(cs, 10, pStandardsRevisionCount);
          setInOutString(cs, 11, null);  // P_ERROR_MESSAGE
          registerOutCursor(cs, 12);     // P_RESULTS
        },
        cs -> {
          // Legacy reads cursor by column NAME.
          List<Row> rows = readCursor(cs, 12, rs -> new Row(
              rs.getString("SILV_TREE_SPECIES_CODE_NEW"),
              rs.getString("SILV_TREE_SPECIES_CODE_OLD"),
              rs.getString("SPECIES_DESCRIPTION"),
              rs.getString("MIN_HEIGHT"),
              rs.getString("REVISION_COUNT")
          ));
          Header header = new Header(
              cs.getString(1), cs.getString(2), cs.getString(3),
              cs.getString(4), cs.getString(5), cs.getString(6),
              cs.getString(7), cs.getString(8), cs.getString(9),
              cs.getString(10), cs.getString(11));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, header.pErrorMessage());
          return new Result(header, rows);
        });
  }
}
