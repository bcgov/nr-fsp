package ca.bc.gov.nrs.fsp.api.dao.v1;

import java.util.List;

/**
 * Wraps Oracle package FSP_400_ATTACHMENTS (legacy: data/manager/Fsp400AttachmentsDataManager.java).
 * Multiple procedures: GET (lists 9 attachment categories), GET_ATTACH_BLOB (download),
 * CREATE_ATTACHMENT (metadata insert with empty BLOB OUT), SAVE_ATTACHMENT_CONTENT (BLOB write),
 * SAVE_REFERENCE (link existing attachment), REMOVE_ATTACHMENT (delete).
 *
 * GET parameter positions match the legacy data manager exactly. The legacy passes IN
 * values only at positions 1, 2, 7, 8, 9, 10, 16; the rest are registered OUT-only.
 */
public interface Fsp400AttachmentsDao {

  String PACKAGE_NAME = "FSP_400_ATTACHMENTS";

  /** Single attachment row from any of the GET cursors. */
  record AttachmentRow(
      String fspAttachmentId,
      String fspAmendmentNumber,
      String attachmentName,
      String attachmentDescription,
      String attachmentSize,
      String consolidatedInd
  ) {}

  /** Result of FSP_400_ATTACHMENTS.GET — 9 distinct cursors plus error. */
  record GetResult(
      List<AttachmentRow> legalDocs,
      List<AttachmentRow> amendDesc,
      List<AttachmentRow> stockStandards,
      List<AttachmentRow> fduMap,
      List<AttachmentRow> identAreas1961,
      List<AttachmentRow> identAreas1962,
      List<AttachmentRow> declaredAreas,
      List<AttachmentRow> supportingDocs,
      List<AttachmentRow> ddmDecision,
      String pErrorMessage
  ) {}

  /** Result of FSP_400_ATTACHMENTS.GET_ATTACH_BLOB. */
  record AttachBlob(String fileName, byte[] content, String pErrorMessage) {}

  /** Result of FSP_400_ATTACHMENTS.CREATE_ATTACHMENT. */
  record CreateAttachmentResult(Long createdAttachmentId, String pErrorMessage) {}

  /**
   * FSP_400_ATTACHMENTS.GET — only the IN-set positions are exposed as method
   * parameters; other INOUT slots are registered OUT-only as the legacy does.
   */
  GetResult get(
      String pFspId,
      String pNewFspId,
      String pFspAmendmentNumber,
      String pNewFspAmendmentNumber,
      String pUserClientNumber,
      String pUserRole,
      String pAllAttachesInd
  );

  AttachBlob getAttachBlob(Long pAttachmentId);

  CreateAttachmentResult createAttachment(
      Long pFspId,
      String pFspAmendmentNumber,
      String attachmentType,
      String filename,
      Long fileSize,
      String description,
      String consolidatedInd,
      String userId
  );

  String saveAttachmentContent(Long attachmentId, byte[] content);

  String saveReference(Long fspId, String amendmentNumber, Long attachmentId, String userId);

  String removeAttachment(Long fspId, String amendmentNumber, Long attachmentId);
}
