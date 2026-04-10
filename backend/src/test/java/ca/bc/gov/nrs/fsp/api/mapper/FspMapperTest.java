package ca.bc.gov.nrs.fsp.api.mapper;

import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapperImpl;
import ca.bc.gov.nrs.fsp.api.model.v1.Fsp;
import ca.bc.gov.nrs.fsp.api.model.v1.FspAttachment;
import ca.bc.gov.nrs.fsp.api.model.v1.FspStockingStandard;
import ca.bc.gov.nrs.fsp.api.model.v1.FspWorkflow;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentResponse;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.StandardRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FspMapperImpl.class, StringMapper.class, LocalDateTimeMapper.class})
class FspMapperTest {
  @Autowired
  FspMapper mapper;
  
  @Test
  void toStruct_mapsAllFspFields() {
    Fsp fsp = new Fsp();
    fsp.setFspId(1L);
    fsp.setClientNumber("00123456");
    fsp.setClientName("Test Licensee");
    fsp.setFspStatusCode("APP");
    fsp.setOrgUnitNo(100L);
    fsp.setFspStartDate(LocalDate.of(2020, 1, 1));
    fsp.setFspEndDate(LocalDate.of(2025, 12, 31));
    fsp.setAmendmentNumber(2);

    FspRequest result = mapper.toStruct(fsp);

    assertThat(result.getFspId()).isEqualTo(1L);
    assertThat(result.getClientNumber()).isEqualTo("00123456");
    assertThat(result.getClientName()).isEqualTo("Test Licensee");
    assertThat(result.getFspStatusCode()).isEqualTo("APP");
    assertThat(result.getOrgUnitNo()).isEqualTo(100L);
    assertThat(result.getFspStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
    assertThat(result.getAmendmentNumber()).isEqualTo(2);
  }

  @Test
  void toEntity_mapsAllFspRequestFields() {
    FspRequest request = new FspRequest();
    request.setFspId(1L);
    request.setClientNumber("00123456");
    request.setFspStatusCode("DFT");
    request.setOrgUnitNo(100L);

    Fsp result = mapper.toEntity(request);

    assertThat(result.getFspId()).isEqualTo(1L);
    assertThat(result.getClientNumber()).isEqualTo("00123456");
    assertThat(result.getFspStatusCode()).isEqualTo("DFT");
  }

  @Test
  void updateEntity_onlyUpdatesNonNullFields() {
    Fsp existing = new Fsp();
    existing.setFspId(1L);
    existing.setClientName("Original Name");
    existing.setFspStatusCode("DFT");

    FspRequest update = new FspRequest();
    update.setFspStatusCode("APP");
    // clientName intentionally null — should not overwrite

    mapper.updateEntity(update, existing);

    assertThat(existing.getFspStatusCode()).isEqualTo("APP");
    assertThat(existing.getClientName()).isEqualTo("Original Name");
  }

  @Test
  void toStructList_mapsMultipleEntities() {
    Fsp fsp1 = new Fsp();
    fsp1.setFspId(1L);
    fsp1.setClientNumber("00111111");

    Fsp fsp2 = new Fsp();
    fsp2.setFspId(2L);
    fsp2.setClientNumber("00222222");

    List<FspRequest> result = mapper.toStructList(List.of(fsp1, fsp2));

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getClientNumber()).isEqualTo("00111111");
    assertThat(result.get(1).getClientNumber()).isEqualTo("00222222");
  }

  @Test
  void toWorkflowResponse_mapsAllFields() {
    FspWorkflow workflow = new FspWorkflow();
    workflow.setFspWorkflowId(10L);
    workflow.setFspId(1L);
    workflow.setWorkflowStatusCode("SUBMITTED");
    workflow.setWorkflowActionCode("SUBMIT");
    workflow.setComments("Test comment");
    workflow.setActionUserid("TESTUSER");
    workflow.setActionTimestamp(LocalDateTime.of(2024, 6, 1, 10, 0));

    WorkflowResponse result = mapper.toWorkflowResponse(workflow);

    assertThat(result.getFspWorkflowId()).isEqualTo(10L);
    assertThat(result.getFspId()).isEqualTo(1L);
    assertThat(result.getWorkflowStatusCode()).isEqualTo("SUBMITTED");
    assertThat(result.getComments()).isEqualTo("Test comment");
    assertThat(result.getActionUserid()).isEqualTo("TESTUSER");
  }

  @Test
  void toAttachmentResponse_excludesFileContent() {
    FspAttachment attachment = new FspAttachment();
    attachment.setFspAttachmentId(20L);
    attachment.setFspId(1L);
    attachment.setFileName("test.pdf");
    attachment.setFileSize(2048L);
    attachment.setFileContent(new byte[]{1, 2, 3, 4});

    AttachmentResponse result = mapper.toAttachmentResponse(attachment);

    assertThat(result.getFspAttachmentId()).isEqualTo(20L);
    assertThat(result.getFileName()).isEqualTo("test.pdf");
    assertThat(result.getFileSize()).isEqualTo(2048L);
  }

  @Test
  void toStandardRequest_mapsAllFields() {
    FspStockingStandard entity = new FspStockingStandard();
    entity.setFspStockingStandardId(5L);
    entity.setFspId(1L);
    entity.setBgcZoneCode("SBS");
    entity.setBgcSubzoneCode("dw");
    entity.setSpeciesCode1("PL");
    entity.setSpeciesPct1(80);
    entity.setSpeciesCode2("SX");
    entity.setSpeciesPct2(20);
    entity.setMinWellSpacedTrees(400);
    entity.setPreferredWellSpacedTrees(500);
    entity.setDefaultInd("Y");

    StandardRequest result = mapper.toStandardRequest(entity);

    assertThat(result.getFspStockingStandardId()).isEqualTo(5L);
    assertThat(result.getBgcZoneCode()).isEqualTo("SBS");
    assertThat(result.getSpeciesCode1()).isEqualTo("PL");
    assertThat(result.getSpeciesPct1()).isEqualTo(80);
    assertThat(result.getSpeciesCode2()).isEqualTo("SX");
    assertThat(result.getMinWellSpacedTrees()).isEqualTo(400);
    assertThat(result.getDefaultInd()).isEqualTo("Y");
  }

  @Test
  void toStandardEntity_mapsAllFields() {
    StandardRequest request = new StandardRequest();
    request.setBgcZoneCode("ICH");
    request.setBgcSubzoneCode("mw");
    request.setSpeciesCode1("FD");
    request.setSpeciesPct1(100);
    request.setMinWellSpacedTrees(300);
    request.setDefaultInd("N");

    FspStockingStandard result = mapper.toStandardEntity(request);

    assertThat(result.getBgcZoneCode()).isEqualTo("ICH");
    assertThat(result.getSpeciesCode1()).isEqualTo("FD");
    assertThat(result.getMinWellSpacedTrees()).isEqualTo(300);
  }

  @Test
  void toStruct_withNullInput_returnsNull() {
    assertThat(mapper.toStruct(null)).isNull();
  }

  @Test
  void toStructList_withEmptyList_returnsEmptyList() {
    assertThat(mapper.toStructList(List.of())).isEmpty();
  }
}
