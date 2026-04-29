package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors a row of any FSP_400_ATTACHMENTS.GET cursor (legalDocs, supportingDocs,
 * etc.). All fields are VARCHAR in the legacy proc; numeric-looking IDs are kept
 * as String to avoid lossy conversion at this layer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse {

  private String fspAttachmentId;
  private String fspAmendmentNumber;
  private String attachmentName;
  private String attachmentDescription;
  private String attachmentSize;
  private String consolidatedInd;
}
