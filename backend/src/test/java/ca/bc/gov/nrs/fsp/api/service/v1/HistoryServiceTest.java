package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspHistory;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspHistoryRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

  @Mock FspHistoryRepository historyRepository;
  @Mock FspMapper fspMapper;

  @InjectMocks HistoryService historyService;

  private FspHistory historyEntry;

  @BeforeEach
  void setUp() {
    historyEntry = new FspHistory();
    historyEntry.setFspHistoryId(30L);
    historyEntry.setFspId(1L);
    historyEntry.setActionCode("SUBMIT");
    historyEntry.setActionUserid("TESTUSER");
    historyEntry.setActionTimestamp(LocalDateTime.of(2024, 6, 1, 10, 0));
    historyEntry.setComments("Submitted for district review");
  }

  @Test
  void getHistory_returnsMappedHistoryInDescendingOrder() {
    when(historyRepository.findByFspIdOrderByActionTimestampDesc(1L))
        .thenReturn(List.of(historyEntry));

    List<WorkflowResponse> result = historyService.getHistory(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFspId()).isEqualTo(1L);
    assertThat(result.get(0).getWorkflowActionCode()).isEqualTo("SUBMIT");
    assertThat(result.get(0).getActionUserid()).isEqualTo("TESTUSER");
    assertThat(result.get(0).getComments()).isEqualTo("Submitted for district review");
  }

  @Test
  void getHistory_whenNoHistory_returnsEmptyList() {
    when(historyRepository.findByFspIdOrderByActionTimestampDesc(1L)).thenReturn(List.of());
    assertThat(historyService.getHistory(1L)).isEmpty();
  }
}
