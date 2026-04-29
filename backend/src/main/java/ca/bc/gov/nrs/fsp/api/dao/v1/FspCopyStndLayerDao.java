package ca.bc.gov.nrs.fsp.api.dao.v1;

/**
 * Wraps Oracle package fsp_copy_stnd_layer (legacy: pkgdefinitions/FspCopyStndLayer.java).
 * Package name lowercase per legacy descriptor.
 */
public interface FspCopyStndLayerDao {

  String PACKAGE_NAME = "fsp_copy_stnd_layer";
  String PROCEDURE_NAME = "MAINLINE";

  record Result(
      String pAction,
      String pStockingStandardUnitId,
      String pStockingLayerId,
      String pStandardsRegimeLayerId,
      String pStandardsRegimeId,
      String pStockingLayerList,
      String pUpdateUserid,
      String pErrorMessage
  ) {}

  Result mainline(
      String pAction,
      String pStockingStandardUnitId,
      String pStockingLayerId,
      String pStandardsRegimeLayerId,
      String pStandardsRegimeId,
      String pStockingLayerList,
      String pUpdateUserid
  );
}
