package ca.bc.gov.nrs.fsp.api.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors the union of cursor columns returned by FSP_100_SEARCH and fsp_200_inbox.
 * All values are VARCHAR in the source procedures (Oracle) — IDs stay as String here
 * to avoid lossy String→Long conversion. numberOfFdu is the only numeric column,
 * read via rs.getInt(9) in the legacy Fsp200InboxBeanJDBCAdaptor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FspSearchResult {

  private String fspId;
  private String planName;
  private String fspAmendmentName;          // FSP_100 only
  private String fspAmendmentNumber;
  private String orgUnitCode;               // FSP_100 only
  private String planStartDate;             // FSP_100 only
  private String planEndDate;               // FSP_100 only
  private String planSubmissionDate;        // FSP_200 only
  private String agreementHolder;
  private String amendmentApprovalRequirdInd;  // FSP_100 only
  private String extensionNumber;           // FSP_200 only
  private String updateUserid;              // FSP_200 only
  private String fspStatusDesc;
  private Integer numberOfFdu;              // FSP_200 only
}
