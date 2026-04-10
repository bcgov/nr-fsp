package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowResponse {

  private Long fspWorkflowId;
  private Long fspId;
  private String workflowStatusCode;
  private String workflowActionCode;
  private String comments;
  private String actionUserid;
  private LocalDateTime actionTimestamp;
}