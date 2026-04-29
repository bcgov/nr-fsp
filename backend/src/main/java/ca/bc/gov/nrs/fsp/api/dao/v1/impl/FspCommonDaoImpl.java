package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.FspCommonDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

/**
 * fsp_common.get_error_message is declared as a function (returns VARCHAR) in the
 * legacy descriptor, with one IN VARCHAR parameter (p_error_key). Call it via
 * the standard JDBC function-call escape.
 */
@Repository
public class FspCommonDaoImpl extends AbstractStoredProcedureDao implements FspCommonDao {

  private static final String CALL = "{? = call " + PACKAGE_NAME + ".get_error_message(?)}";

  public FspCommonDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public String getErrorMessage(String pErrorKey) {
    return executeCall(CALL,
        cs -> {
          cs.registerOutParameter(1, Types.VARCHAR);
          cs.setString(2, pErrorKey);
        },
        cs -> cs.getString(1));
  }
}
