package ca.bc.gov.nrs.fsp.api.dao.v1;

/**
 * Wraps Oracle package FSP_550_SUB_LAYERS (legacy: pkgdefinitions/Fsp550SubLayers.java).
 * No REF CURSOR — only INOUT VARCHARs.
 */
public interface Fsp550SubLayersDao {

  String PACKAGE_NAME = "FSP_550_SUB_LAYERS";
  String PROCEDURE_NAME = "MAINLINE";

  record Result(
      String pAction,
      String pStandardsRegimeId,
      String pStandardsRegimeLayerId,
      String pStockingLayerCode,
      String pTargetStocking,
      String pMinHorizontalDistance,
      String pMinPrefStockingStandard,
      String pMinStockingStandard,
      String pResidualBasalArea,
      String pMinPostSpacing,
      String pMaxPostSpacing,
      String pMaxConifer,
      String pHghtRelativeToComp,
      String pTreeSizeUnitCode,
      String pUpdateUserid,
      String pRevisionCount,
      String pStandardsRevisionCount,
      String pErrorMessage
  ) {}

  Result mainline(
      String pAction,
      String pStandardsRegimeId,
      String pStandardsRegimeLayerId,
      String pStockingLayerCode,
      String pTargetStocking,
      String pMinHorizontalDistance,
      String pMinPrefStockingStandard,
      String pMinStockingStandard,
      String pResidualBasalArea,
      String pMinPostSpacing,
      String pMaxPostSpacing,
      String pMaxConifer,
      String pHghtRelativeToComp,
      String pTreeSizeUnitCode,
      String pUpdateUserid,
      String pRevisionCount,
      String pStandardsRevisionCount
  );
}
