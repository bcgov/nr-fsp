package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspHistoryRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

  private final FspHistoryRepository historyRepository;
  private final FspMapper fspMapper;

  public List<WorkflowResponse> getHistory(Long fspId) {
    return historyRepository.findByFspIdOrderByActionTimestampDesc(fspId)
        .stream()
        .map(h -> WorkflowResponse.builder()
            .fspId(h.getFspId())
            .workflowActionCode(h.getActionCode())
            .actionUserid(h.getActionUserid())
            .actionTimestamp(h.getActionTimestamp())
            .comments(h.getComments())
            .build()
        ).toList();
  }
}