package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp400AttachmentsDao;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentBlob;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentResponse;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the FSP_400_ATTACHMENTS package. The legacy GET proc returns 9 distinct
 * attachment-category cursors; this list endpoint flattens them into a single
 * list as the simplest preservation of "all attachments for an FSP".
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentsService {

  private static final String ALL_ATTACHES_IND = "Y";
  private static final String DEFAULT_AMENDMENT_NUMBER = "1";
  private static final String DEFAULT_CONSOLIDATED_IND = "N";

  private final Fsp400AttachmentsDao attachmentsDao;

  public List<AttachmentResponse> getByFspId(String fspId) {
    Fsp400AttachmentsDao.GetResult get = attachmentsDao.get(
        fspId,
        "",                           // p_new_fsp_id
        DEFAULT_AMENDMENT_NUMBER,
        "",                           // p_new_fsp_amendment_number
        "",                           // p_user_client_number
        "",                           // p_user_role
        ALL_ATTACHES_IND);
    List<AttachmentResponse> all = new ArrayList<>();
    appendAll(all, get.legalDocs());
    appendAll(all, get.amendDesc());
    appendAll(all, get.stockStandards());
    appendAll(all, get.fduMap());
    appendAll(all, get.identAreas1961());
    appendAll(all, get.identAreas1962());
    appendAll(all, get.declaredAreas());
    appendAll(all, get.supportingDocs());
    appendAll(all, get.ddmDecision());
    return all;
  }

  public AttachmentBlob getAttachment(String fspId, Long attachmentId) {
    Fsp400AttachmentsDao.AttachBlob blob = attachmentsDao.getAttachBlob(attachmentId);
    return new AttachmentBlob(blob.fileName(), blob.content());
  }

  @Transactional
  public AttachmentResponse upload(String fspId, MultipartFile file, String typeCode)
      throws IOException {
    String userId = RequestUtil.getCurrentUserName();
    Fsp400AttachmentsDao.CreateAttachmentResult created = attachmentsDao.createAttachment(
        Long.valueOf(fspId),
        DEFAULT_AMENDMENT_NUMBER,
        typeCode,
        file.getOriginalFilename(),
        file.getSize(),
        "",   // description
        DEFAULT_CONSOLIDATED_IND,
        userId);
    attachmentsDao.saveAttachmentContent(created.createdAttachmentId(), file.getBytes());
    return AttachmentResponse.builder()
        .fspAttachmentId(String.valueOf(created.createdAttachmentId()))
        .fspAmendmentNumber(DEFAULT_AMENDMENT_NUMBER)
        .attachmentName(file.getOriginalFilename())
        .attachmentSize(String.valueOf(file.getSize()))
        .consolidatedInd(DEFAULT_CONSOLIDATED_IND)
        .build();
  }

  @Transactional
  public void delete(String fspId, Long attachmentId) {
    attachmentsDao.removeAttachment(Long.valueOf(fspId), DEFAULT_AMENDMENT_NUMBER, attachmentId);
  }

  private static void appendAll(List<AttachmentResponse> sink, List<Fsp400AttachmentsDao.AttachmentRow> rows) {
    for (Fsp400AttachmentsDao.AttachmentRow row : rows) {
      sink.add(AttachmentResponse.builder()
          .fspAttachmentId(row.fspAttachmentId())
          .fspAmendmentNumber(row.fspAmendmentNumber())
          .attachmentName(row.attachmentName())
          .attachmentDescription(row.attachmentDescription())
          .attachmentSize(row.attachmentSize())
          .consolidatedInd(row.consolidatedInd())
          .build());
    }
  }
}
