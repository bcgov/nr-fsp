package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Search criteria for FSP_100_SEARCH.MAINLINE. Field names mirror the P_*
 * parameters of the legacy package (lowercased).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FspSearchRequest {

  private String fspId;
  private String fspPlanName;
  private String orgUnitNo;
  private String ahClientNumber;
  private String fspAmendmentName;
  private String fspDateStart;
  private String fspDateEnd;
  private String fspDateType;
  private String fspStatusCode;
  private String approvalRequired;
}
