package ca.bc.gov.nrs.fsp.api.model.v1;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "FSP_STOCKING_STANDARD")
public class FspStockingStandard {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_std_seq")
  @SequenceGenerator(name = "fsp_std_seq", sequenceName = "FSP_STOCKING_STANDARD_SEQ", allocationSize = 1)
  @Column(name = "FSP_STOCKING_STANDARD_ID")
  private Long fspStockingStandardId;

  @Column(name = "FSP_ID", nullable = false)
  private Long fspId;

  @Column(name = "BGC_ZONE_CODE")
  private String bgcZoneCode;

  @Column(name = "BGC_SUBZONE_CODE")
  private String bgcSubzoneCode;

  @Column(name = "SPECIES_CD_1")
  private String speciesCode1;

  @Column(name = "SPECIES_PCT_1")
  private Integer speciesPct1;

  @Column(name = "SPECIES_CD_2")
  private String speciesCode2;

  @Column(name = "SPECIES_PCT_2")
  private Integer speciesPct2;

  @Column(name = "MIN_WELL_SPACED_TREES")
  private Integer minWellSpacedTrees;

  @Column(name = "PREFERRED_WELL_SPACED_TREES")
  private Integer preferredWellSpacedTrees;

  @Column(name = "EARLIEST_FREE_GROWING_DATE")
  private Integer earliestFreeGrowingDate;

  @Column(name = "LATEST_FREE_GROWING_DATE")
  private Integer latestFreeGrowingDate;

  @Column(name = "DEFAULT_IND")
  private String defaultInd;
}