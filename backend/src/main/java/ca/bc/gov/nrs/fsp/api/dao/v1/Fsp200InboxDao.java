package ca.bc.gov.nrs.fsp.api.dao.v1;

import java.util.List;

/**
 * Wraps Oracle package fsp_200_inbox (legacy: pkgdefinitions/Fsp200Inbox.java).
 * Note the package name is lowercase in the legacy descriptor — preserved here.
 */
public interface Fsp200InboxDao {

  String PACKAGE_NAME = "fsp_200_inbox";
  String PROCEDURE_NAME = "MAINLINE";

  record Header(
      String pAction,
      String pOrgUnitNo,
      String pFspId,
      String pFspPlanName,
      String pFspStatusCode,
      String pAhClientNumber,
      String pErrorMessage
  ) {}

  /** P_FSP_SEARCH_RESULT REF CURSOR row. Column 9 is numeric (legacy: rs.getInt(9)). */
  record Row(
      String fspId,
      String planName,
      String fspAmendmentNumber,
      String extensionNumber,
      String updateUserid,
      String fspStatusDesc,
      String planSubmissionDate,
      String agreementHolder,
      Integer numberOfFdu
  ) {}

  record Result(Header header, List<Row> rows) {}

  Result mainline(
      String pAction,
      String pOrgUnitNo,
      String pFspId,
      String pFspPlanName,
      String pFspStatusCode,
      String pAhClientNumber
  );
}
