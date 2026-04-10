package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.FspStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.model.v1.Fsp;
import ca.bc.gov.nrs.fsp.api.exception.EntityNotFoundException;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FspServiceTest {

  @Mock FspRepository fspRepository;
  @Mock FspStoredProcedureDao storedProcedureDao;
  @Mock FspMapper fspMapper;

  @InjectMocks FspService fspService;

  private Fsp fsp;
  private FspRequest fspRequest;

  @BeforeEach
  void setUp() {
    fsp = new Fsp();
    fsp.setFspId(1L);
    fsp.setClientNumber("00123456");
    fsp.setClientName("Test Licensee");
    fsp.setFspStatusCode("APP");
    fsp.setOrgUnitNo(100L);

    fspRequest = new FspRequest();
    fspRequest.setFspId(1L);
    fspRequest.setClientNumber("00123456");
    fspRequest.setClientName("Test Licensee");
    fspRequest.setFspStatusCode("APP");
    fspRequest.setOrgUnitNo(100L);
  }

  @Test
  void getById_whenFspExists_returnsRequest() {
    when(fspRepository.findById(1L)).thenReturn(Optional.of(fsp));
    when(fspMapper.toStruct(fsp)).thenReturn(fspRequest);

    FspRequest result = fspService.getById(1L);

    assertThat(result).isNotNull();
    assertThat(result.getFspId()).isEqualTo(1L);
    assertThat(result.getClientNumber()).isEqualTo("00123456");
    verify(fspRepository).findById(1L);
    verify(fspMapper).toStruct(fsp);
  }

  @Test
  void getById_whenFspNotFound_throwsEntityNotFoundException() {
    when(fspRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> fspService.getById(99L))
        .isInstanceOf(EntityNotFoundException.class);

    verify(fspRepository).findById(99L);
    verifyNoInteractions(fspMapper);
  }

  @Test
  void update_whenFspExists_updatesAndReturns() {
    when(fspRepository.findById(1L)).thenReturn(Optional.of(fsp));
    when(fspRepository.save(fsp)).thenReturn(fsp);
    when(fspMapper.toStruct(fsp)).thenReturn(fspRequest);

    FspRequest result = fspService.update(1L, fspRequest);

    assertThat(result).isNotNull();
    verify(fspMapper).updateEntity(fspRequest, fsp);
    verify(fspRepository).save(fsp);
  }

  @Test
  void update_whenFspNotFound_throwsEntityNotFoundException() {
    when(fspRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> fspService.update(99L, fspRequest))
        .isInstanceOf(EntityNotFoundException.class);

    verify(fspRepository, never()).save(any());
  }

  @Test
  void search_mapsStoredProcedureResultsToSearchResults() {
    FspSearchRequest request = new FspSearchRequest();
    request.setClientNumber("00123456");
    request.setOrgUnitNo(100L);
    request.setStatusCode("APP");

    List<Map<String, Object>> rows = List.of(Map.of(
        "FSP_ID", 1L,
        "CLIENT_NUMBER", "00123456",
        "CLIENT_NAME", "Test Licensee",
        "FSP_STATUS_CODE", "APP",
        "DISTRICT_NAME", "Prince George Natural Resource District"
    ));

    when(storedProcedureDao.fsp100Search("00123456", 100L, "APP")).thenReturn(rows);

    List<FspSearchResult> results = fspService.search(request);

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getFspId()).isEqualTo(1L);
    assertThat(results.get(0).getClientNumber()).isEqualTo("00123456");
    assertThat(results.get(0).getDistrictName())
        .isEqualTo("Prince George Natural Resource District");
  }

  @Test
  void search_withNullParams_passesNullsToDao() {
    FspSearchRequest request = new FspSearchRequest();
    when(storedProcedureDao.fsp100Search(null, null, null)).thenReturn(List.of());

    List<FspSearchResult> results = fspService.search(request);

    assertThat(results).isEmpty();
    verify(storedProcedureDao).fsp100Search(null, null, null);
  }
}
