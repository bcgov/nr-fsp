package ca.bc.gov.nrs.fsp.api.dao.v1;

import java.util.List;

/**
 * Wraps Oracle package FSP_901_NOTIFICATION_BATCH (legacy: batch/DBProcTask.java).
 * GET fetches outstanding notifications; CHANGE marks one done.
 */
public interface Fsp901NotificationBatchDao {

  String PACKAGE_NAME = "FSP_901_NOTIFICATION_BATCH";

  /** GET cursor row — 17 columns (1-indexed in legacy DBProcTask.getOutputBean). */
  record NotificationRow(
      String designateIdir,
      String fspId,
      String fspAmendmentNumber,
      String extensionId,
      String statusCode,
      String entryUserid,
      String entryTimestamp,
      String fduUpdateInd,
      String identifiedAreasUpdateInd,
      String stockingStandardUpdateInd,
      String fduAttachments,
      String iaAttachments,
      String fduSpatialChanged,
      String identifiedSpatialChanged,
      String stockingStandardsChanged,
      String amendmentApprovalRequirdInd,
      String maxStatusId
  ) {}

  record GetResult(String errorMessage, List<NotificationRow> rows) {}

  /** GET(?, ?) — OUT VARCHAR error, OUT CURSOR rows. */
  GetResult get();

  /** CHANGE(?, ?, ?) — IN fspId, IN fspAmendmentNumber, IN maxStatusId. */
  void change(String fspId, String fspAmendmentNumber, String maxStatusId);
}
