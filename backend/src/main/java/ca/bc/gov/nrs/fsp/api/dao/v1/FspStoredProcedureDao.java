package ca.bc.gov.nrs.fsp.api.dao.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * DAO for Oracle stored procedure / package calls that return REF CURSORs
 * or use complex IN/OUT parameters not suited to JPA.
 * Procedure and package names must match your Oracle schema exactly.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class FspStoredProcedureDao {

  private final DataSource dataSource;

  /**
   * FSP search via Oracle package — mirrors legacy FSP100 behaviour.
   * Adjust FSP_PKG / FSP100_SEARCH and parameter names to match your schema.
   */
  public List<Map<String, Object>> fsp100Search(
      String clientNumber, Long orgUnitNo, String statusCode) {

    SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
        .withCatalogName("FSP_PKG")
        .withProcedureName("FSP100_SEARCH")
        .declareParameters(
            new SqlParameter("p_client_number", Types.VARCHAR),
            new SqlParameter("p_org_unit_no", Types.NUMERIC),
            new SqlParameter("p_status_code", Types.VARCHAR),
            new SqlOutParameter("p_result_cursor", OracleTypes.CURSOR),
            new SqlOutParameter("p_return_code", Types.NUMERIC),
            new SqlOutParameter("p_return_msg", Types.VARCHAR)
        );

    Map<String, Object> out = call.execute(Map.of(
        "p_client_number", clientNumber != null ? clientNumber : "",
        "p_org_unit_no", orgUnitNo != null ? orgUnitNo : 0L,
        "p_status_code", statusCode != null ? statusCode : ""
    ));

    checkReturnCode(out);

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> rows = (List<Map<String, Object>>) out.get("p_result_cursor");
    return rows;
  }

  /**
   * Submit a workflow action via Oracle package.
   * Adjust FSP_WORKFLOW_PKG / FSP_SUBMIT and parameter names to match your schema.
   */
  public void fspSubmit(Long fspId, String userId, String action, String comments) {
    SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
        .withCatalogName("FSP_WORKFLOW_PKG")
        .withProcedureName("FSP_SUBMIT")
        .declareParameters(
            new SqlParameter("p_fsp_id", Types.NUMERIC),
            new SqlParameter("p_user_id", Types.VARCHAR),
            new SqlParameter("p_action", Types.VARCHAR),
            new SqlParameter("p_comments", Types.VARCHAR),
            new SqlOutParameter("p_return_code", Types.NUMERIC),
            new SqlOutParameter("p_return_msg", Types.VARCHAR)
        );

    Map<String, Object> out = call.execute(Map.of(
        "p_fsp_id", fspId,
        "p_user_id", userId,
        "p_action", action,
        "p_comments", comments != null ? comments : ""
    ));

    checkReturnCode(out);
  }

  private void checkReturnCode(Map<String, Object> out) {
    Number returnCode = (Number) out.get("p_return_code");
    if (returnCode != null && returnCode.intValue() != 0) {
      String msg = (String) out.get("p_return_msg");
      log.error("Oracle procedure returned error [{}]: {}", returnCode, msg);
      throw new RuntimeException("Oracle procedure error [" + returnCode + "]: " + msg);
    }
  }
}