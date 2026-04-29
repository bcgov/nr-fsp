package ca.bc.gov.nrs.fsp.api.struct.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Subset of FSP_700_WORKFLOW.MAINLINE inputs needed to submit a workflow action.
 * The full proc has 70 INOUT params — only the dispatch+comment fields are
 * exposed here. Other slots default to empty in the service layer.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkflowRequest extends BaseRequest {

  /** P_ACTION — e.g. "SUBMIT", "APPROVE", "REJECT". */
  @NotBlank(message = "Action is required")
  @Size(max = 20)
  private String action;

  /** P_REVIEW_COMMENT. */
  @Size(max = 2000)
  private String comments;
}
