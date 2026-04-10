package ca.bc.gov.nrs.fsp.api.struct.v1;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StandardRequest extends BaseRequest {

  private Long fspStockingStandardId;

  @Size(max = 4)
  private String bgcZoneCode;

  @Size(max = 4)
  private String bgcSubzoneCode;

  @Size(max = 3)
  private String speciesCode1;

  @Min(0) @Max(100)
  private Integer speciesPct1;

  @Size(max = 3)
  private String speciesCode2;

  @Min(0) @Max(100)
  private Integer speciesPct2;

  private Integer minWellSpacedTrees;
  private Integer preferredWellSpacedTrees;
  private Integer earliestFreeGrowingDate;
  private Integer latestFreeGrowingDate;

  @Size(max = 1)
  private String defaultInd;
}