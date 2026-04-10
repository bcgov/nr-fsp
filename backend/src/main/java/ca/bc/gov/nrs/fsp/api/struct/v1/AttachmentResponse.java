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
public class AttachmentResponse {

  private Long fspAttachmentId;
  private Long fspId;
  private String attachmentTypeCode;
  private String fileName;
  private Long fileSize;
  private String entryUserid;
  private LocalDateTime entryTimestamp;
  // fileContent intentionally excluded — use the /download endpoint
}