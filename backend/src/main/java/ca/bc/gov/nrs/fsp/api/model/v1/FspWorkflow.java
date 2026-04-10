package ca.bc.gov.nrs.fsp.api.model.v1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FSP_WORKFLOW")
public class FspWorkflow {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_workflow_seq")
  @SequenceGenerator(name = "fsp_workflow_seq", sequenceName = "FSP_WORKFLOW_SEQ", allocationSize = 1)
  @Column(name = "FSP_WORKFLOW_ID")
  private Long fspWorkflowId;

  @Column(name = "FSP_ID", nullable = false)
  private Long fspId;

  @Column(name = "WORKFLOW_STATUS_CODE", nullable = false)
  private String workflowStatusCode;

  @Column(name = "WORKFLOW_ACTION_CODE")
  private String workflowActionCode;

  @Column(name = "COMMENTS", length = 2000)
  private String comments;

  @Column(name = "ACTION_USERID")
  private String actionUserid;

  @Column(name = "ACTION_TIMESTAMP")
  private LocalDateTime actionTimestamp;
}