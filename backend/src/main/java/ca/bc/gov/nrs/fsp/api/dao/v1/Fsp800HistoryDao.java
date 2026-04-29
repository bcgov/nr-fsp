package ca.bc.gov.nrs.fsp.api.dao.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.bean.OrgUnitArrayElement;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Wraps Oracle package FSP_800_HISTORY (legacy: pkgdefinitions/Fsp800History.java).
 */
public interface Fsp800HistoryDao {

  String PACKAGE_NAME = "FSP_800_HISTORY";
  String PROCEDURE_NAME = "MAINLINE";

  record Header(
      String pAction,
      String pFspId,
      String pNewFspId,
      String pFspPlanName,
      List<OrgUnitArrayElement> pFspOrgUnits,
      String pFspStatusCode,
      String pFspStatusDesc,
      String pFspAmendmentNumber,
      String pNewFspAmendmentNumber,
      String pUserClientNumber,
      String pUserRole,
      String pSubmissionId,
      String pFspExpiryDate,
      String pAmendmentName,
      String pAmendmentEfftvDate,
      String pSortOrder,
      String pErrorMessage
  ) {}

  /** P_FSP_SEARCH_RESULTS REF CURSOR row. */
  record Row(
      String amendmentNumber,
      String extensionNumber,
      String eventDateTime,
      String userId,
      String event,
      String description,
      String approvalRequestIndicator,
      String submissionId
  ) {}

  record Result(Header header, List<Row> rows) {}

  Result mainline(
      String pAction,
      String pFspId,
      String pNewFspId,
      String pFspPlanName,
      @Nullable List<OrgUnitArrayElement> pFspOrgUnits,
      String pFspStatusCode,
      String pFspStatusDesc,
      String pFspAmendmentNumber,
      String pNewFspAmendmentNumber,
      String pUserClientNumber,
      String pUserRole,
      String pSubmissionId,
      String pFspExpiryDate,
      String pAmendmentName,
      String pAmendmentEfftvDate,
      String pSortOrder
  );
}
