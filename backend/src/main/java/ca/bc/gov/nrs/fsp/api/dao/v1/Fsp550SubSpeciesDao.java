package ca.bc.gov.nrs.fsp.api.dao.v1;

import java.util.List;

/**
 * Wraps Oracle package FSP_550_SUB_SPECIES (legacy: pkgdefinitions/Fsp550SubSpecies.java).
 */
public interface Fsp550SubSpeciesDao {

  String PACKAGE_NAME = "FSP_550_SUB_SPECIES";
  String PROCEDURE_NAME = "MAINLINE";

  record Header(
      String pAction,
      String pStandardsRegimeLayerId,
      String pSilvTreeSpeciesCodeNew,
      String pSilvTreeSpeciesCodeOld,
      String pMinHeight,
      String pPreferredInd,
      String pUpdateUserid,
      String pRevisionCount,
      String pStandardsRegimeId,
      String pStandardsRevisionCount,
      String pErrorMessage
  ) {}

  /** P_RESULTS REF CURSOR row, read by column name in legacy adaptor. */
  record Row(
      String silvTreeSpeciesCodeNew,
      String silvTreeSpeciesCodeOld,
      String speciesDescription,
      String minHeight,
      String revisionCount
  ) {}

  record Result(Header header, List<Row> rows) {}

  Result mainline(
      String pAction,
      String pStandardsRegimeLayerId,
      String pSilvTreeSpeciesCodeNew,
      String pSilvTreeSpeciesCodeOld,
      String pMinHeight,
      String pPreferredInd,
      String pUpdateUserid,
      String pRevisionCount,
      String pStandardsRegimeId,
      String pStandardsRevisionCount
  );
}
