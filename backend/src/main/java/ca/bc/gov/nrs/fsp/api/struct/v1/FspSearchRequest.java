package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FspSearchRequest {

  private String clientNumber;
  private Long orgUnitNo;
  private String statusCode;
  private LocalDate startDateFrom;
  private LocalDate startDateTo;
}