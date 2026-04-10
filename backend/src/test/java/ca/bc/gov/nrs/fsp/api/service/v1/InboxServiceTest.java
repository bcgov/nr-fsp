package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspInbox;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspInboxRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboxServiceTest {

  @Mock FspInboxRepository inboxRepository;

  @InjectMocks InboxService inboxService;

  private FspInbox inboxItem;
  private Jwt jwt;

  @BeforeEach
  void setUp() {
    inboxItem = new FspInbox();
    inboxItem.setFspId(1L);
    inboxItem.setClientNumber("00123456");
    inboxItem.setClientName("Test Licensee");
    inboxItem.setFspStatusCode("APP");
    inboxItem.setOrgUnitNo(100L);
    inboxItem.setDistrictName("Prince George Natural Resource District");
    inboxItem.setFspEndDate(LocalDate.now().plusDays(30));

    jwt = mock(Jwt.class);
  }

  @Test
  void getInboxForCurrentUser_withOrgUnitClaim_returnsFilteredItems() {
    when(jwt.getClaim("custom:org_unit_no")).thenReturn(100L);
    when(inboxRepository.findByOrgUnitNo(100L)).thenReturn(List.of(inboxItem));

    try (MockedStatic<RequestUtil> requestUtil = mockStatic(RequestUtil.class)) {
      requestUtil.when(RequestUtil::getCurrentJwt).thenReturn(jwt);

      List<FspSearchResult> result = inboxService.getInboxForCurrentUser();

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getFspId()).isEqualTo(1L);
      assertThat(result.get(0).getDistrictName())
          .isEqualTo("Prince George Natural Resource District");
      verify(inboxRepository).findByOrgUnitNo(100L);
    }
  }

  @Test
  void getInboxForCurrentUser_withNoOrgUnitClaim_returnsEmptyList() {
    when(jwt.getClaim("custom:org_unit_no")).thenReturn(null);

    try (MockedStatic<RequestUtil> requestUtil = mockStatic(RequestUtil.class)) {
      requestUtil.when(RequestUtil::getCurrentJwt).thenReturn(jwt);

      List<FspSearchResult> result = inboxService.getInboxForCurrentUser();

      assertThat(result).isEmpty();
      verifyNoInteractions(inboxRepository);
    }
  }
}
