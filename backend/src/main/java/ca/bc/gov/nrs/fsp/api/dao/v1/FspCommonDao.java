package ca.bc.gov.nrs.fsp.api.dao.v1;

/**
 * Wraps Oracle package fsp_common (legacy: pkgdefinitions/FspErrorMessageFunctions.java).
 */
public interface FspCommonDao {

  String PACKAGE_NAME = "fsp_common";

  /** fsp_common.get_error_message(p_error_key IN VARCHAR2) RETURN VARCHAR2. */
  String getErrorMessage(String pErrorKey);
}
