package ca.bc.gov.nrs.fsp.api.dao.v1;

import java.util.List;
import java.util.Map;

/**
 * Wraps Oracle package FSP_CODE_LISTS (legacy: pkgdefinitions/PkgFspCodeLists.java).
 * Each method calls a different procedure that returns a single REF CURSOR.
 * Cursor row column names vary per lookup, so we return generic Map rows.
 */
public interface FspCodeListsDao {

  String PACKAGE_NAME = "FSP_CODE_LISTS";

  List<Map<String, Object>> getFspAmendmentNumbers(String pFspId);

  List<Map<String, Object>> getAttachReferenceList(String pFspId);

  List<Map<String, Object>> getOrgUnitFiltered(String pOrgUnitFilter);

  List<Map<String, Object>> getDistrictNo();

  List<Map<String, Object>> getTreeSizeUnitCd();

  List<Map<String, Object>> getSilvTreeSpCd();

  List<Map<String, Object>> getStatuteCd();

  List<Map<String, Object>> getFspStatusCode();

  List<Map<String, Object>> getStdRegimeStatusCode();
}
