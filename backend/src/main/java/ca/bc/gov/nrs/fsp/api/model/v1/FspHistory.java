package ca.bc.gov.nrs.fsp.api.model.v1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FSP_HISTORY")
public class FspHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_hist_seq")
  @SequenceGenerator(name = "fsp_hist_seq", sequenceName = "FSP_HISTORY_SEQ", allocationSize = 1)
  @Column(name = "FSP_HISTORY_ID")
  private Long fspHistoryId;

  @Column(name = "FSP_ID", nullable = false)
  private Long fspId;

  @Column(name = "ACTION_CODE")
  private String actionCode;

  @Column(name = "ACTION_USERID")
  private String actionUserid;

  @Column(name = "ACTION_TIMESTAMP")
  private LocalDateTime actionTimestamp;

  @Column(name = "COMMENTS", length = 2000)
  private String comments;
}