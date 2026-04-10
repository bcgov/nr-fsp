package ca.bc.gov.nrs.fsp.api.model.v1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FSP")
public class Fsp {

  @Id
  @Column(name = "FSP_ID")
  private Long fspId;

  @Column(name = "CLIENT_NUMBER", nullable = false)
  private String clientNumber;

  @Column(name = "CLIENT_NAME")
  private String clientName;

  @Column(name = "FSP_STATUS_CODE", nullable = false)
  private String fspStatusCode;

  @Column(name = "ORG_UNIT_NO")
  private Long orgUnitNo;

  @Column(name = "FSP_START_DATE")
  private LocalDate fspStartDate;

  @Column(name = "FSP_END_DATE")
  private LocalDate fspEndDate;

  @Column(name = "AMENDMENT_NUMBER")
  private Integer amendmentNumber;

  @Column(name = "ENTRY_USERID")
  private String entryUserid;

  @Column(name = "ENTRY_TIMESTAMP")
  private LocalDateTime entryTimestamp;

  @Column(name = "UPDATE_USERID")
  private String updateUserid;

  @Column(name = "UPDATE_TIMESTAMP")
  private LocalDateTime updateTimestamp;
}