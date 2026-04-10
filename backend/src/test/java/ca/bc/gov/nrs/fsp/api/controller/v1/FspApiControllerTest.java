package ca.bc.gov.nrs.fsp.api.controller.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.FspStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.service.v1.*;
import ca.bc.gov.nrs.fsp.api.struct.v1.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://cognito-idp.ca-central-1.amazonaws.com/test-pool",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://cognito-idp.ca-central-1.amazonaws.com/test-pool/.well-known/jwks.json"
})
@AutoConfigureMockMvc
class FspApiControllerTest {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @MockitoBean FspService fspService;
  @MockitoBean WorkflowService workflowService;
  @MockitoBean StandardsService standardsService;
  @MockitoBean AttachmentsService attachmentsService;
  @MockitoBean InboxService inboxService;
  @MockitoBean HistoryService historyService;
  @MockitoBean FspStoredProcedureDao fspStoredProcedureDao;

  private FspRequest fspRequest;
  private FspSearchResult searchResult;
  private WorkflowResponse workflowResponse;

  @BeforeEach
  void setUp() {
    fspRequest = new FspRequest();
    fspRequest.setFspId(1L);
    fspRequest.setClientNumber("00123456");
    fspRequest.setClientName("Test Licensee");
    fspRequest.setFspStatusCode("APP");

    searchResult = FspSearchResult.builder()
        .fspId(1L)
        .clientNumber("00123456")
        .clientName("Test Licensee")
        .fspStatusCode("APP")
        .build();

    workflowResponse = WorkflowResponse.builder()
        .fspWorkflowId(10L)
        .fspId(1L)
        .workflowStatusCode("SUBMITTED")
        .actionUserid("TESTUSER")
        .actionTimestamp(LocalDateTime.now())
        .build();
  }

  @Test
  void getFspById_withValidScope_returns200() throws Exception {
    when(fspService.getById(1L)).thenReturn(fspRequest);

    mockMvc.perform(get("/api/v1/fsp/1")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fspId").value(1))
        .andExpect(jsonPath("$.clientNumber").value("00123456"));
  }

  @Test
  void getFspById_withoutToken_returns401() throws Exception {
    mockMvc.perform(get("/api/v1/fsp/1"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void getFspById_withWrongScope_returns403() throws Exception {
    mockMvc.perform(get("/api/v1/fsp/1")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:write"))))
        .andExpect(status().isForbidden());
  }

  @Test
  void searchFsp_withValidScope_returns200WithResults() throws Exception {
    when(fspService.search(any(FspSearchRequest.class))).thenReturn(List.of(searchResult));

    mockMvc.perform(get("/api/v1/fsp/search")
            .param("clientNumber", "00123456")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fspId").value(1))
        .andExpect(jsonPath("$[0].clientNumber").value("00123456"));
  }

  @Test
  void updateFsp_withWriteScope_returns200() throws Exception {
    when(fspService.update(eq(1L), any(FspRequest.class))).thenReturn(fspRequest);

    mockMvc.perform(put("/api/v1/fsp/1")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:write")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fspRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fspId").value(1));
  }

  @Test
  void updateFsp_withReadScopeOnly_returns403() throws Exception {
    mockMvc.perform(put("/api/v1/fsp/1")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fspRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  void getWorkflow_returns200WithWorkflowHistory() throws Exception {
    when(workflowService.getWorkflow(1L)).thenReturn(List.of(workflowResponse));

    mockMvc.perform(get("/api/v1/fsp/1/workflow")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fspWorkflowId").value(10))
        .andExpect(jsonPath("$[0].workflowStatusCode").value("SUBMITTED"));
  }

  @Test
  void submitWorkflowAction_withWriteScope_returns200() throws Exception {
    WorkflowRequest request = new WorkflowRequest();
    request.setAction("SUBMIT");
    request.setComments("Ready for review");

    mockMvc.perform(post("/api/v1/fsp/1/workflow/action")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:write")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(workflowService).submitAction(eq(1L), any(WorkflowRequest.class));
  }

  @Test
  void getStandards_returns200() throws Exception {
    StandardRequest std = new StandardRequest();
    std.setFspStockingStandardId(5L);
    std.setBgcZoneCode("SBS");
    std.setSpeciesCode1("PL");
    when(standardsService.getByFspId(1L)).thenReturn(List.of(std));

    mockMvc.perform(get("/api/v1/fsp/1/standards")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].bgcZoneCode").value("SBS"))
        .andExpect(jsonPath("$[0].speciesCode1").value("PL"));
  }

  @Test
  void deleteStandard_withWriteScope_returns204() throws Exception {
    mockMvc.perform(delete("/api/v1/fsp/1/standards/5")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:write"))))
        .andExpect(status().isNoContent());

    verify(standardsService).delete(5L);
  }

  @Test
  void getAttachments_returns200WithMetadataOnly() throws Exception {
    AttachmentResponse response = AttachmentResponse.builder()
        .fspAttachmentId(20L).fspId(1L).fileName("test-map.pdf").fileSize(1024L).build();
    when(attachmentsService.getByFspId(1L)).thenReturn(List.of(response));

    mockMvc.perform(get("/api/v1/fsp/1/attachments")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fspAttachmentId").value(20))
        .andExpect(jsonPath("$[0].fileName").value("test-map.pdf"))
        .andExpect(jsonPath("$[0].fileContent").doesNotExist());
  }

  @Test
  void uploadAttachment_withWriteScope_returns200() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file", "test-map.pdf", "application/pdf", new byte[]{1, 2, 3}
    );
    AttachmentResponse response = AttachmentResponse.builder()
        .fspAttachmentId(20L).fileName("test-map.pdf").build();
    when(attachmentsService.upload(eq(1L), any(), eq("MAP"))).thenReturn(response);

    mockMvc.perform(multipart("/api/v1/fsp/1/attachments")
            .file(file)
            .param("typeCode", "MAP")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:write"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fspAttachmentId").value(20));
  }

  @Test
  void deleteAttachment_withWriteScope_returns204() throws Exception {
    mockMvc.perform(delete("/api/v1/fsp/1/attachments/20")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:write"))))
        .andExpect(status().isNoContent());

    verify(attachmentsService).delete(1L, 20L);
  }

  @Test
  void getInbox_returns200() throws Exception {
    when(inboxService.getInboxForCurrentUser()).thenReturn(List.of(searchResult));

    mockMvc.perform(get("/api/v1/fsp/inbox")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fspId").value(1));
  }

  @Test
  void getHistory_returns200() throws Exception {
    when(historyService.getHistory(1L)).thenReturn(List.of(workflowResponse));

    mockMvc.perform(get("/api/v1/fsp/1/history")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_fsp:read"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].actionUserid").value("TESTUSER"));
  }
}
