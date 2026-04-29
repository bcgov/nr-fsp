package ca.bc.gov.nrs.fsp.api.dao.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.bean.LicenseeArrayElement;
import ca.bc.gov.nrs.fsp.api.dao.v1.bean.OrgUnitArrayElement;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Wraps Oracle package fsp_300_information (legacy: pkgdefinitions/Fsp300Information.java).
 * 46 INOUT parameters. P_FSP_ORG_UNITS, P_FSP_LICENSES, P_ORG_UNITS are VARRAYs.
 */
public interface Fsp300InformationDao {

  String PACKAGE_NAME = "fsp_300_information";
  String PROCEDURE_NAME = "MAINLINE";

  /** All INOUT echoed-back values (positions 1..46). */
  record Result(
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
      String pNewFspPlanName,
      String pNewAmendmentName,
      String pFspPlanStartDate,
      String pFspPlanTermYears,
      String pFspPlanTermMonths,
      String pFspPlanEndDate,
      String pFspPlanSubmissionDate,
      String pFspContactName,
      String pFspTelephoneNumber,
      String pFspEmailAddress,
      String pFduUpdateInd,
      String pIdentifiedAreasUpdateInd,
      String pStockingStandardUpdateInd,
      String pApprovalRequiredInd,
      String pTransitionInd,
      String pFrpa197electionInd,
      String pAmendmentAuthority,
      List<LicenseeArrayElement> pFspLicenses,
      List<OrgUnitArrayElement> pOrgUnits,
      String pFspStatus,
      String pEntryUserid,
      String pAmendmentReason,
      String pFspRejectedByEsfInd,
      String pFspUnapprovedAmendsInd,
      String pFspExtensionStat,
      String pFspAmendmentCode,
      String pFspAmendmentDesc,
      String pIsExpiryChanged,
      String pRevisionCount,
      String pUpdateUserid,
      String pErrorMessage
  ) {}

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
      String pNewFspPlanName,
      String pNewAmendmentName,
      String pFspPlanStartDate,
      String pFspPlanTermYears,
      String pFspPlanTermMonths,
      String pFspPlanEndDate,
      String pFspPlanSubmissionDate,
      String pFspContactName,
      String pFspTelephoneNumber,
      String pFspEmailAddress,
      String pFduUpdateInd,
      String pIdentifiedAreasUpdateInd,
      String pStockingStandardUpdateInd,
      String pApprovalRequiredInd,
      String pTransitionInd,
      String pFrpa197electionInd,
      String pAmendmentAuthority,
      @Nullable List<LicenseeArrayElement> pFspLicenses,
      @Nullable List<OrgUnitArrayElement> pOrgUnits,
      String pFspStatus,
      String pEntryUserid,
      String pAmendmentReason,
      String pFspRejectedByEsfInd,
      String pFspUnapprovedAmendsInd,
      String pFspExtensionStat,
      String pFspAmendmentCode,
      String pFspAmendmentDesc,
      String pIsExpiryChanged,
      String pRevisionCount,
      String pUpdateUserid
  );
}
