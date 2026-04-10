package ca.bc.gov.nrs.fsp.api.model.v1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FSP_ATTACHMENT")
public class FspAttachment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_att_seq")
  @SequenceGenerator(name = "fsp_att_seq", sequenceName = "FSP_ATTACHMENT_SEQ", allocationSize = 1)
  @Column(name = "FSP_ATTACHMENT_ID")
  private Long fspAttachmentId;

  @Column(name = "FSP_ID", nullable = false)
  private Long fspId;

  @Column(name = "ATTACHMENT_TYPE_CODE")
  private String attachmentTypeCode;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "FILE_SIZE")
  private Long fileSize;

  @Lob
  @Column(name = "FILE_CONTENT")
  private byte[] fileContent;

  @Column(name = "ENTRY_USERID")
  private String entryUserid;

  @Column(name = "ENTRY_TIMESTAMP")
  private LocalDateTime entryTimestamp;
}