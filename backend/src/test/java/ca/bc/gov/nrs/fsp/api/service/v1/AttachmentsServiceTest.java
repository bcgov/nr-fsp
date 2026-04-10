package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspAttachment;
import ca.bc.gov.nrs.fsp.api.exception.EntityNotFoundException;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspAttachmentRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentResponse;
import ca.bc.gov.nrs.fsp.api.util.RequestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentsServiceTest {

  @Mock FspAttachmentRepository attachmentRepository;
  @Mock FspMapper fspMapper;

  @InjectMocks AttachmentsService attachmentsService;

  private FspAttachment attachment;
  private AttachmentResponse attachmentResponse;

  @BeforeEach
  void setUp() {
    attachment = new FspAttachment();
    attachment.setFspAttachmentId(20L);
    attachment.setFspId(1L);
    attachment.setAttachmentTypeCode("MAP");
    attachment.setFileName("test-map.pdf");
    attachment.setFileSize(1024L);
    attachment.setFileContent(new byte[]{1, 2, 3});
    attachment.setEntryUserid("TESTUSER");
    attachment.setEntryTimestamp(LocalDateTime.now());

    attachmentResponse = AttachmentResponse.builder()
        .fspAttachmentId(20L)
        .fspId(1L)
        .attachmentTypeCode("MAP")
        .fileName("test-map.pdf")
        .fileSize(1024L)
        .build();
  }

  @Test
  void getByFspId_returnsMappedResponsesWithoutFileContent() {
    when(attachmentRepository.findByFspId(1L)).thenReturn(List.of(attachment));
    when(fspMapper.toAttachmentResponseList(List.of(attachment)))
        .thenReturn(List.of(attachmentResponse));

    List<AttachmentResponse> result = attachmentsService.getByFspId(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFileName()).isEqualTo("test-map.pdf");
  }

  @Test
  void getAttachment_whenExistsAndBelongsToFsp_returnsEntity() {
    when(attachmentRepository.findById(20L)).thenReturn(Optional.of(attachment));

    FspAttachment result = attachmentsService.getAttachment(1L, 20L);

    assertThat(result.getFspAttachmentId()).isEqualTo(20L);
    assertThat(result.getFileContent()).isEqualTo(new byte[]{1, 2, 3});
  }

  @Test
  void getAttachment_whenBelongsToDifferentFsp_throwsEntityNotFoundException() {
    attachment.setFspId(99L);
    when(attachmentRepository.findById(20L)).thenReturn(Optional.of(attachment));

    assertThatThrownBy(() -> attachmentsService.getAttachment(1L, 20L))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAttachment_whenNotFound_throwsEntityNotFoundException() {
    when(attachmentRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> attachmentsService.getAttachment(1L, 99L))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void upload_savesAttachmentWithCorrectFields() throws IOException {
    MockMultipartFile file = new MockMultipartFile(
        "file", "test-map.pdf", "application/pdf", new byte[]{1, 2, 3}
    );
    when(attachmentRepository.save(any(FspAttachment.class))).thenReturn(attachment);
    when(fspMapper.toAttachmentResponse(attachment)).thenReturn(attachmentResponse);

    try (MockedStatic<RequestUtil> requestUtil = mockStatic(RequestUtil.class)) {
      requestUtil.when(RequestUtil::getCurrentUserName).thenReturn("TESTUSER");

      AttachmentResponse result = attachmentsService.upload(1L, file, "MAP");

      assertThat(result.getFileName()).isEqualTo("test-map.pdf");
      verify(attachmentRepository).save(argThat(a ->
          a.getFspId().equals(1L) &&
          a.getAttachmentTypeCode().equals("MAP") &&
          a.getFileName().equals("test-map.pdf") &&
          a.getEntryUserid().equals("TESTUSER")
      ));
    }
  }

  @Test
  void delete_whenAttachmentExists_deletesIt() {
    when(attachmentRepository.findById(20L)).thenReturn(Optional.of(attachment));
    attachmentsService.delete(1L, 20L);
    verify(attachmentRepository).delete(attachment);
  }

  @Test
  void delete_whenNotFound_throwsEntityNotFoundException() {
    when(attachmentRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> attachmentsService.delete(1L, 99L))
        .isInstanceOf(EntityNotFoundException.class);

    verify(attachmentRepository, never()).delete(any());
  }
}
