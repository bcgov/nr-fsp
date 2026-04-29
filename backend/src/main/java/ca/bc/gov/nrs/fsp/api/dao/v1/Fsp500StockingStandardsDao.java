package ca.bc.gov.nrs.fsp.api.dao.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.bean.OrgUnitArrayElement;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Wraps Oracle package FSP_500_STOCKING_STANDARDS (legacy: pkgdefinitions/Fsp500StockingStandards.java).
 */
public interface Fsp500StockingStandardsDao {

  String PACKAGE_NAME = "FSP_500_STOCKING_STANDARDS";
  String PROCEDURE_NAME = "MAINLINE";

  record Header(
      String pAction,
      String pFspId,
      String pNewFspId,
      String pFspPlanName,
      List<OrgUnitArrayElement> pFspOrgUnits,
      String pFspStatusCode,
      String pFspStatusDesc,
      String pNewFspAmendmentNumber,
      String pFspAmendmentNumber,
      String pUserClientNumber,
      String pUserRole,
      String pSubmissionId,
      String pFspExpiryDate,
      String pAmendmentName,
      String pAmendmentEfftvDate,
      String pErrorMessage
  ) {}

  /** P_FSP_SS_RESULTS REF CURSOR row. */
  record Row(
      String standardsRegimeId,
      String standardsRegimeName,
      String standardsObjective,
      String standardsAmndNumber,
      String standardsBgc,
      String standardsRegimeStatus,
      String standardsEffectiveDate,
      String defaultStandardInd
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
      String pNewFspAmendmentNumber,
      String pFspAmendmentNumber,
      String pUserClientNumber,
      String pUserRole,
      String pSubmissionId,
      String pFspExpiryDate,
      String pAmendmentName,
      String pAmendmentEfftvDate
  );
}
