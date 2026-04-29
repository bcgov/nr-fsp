package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp100SearchDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp300InformationDao;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Search via FSP_100_SEARCH.MAINLINE; fetch + update via fsp_300_information.MAINLINE.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FspService {

  private static final String ACTION_FETCH = "FETCH";
  private static final String ACTION_UPDATE = "UPDATE";

  private final Fsp100SearchDao searchDao;
  private final Fsp300InformationDao informationDao;

  public List<FspSearchResult> search(FspSearchRequest request) {
    Fsp100SearchDao.Result result = searchDao.mainline(
        ACTION_FETCH,
        nz(request.getFspId()),
        nz(request.getFspPlanName()),
        nz(request.getOrgUnitNo()),
        nz(request.getAhClientNumber()),
        nz(request.getFspAmendmentName()),
        "",   // P_ENTRY_USER_CLIENT_NUMBER
        nz(RequestUtil.getCurrentUserName()),
        "",   // P_ENTRY_USER_ROLE
        nz(request.getFspDateStart()),
        nz(request.getFspDateEnd()),
        nz(request.getFspDateType()),
        nz(request.getFspStatusCode()),
        nz(request.getApprovalRequired())
    );
    return result.rows().stream().map(FspService::toSearchDto).toList();
  }

  public FspRequest getById(String fspId) {
    Fsp300InformationDao.Result r = callInformation(ACTION_FETCH, fspId, null);
    return toFspDto(r);
  }

  @Transactional
  public FspRequest update(String fspId, FspRequest request) {
    Fsp300InformationDao.Result r = callInformation(ACTION_UPDATE, fspId, request);
    return toFspDto(r);
  }

  private Fsp300InformationDao.Result callInformation(String action, String fspId, FspRequest request) {
    String userId = RequestUtil.getCurrentUserName();
    return informationDao.mainline(
        action,
        fspId,
        "",   // P_NEW_FSP_ID
        request == null ? "" : nz(request.getFspPlanName()),
        null, // P_FSP_ORG_UNITS — IN-only when not provided by client
        request == null ? "" : nz(request.getFspStatusCode()),
        request == null ? "" : nz(request.getFspStatusDesc()),
        request == null ? "" : nz(request.getFspAmendmentNumber()),
        "",   // P_NEW_FSP_AMENDMENT_NUMBER
        request == null ? "" : nz(request.getUserClientNumber()),
        request == null ? "" : nz(request.getUserRole()),
        request == null ? "" : nz(request.getSubmissionId()),
        request == null ? "" : nz(request.getFspExpiryDate()),
        request == null ? "" : nz(request.getAmendmentName()),
        request == null ? "" : nz(request.getAmendmentEfftvDate()),
        "",   // P_NEW_FSP_PLAN_NAME
        "",   // P_NEW_AMENDMENT_NAME
        request == null ? "" : nz(request.getFspPlanStartDate()),
        request == null ? "" : nz(request.getFspPlanTermYears()),
        request == null ? "" : nz(request.getFspPlanTermMonths()),
        request == null ? "" : nz(request.getFspPlanEndDate()),
        request == null ? "" : nz(request.getFspPlanSubmissionDate()),
        request == null ? "" : nz(request.getFspContactName()),
        request == null ? "" : nz(request.getFspTelephoneNumber()),
        request == null ? "" : nz(request.getFspEmailAddress()),
        request == null ? "" : nz(request.getFduUpdateInd()),
        request == null ? "" : nz(request.getIdentifiedAreasUpdateInd()),
        request == null ? "" : nz(request.getStockingStandardUpdateInd()),
        request == null ? "" : nz(request.getApprovalRequiredInd()),
        request == null ? "" : nz(request.getTransitionInd()),
        request == null ? "" : nz(request.getFrpa197electionInd()),
        request == null ? "" : nz(request.getAmendmentAuthority()),
        null, // P_FSP_LICENSES
        null, // P_ORG_UNITS
        "",   // P_FSP_STATUS
        userId,
        request == null ? "" : nz(request.getAmendmentReason()),
        request == null ? "" : nz(request.getFspRejectedByEsfInd()),
        request == null ? "" : nz(request.getFspUnapprovedAmendsInd()),
        request == null ? "" : nz(request.getFspExtensionStat()),
        request == null ? "" : nz(request.getFspAmendmentCode()),
        request == null ? "" : nz(request.getFspAmendmentDesc()),
        "",   // P_IS_EXPIRY_CHANGED
        request == null ? "" : nz(request.getRevisionCount()),
        userId  // P_UPDATE_USERID
    );
  }

  private static FspSearchResult toSearchDto(Fsp100SearchDao.Row row) {
    return FspSearchResult.builder()
        .fspId(row.fspId())
        .planName(row.planName())
        .fspAmendmentName(row.fspAmendmentName())
        .orgUnitCode(row.orgUnitCode())
        .planStartDate(row.planStartDate())
        .planEndDate(row.planEndDate())
        .fspAmendmentNumber(row.fspAmendmentNumber())
        .agreementHolder(row.agreementHolder())
        .amendmentApprovalRequirdInd(row.amendmentApprovalRequirdInd())
        .fspStatusDesc(row.fspStatusDesc())
        .build();
  }

  private static FspRequest toFspDto(Fsp300InformationDao.Result r) {
    return FspRequest.builder()
        .fspId(r.pFspId())
        .newFspId(r.pNewFspId())
        .fspPlanName(r.pFspPlanName())
        .fspStatusCode(r.pFspStatusCode())
        .fspStatusDesc(r.pFspStatusDesc())
        .fspAmendmentNumber(r.pFspAmendmentNumber())
        .newFspAmendmentNumber(r.pNewFspAmendmentNumber())
        .userClientNumber(r.pUserClientNumber())
        .userRole(r.pUserRole())
        .submissionId(r.pSubmissionId())
        .fspExpiryDate(r.pFspExpiryDate())
        .amendmentName(r.pAmendmentName())
        .amendmentEfftvDate(r.pAmendmentEfftvDate())
        .fspPlanStartDate(r.pFspPlanStartDate())
        .fspPlanTermYears(r.pFspPlanTermYears())
        .fspPlanTermMonths(r.pFspPlanTermMonths())
        .fspPlanEndDate(r.pFspPlanEndDate())
        .fspPlanSubmissionDate(r.pFspPlanSubmissionDate())
        .fspContactName(r.pFspContactName())
        .fspTelephoneNumber(r.pFspTelephoneNumber())
        .fspEmailAddress(r.pFspEmailAddress())
        .fduUpdateInd(r.pFduUpdateInd())
        .identifiedAreasUpdateInd(r.pIdentifiedAreasUpdateInd())
        .stockingStandardUpdateInd(r.pStockingStandardUpdateInd())
        .approvalRequiredInd(r.pApprovalRequiredInd())
        .transitionInd(r.pTransitionInd())
        .frpa197electionInd(r.pFrpa197electionInd())
        .amendmentAuthority(r.pAmendmentAuthority())
        .amendmentReason(r.pAmendmentReason())
        .fspRejectedByEsfInd(r.pFspRejectedByEsfInd())
        .fspUnapprovedAmendsInd(r.pFspUnapprovedAmendsInd())
        .fspExtensionStat(r.pFspExtensionStat())
        .fspAmendmentCode(r.pFspAmendmentCode())
        .fspAmendmentDesc(r.pFspAmendmentDesc())
        .revisionCount(r.pRevisionCount())
        .build();
  }

  private static String nz(String s) {
    return s == null ? "" : s;
  }
}
