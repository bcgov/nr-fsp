package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Mirrors the row shape of FSP_500_STOCKING_STANDARDS P_FSP_SS_RESULTS cursor.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StandardRequest extends BaseRequest {

  private String standardsRegimeId;
  private String standardsRegimeName;
  private String standardsObjective;
  private String standardsAmndNumber;
  private String standardsBgc;
  private String standardsRegimeStatus;
  private String standardsEffectiveDate;
  private String defaultStandardInd;
}
