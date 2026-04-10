package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspStockingStandard;
import ca.bc.gov.nrs.fsp.api.exception.EntityNotFoundException;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspStockingStandardRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.StandardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandardsServiceTest {

  @Mock FspStockingStandardRepository standardsRepository;
  @Mock FspMapper fspMapper;

  @InjectMocks StandardsService standardsService;

  private FspStockingStandard entity;
  private StandardRequest request;

  @BeforeEach
  void setUp() {
    entity = new FspStockingStandard();
    entity.setFspStockingStandardId(5L);
    entity.setFspId(1L);
    entity.setBgcZoneCode("SBS");
    entity.setBgcSubzoneCode("dw");
    entity.setSpeciesCode1("PL");
    entity.setSpeciesPct1(80);
    entity.setMinWellSpacedTrees(400);
    entity.setPreferredWellSpacedTrees(500);
    entity.setDefaultInd("Y");

    request = new StandardRequest();
    request.setFspStockingStandardId(5L);
    request.setBgcZoneCode("SBS");
    request.setBgcSubzoneCode("dw");
    request.setSpeciesCode1("PL");
    request.setSpeciesPct1(80);
    request.setMinWellSpacedTrees(400);
    request.setPreferredWellSpacedTrees(500);
    request.setDefaultInd("Y");
  }

  @Test
  void getByFspId_returnsMappedRequests() {
    when(standardsRepository.findByFspId(1L)).thenReturn(List.of(entity));
    when(fspMapper.toStandardRequestList(List.of(entity))).thenReturn(List.of(request));

    List<StandardRequest> result = standardsService.getByFspId(1L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getBgcZoneCode()).isEqualTo("SBS");
    assertThat(result.get(0).getSpeciesCode1()).isEqualTo("PL");
  }

  @Test
  void getByFspId_whenNoStandards_returnsEmptyList() {
    when(standardsRepository.findByFspId(1L)).thenReturn(List.of());
    when(fspMapper.toStandardRequestList(List.of())).thenReturn(List.of());

    assertThat(standardsService.getByFspId(1L)).isEmpty();
  }

  @Test
  void saveAll_deletesExistingAndSavesNew() {
    when(fspMapper.toStandardEntityList(List.of(request))).thenReturn(List.of(entity));
    when(standardsRepository.saveAll(anyList())).thenReturn(List.of(entity));
    when(standardsRepository.findByFspId(1L)).thenReturn(List.of(entity));
    when(fspMapper.toStandardRequestList(List.of(entity))).thenReturn(List.of(request));

    List<StandardRequest> result = standardsService.saveAll(1L, List.of(request));

    verify(standardsRepository).deleteByFspId(1L);
    verify(standardsRepository).saveAll(anyList());
    assertThat(result).hasSize(1);
  }

  @Test
  void saveAll_setsFspIdOnAllEntities() {
    FspStockingStandard entityWithoutFspId = new FspStockingStandard();
    when(fspMapper.toStandardEntityList(anyList())).thenReturn(List.of(entityWithoutFspId));
    when(standardsRepository.saveAll(anyList())).thenReturn(List.of(entity));
    when(standardsRepository.findByFspId(1L)).thenReturn(List.of(entity));
    when(fspMapper.toStandardRequestList(anyList())).thenReturn(List.of(request));

    standardsService.saveAll(1L, List.of(request));

    assertThat(entityWithoutFspId.getFspId()).isEqualTo(1L);
  }

  @Test
  void delete_whenExists_deletesById() {
    when(standardsRepository.existsById(5L)).thenReturn(true);
    standardsService.delete(5L);
    verify(standardsRepository).deleteById(5L);
  }

  @Test
  void delete_whenNotFound_throwsEntityNotFoundException() {
    when(standardsRepository.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> standardsService.delete(99L))
        .isInstanceOf(EntityNotFoundException.class);

    verify(standardsRepository, never()).deleteById(any());
  }
}
