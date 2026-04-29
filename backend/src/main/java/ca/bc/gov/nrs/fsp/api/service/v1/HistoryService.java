package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp800HistoryDao;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wraps FSP_800_HISTORY.MAINLINE for the audit-trail endpoint.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

  private static final String ACTION_FETCH = "FETCH";

  private final Fsp800HistoryDao historyDao;

  public List<WorkflowResponse> getHistory(String fspId) {
    Fsp800HistoryDao.Result result = historyDao.mainline(
        ACTION_FETCH, fspId, "", "", null,
        "", "", "", "", "", "", "", "", "", "", "");
    return result.rows().stream().map(row -> WorkflowResponse.builder()
        .amendmentNumber(row.amendmentNumber())
        .extensionNumber(row.extensionNumber())
        .eventDateTime(row.eventDateTime())
        .userId(row.userId())
        .event(row.event())
        .description(row.description())
        .approvalRequestIndicator(row.approvalRequestIndicator())
        .submissionId(row.submissionId())
        .build()).toList();
  }
}
