package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors a row from the FSP_800_HISTORY P_FSP_SEARCH_RESULTS cursor. Used for
 * both /workflow and /history endpoints — the workflow endpoint surfaces the
 * audit trail of a single FSP, which the legacy app drives off the same cursor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowResponse {

  private String amendmentNumber;
  private String extensionNumber;
  private String eventDateTime;
  private String userId;
  private String event;
  private String description;
  private String approvalRequestIndicator;
  private String submissionId;
}
