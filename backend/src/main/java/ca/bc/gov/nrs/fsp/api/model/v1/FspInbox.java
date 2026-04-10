package ca.bc.gov.nrs.fsp.api.model.v1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "FSP_INBOX_V")  // likely a view in the legacy app
public class FspInbox {

  @Id
  @Column(name = "FSP_ID")
  private Long fspId;

  @Column(name = "CLIENT_NUMBER")
  private String clientNumber;

  @Column(name = "CLIENT_NAME")
  private String clientName;

  @Column(name = "FSP_STATUS_CODE")
  private String fspStatusCode;

  @Column(name = "ORG_UNIT_NO")
  private Long orgUnitNo;

  @Column(name = "DISTRICT_NAME")
  private String districtName;

  @Column(name = "FSP_END_DATE")
  private LocalDate fspEndDate;

  @Column(name = "DAYS_UNTIL_EXPIRY")
  private Integer daysUntilExpiry;
}
