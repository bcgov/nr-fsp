package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FspSearchResult {

  private Long fspId;
  private String clientNumber;
  private String clientName;
  private String fspStatusCode;
  private Long orgUnitNo;
  private String districtName;
  private LocalDate fspEndDate;
  private Integer amendmentNumber;
}