package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.FspStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspWorkflowRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowService {

  private final FspWorkflowRepository workflowRepository;
  private final FspStoredProcedureDao storedProcedureDao;
  private final FspMapper fspMapper;

  public List<WorkflowResponse> getWorkflow(Long fspId) {
    return fspMapper.toWorkflowResponseList(
        workflowRepository.findByFspIdOrderByActionTimestampDesc(fspId)
    );
  }

  @Transactional
  public void submitAction(Long fspId, WorkflowRequest request) {
    String userId = RequestUtil.getCurrentUserName();
    storedProcedureDao.fspSubmit(fspId, userId, request.getAction(), request.getComments());
  }
}