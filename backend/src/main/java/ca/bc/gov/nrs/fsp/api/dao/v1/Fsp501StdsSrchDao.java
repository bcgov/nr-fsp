package ca.bc.gov.nrs.fsp.api.dao.v1;

import java.util.List;

/**
 * Wraps Oracle package FSP_501_STDS_SRCH (legacy: pkgdefinitions/Fsp501StdsSrch.java).
 * Note: in the legacy descriptor P_ERROR_MESSAGE is at position 22 and P_RESULTS is at 23.
 */
public interface Fsp501StdsSrchDao {

  String PACKAGE_NAME = "FSP_501_STDS_SRCH";
  String PROCEDURE_NAME = "MAINLINE";

  record Header(
      String pAction,
      String pDefaultStandard,
      String pStandardsRegimeStatusCode,
      String pOrgUnitNo,
      String pFspId,
      String pClientNumber,
      String pClientName,
      String pStandardsRegimeId,
      String pStandardsRegimeName,
      String pStandardsObjective,
      String pPreferredSpecies,
      String pAcceptableSpecies,
      String pExpiryDtFrom,
      String pExpiryDtTo,
      String pBgcZoneCode,
      String pBgcSubzoneCode,
      String pBgcVariant,
      String pBgcPhase,
      String pBecSiteSeriesCd,
      String pBecSiteSeriesPhaseCd,
      String pBecSeral,
      String pErrorMessage
  ) {}

  /** P_RESULTS REF CURSOR row, read by column name in legacy adaptor. */
  record Row(
      String standardsRegimeId,
      String standardsRegimeName,
      String standardsObjective,
      String bgc,
      String clientNumber,
      String description,
      String expiryDate,
      String fspIdList
  ) {}

  record Result(Header header, List<Row> rows) {}

  Result mainline(
      String pAction,
      String pDefaultStandard,
      String pStandardsRegimeStatusCode,
      String pOrgUnitNo,
      String pFspId,
      String pClientNumber,
      String pClientName,
      String pStandardsRegimeId,
      String pStandardsRegimeName,
      String pStandardsObjective,
      String pPreferredSpecies,
      String pAcceptableSpecies,
      String pExpiryDtFrom,
      String pExpiryDtTo,
      String pBgcZoneCode,
      String pBgcSubzoneCode,
      String pBgcVariant,
      String pBgcPhase,
      String pBecSiteSeriesCd,
      String pBecSiteSeriesPhaseCd,
      String pBecSeral
  );
}
