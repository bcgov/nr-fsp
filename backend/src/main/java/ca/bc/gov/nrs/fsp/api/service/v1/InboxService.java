package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspInbox;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspInboxRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InboxService {

  private final FspInboxRepository inboxRepository;

  public List<FspSearchResult> getInboxForCurrentUser() {
    Jwt jwt = RequestUtil.getCurrentJwt();
    // Cognito claim for org unit — adjust claim name to match your user pool attributes
    Long orgUnitNo = jwt.getClaim("custom:org_unit_no");
    List<FspInbox> items = orgUnitNo != null
        ? inboxRepository.findByOrgUnitNo(orgUnitNo)
        : List.of();

    return items.stream().map(i -> FspSearchResult.builder()
        .fspId(i.getFspId())
        .clientNumber(i.getClientNumber())
        .clientName(i.getClientName())
        .fspStatusCode(i.getFspStatusCode())
        .orgUnitNo(i.getOrgUnitNo())
        .districtName(i.getDistrictName())
        .fspEndDate(i.getFspEndDate())
        .build()
    ).collect(Collectors.toList());
  }
}