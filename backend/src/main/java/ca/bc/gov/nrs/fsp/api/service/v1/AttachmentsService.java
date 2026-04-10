package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspAttachment;
import ca.bc.gov.nrs.fsp.api.exception.EntityNotFoundException;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspAttachmentRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentResponse;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentsService {

  private final FspAttachmentRepository attachmentRepository;
  private final FspMapper fspMapper;

  public List<AttachmentResponse> getByFspId(Long fspId) {
    return fspMapper.toAttachmentResponseList(attachmentRepository.findByFspId(fspId));
  }

  public FspAttachment getAttachment(Long fspId, Long attachmentId) {
    return attachmentRepository.findById(attachmentId)
        .filter(a -> a.getFspId().equals(fspId))
        .orElseThrow(() -> new EntityNotFoundException(FspAttachment.class, "attachmentId", String.valueOf(attachmentId), "fspId", String.valueOf(fspId)));
  }

  @Transactional
  public AttachmentResponse upload(Long fspId, MultipartFile file, String typeCode)
      throws IOException {
    FspAttachment attachment = new FspAttachment();
    attachment.setFspId(fspId);
    attachment.setAttachmentTypeCode(typeCode);
    attachment.setFileName(file.getOriginalFilename());
    attachment.setFileSize(file.getSize());
    attachment.setFileContent(file.getBytes());
    attachment.setEntryUserid(RequestUtil.getCurrentUserName());
    attachment.setEntryTimestamp(LocalDateTime.now());
    return fspMapper.toAttachmentResponse(attachmentRepository.save(attachment));
  }

  @Transactional
  public void delete(Long fspId, Long attachmentId) {
    FspAttachment attachment = getAttachment(fspId, attachmentId);
    attachmentRepository.delete(attachment);
  }
}