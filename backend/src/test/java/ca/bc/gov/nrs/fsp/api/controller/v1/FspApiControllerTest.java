package ca.bc.gov.nrs.fsp.api.controller.v1;

import ca.bc.gov.nrs.fsp.api.service.v1.AttachmentsService;
import ca.bc.gov.nrs.fsp.api.service.v1.FspService;
import ca.bc.gov.nrs.fsp.api.service.v1.HistoryService;
import ca.bc.gov.nrs.fsp.api.service.v1.InboxService;
import ca.bc.gov.nrs.fsp.api.service.v1.StandardsService;
import ca.bc.gov.nrs.fsp.api.service.v1.WorkflowService;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentResponse;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Smoke tests that the controller routes wire to the right service method and
 * that JWT scope auth is enforced. Service implementations are mocked — DAO
 * tests against the real Oracle packages live separately.
 */
@SpringBootTest(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://example.invalid/",
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://example.invalid/jwks.json"
})
@AutoConfigureMockMvc
class FspApiControllerTest {

  @Autowired MockMvc mvc;

  @MockitoBean FspService fspService;
  @MockitoBean WorkflowService workflowService;
  @MockitoBean StandardsService standardsService;
  @MockitoBean AttachmentsService attachmentsService;
  @MockitoBean InboxService inboxService;
  @MockitoBean HistoryService historyService;

  @Test
  void searchFsp_returnsRows() throws Exception {
    when(fspService.search(any(FspSearchRequest.class))).thenReturn(List.of(
        FspSearchResult.builder().fspId("123").planName("Test Plan").build()));

    mvc.perform(get("/api/v1/fsp/search")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fspId").value("123"))
        .andExpect(jsonPath("$[0].planName").value("Test Plan"));
  }

  @Test
  void getAttachments_requiresFspReadScope() throws Exception {
    when(attachmentsService.getByFspId("42")).thenReturn(List.of(
        AttachmentResponse.builder().fspAttachmentId("7").attachmentName("a.pdf").build()));

    mvc.perform(get("/api/v1/fsp/42/attachments")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fspAttachmentId").value("7"));
  }

  @Test
  void getInbox_unauthorized_withoutScope() throws Exception {
    mvc.perform(get("/api/v1/fsp/inbox").with(jwt()))
        .andExpect(status().isForbidden());
  }
}
