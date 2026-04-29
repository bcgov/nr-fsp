package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp200InboxDao;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wraps fsp_200_inbox.MAINLINE. The legacy app's inbox screen calls this with
 * P_ACTION = "FETCH" and a P_ORG_UNIT_NO scoped to the user's district. The
 * org-unit number is read from the "custom:org_unit_no" Cognito claim — adjust
 * if your user pool uses a different attribute.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InboxService {

  private static final String ACTION_FETCH = "FETCH";

  private final Fsp200InboxDao inboxDao;

  public List<FspSearchResult> getInboxForCurrentUser() {
    Jwt jwt = RequestUtil.getCurrentJwt();
    Object orgUnitClaim = jwt.getClaim("custom:org_unit_no");
    String orgUnitNo = orgUnitClaim == null ? "" : orgUnitClaim.toString();

    Fsp200InboxDao.Result result = inboxDao.mainline(
        ACTION_FETCH,
        orgUnitNo,   // P_ORG_UNIT_NO
        "",          // P_FSP_ID
        "",          // P_FSP_PLAN_NAME
        "",          // P_FSP_STATUS_CODE
        ""           // P_AH_CLIENT_NUMBER
    );
    return result.rows().stream().map(InboxService::toDto).toList();
  }

  private static FspSearchResult toDto(Fsp200InboxDao.Row row) {
    return FspSearchResult.builder()
        .fspId(row.fspId())
        .planName(row.planName())
        .fspAmendmentNumber(row.fspAmendmentNumber())
        .extensionNumber(row.extensionNumber())
        .updateUserid(row.updateUserid())
        .fspStatusDesc(row.fspStatusDesc())
        .planSubmissionDate(row.planSubmissionDate())
        .agreementHolder(row.agreementHolder())
        .numberOfFdu(row.numberOfFdu())
        .build();
  }
}
