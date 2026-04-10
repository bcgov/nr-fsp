package ca.bc.gov.nrs.fsp.api.mapper.v1;

import ca.bc.gov.nrs.fsp.api.mapper.LocalDateTimeMapper;
import ca.bc.gov.nrs.fsp.api.mapper.StringMapper;
import ca.bc.gov.nrs.fsp.api.model.v1.Fsp;
import ca.bc.gov.nrs.fsp.api.model.v1.FspAttachment;
import ca.bc.gov.nrs.fsp.api.model.v1.FspStockingStandard;
import ca.bc.gov.nrs.fsp.api.model.v1.FspWorkflow;
import ca.bc.gov.nrs.fsp.api.struct.v1.AttachmentResponse;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.StandardRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.WorkflowResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {LocalDateTimeMapper.class, StringMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FspMapper {

  FspRequest toStruct(Fsp fsp);

  List<FspRequest> toStructList(List<Fsp> fsps);

  Fsp toEntity(FspRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(FspRequest request, @MappingTarget Fsp fsp);

  WorkflowResponse toWorkflowResponse(FspWorkflow workflow);

  List<WorkflowResponse> toWorkflowResponseList(List<FspWorkflow> workflows);

  AttachmentResponse toAttachmentResponse(FspAttachment attachment);

  List<AttachmentResponse> toAttachmentResponseList(List<FspAttachment> attachments);

  FspStockingStandard toStandardEntity(StandardRequest request);

  List<FspStockingStandard> toStandardEntityList(List<StandardRequest> requests);

  StandardRequest toStandardRequest(FspStockingStandard entity);

  List<StandardRequest> toStandardRequestList(List<FspStockingStandard> entities);
}