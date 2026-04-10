package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.FspStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.model.v1.FspWorkflow;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspWorkflowRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

  @Mock FspWorkflowRepository workflowRepository;
  @Mock FspStoredProcedureDao storedProcedureDao;
  @Mock FspMapper fspMapper;

  @InjectMocks WorkflowService workflowService;

  private FspWorkflow workflow;
  private WorkflowResponse workflowResponse;

  @BeforeEach
  void setUp() {
    workflow = new FspWorkflow();
    workflow.setFspWorkflowId(10L);
    workflow.setFspId(1L);
    workflow.setWorkflowStatusCode("SUBMITTED");
    workflow.setWorkflowActionCode("SUBMIT");
    workflow.setActionUserid("TESTUSER");
    workflow.setActionTimestamp(LocalDateTime.now());

    workflowResponse = WorkflowResponse.builder()
        .fspWorkflowId(10L)
        .fspId(1L)
        .workflowStatusCode("SUBMITTED")
        .workflowActionCode("SUBMIT")
        .actionUserid("TESTUSER")
        .build();
  }

  @Test
  void getWorkflow_returnsMappedResponses() {
    when(workflowRepository.findByFspIdOrderByActionTimestampDesc(1L))
        .thenReturn(List.of(workflow));
    when(fspMapper.toWorkflowResponseList(List.of(workflow)))
        .thenReturn(List.of(workflowResponse));

    List<WorkflowResponse> result = workflowService.getWorkflow(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFspWorkflowId()).isEqualTo(10L);
    assertThat(result.get(0).getWorkflowStatusCode()).isEqualTo("SUBMITTED");
  }

  @Test
  void getWorkflow_whenNoHistory_returnsEmptyList() {
    when(workflowRepository.findByFspIdOrderByActionTimestampDesc(1L)).thenReturn(List.of());
    when(fspMapper.toWorkflowResponseList(List.of())).thenReturn(List.of());

    assertThat(workflowService.getWorkflow(1L)).isEmpty();
  }

  @Test
  void submitAction_callsStoredProcedureWithCurrentUser() {
    WorkflowRequest request = new WorkflowRequest();
    request.setAction("SUBMIT");
    request.setComments("Submitting for review");

    try (MockedStatic<RequestUtil> requestUtil = mockStatic(RequestUtil.class)) {
      requestUtil.when(RequestUtil::getCurrentUserName).thenReturn("TESTUSER");
      workflowService.submitAction(1L, request);
      verify(storedProcedureDao).fspSubmit(1L, "TESTUSER", "SUBMIT", "Submitting for review");
    }
  }

  @Test
  void submitAction_withNullComments_passesThroughToDao() {
    WorkflowRequest request = new WorkflowRequest();
    request.setAction("APPROVE");
    request.setComments(null);

    try (MockedStatic<RequestUtil> requestUtil = mockStatic(RequestUtil.class)) {
      requestUtil.when(RequestUtil::getCurrentUserName).thenReturn("APPROVER");
      workflowService.submitAction(1L, request);
      verify(storedProcedureDao).fspSubmit(1L, "APPROVER", "APPROVE", null);
    }
  }
}
