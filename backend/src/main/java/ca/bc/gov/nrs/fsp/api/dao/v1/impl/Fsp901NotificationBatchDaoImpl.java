package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp901NotificationBatchDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class Fsp901NotificationBatchDaoImpl extends AbstractStoredProcedureDao implements Fsp901NotificationBatchDao {

  private static final String CALL_GET = "{call " + PACKAGE_NAME + ".GET(?,?)}";
  private static final String CALL_CHANGE = "{call " + PACKAGE_NAME + ".CHANGE(?,?,?)}";

  public Fsp901NotificationBatchDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public GetResult get() {
    return executeCall(CALL_GET,
        cs -> {
          cs.registerOutParameter(1, Types.VARCHAR);  // error
          registerOutCursor(cs, 2);
        },
        cs -> {
          List<NotificationRow> rows = readCursor(cs, 2, rs -> new NotificationRow(
              rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
              rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
              rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
              rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16),
              rs.getString(17)));
          return new GetResult(cs.getString(1), rows);
        });
  }

  @Override
  public void change(String fspId, String fspAmendmentNumber, String maxStatusId) {
    executeCall(CALL_CHANGE,
        cs -> {
          cs.setString(1, fspId);
          cs.setString(2, fspAmendmentNumber);
          cs.setString(3, maxStatusId);
        },
        cs -> null);
  }
}
