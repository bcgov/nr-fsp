package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.model.v1.FspStockingStandard;
import ca.bc.gov.nrs.fsp.api.exception.EntityNotFoundException;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspStockingStandardRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.StandardRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StandardsService {

  private final FspStockingStandardRepository standardsRepository;
  private final FspMapper fspMapper;

  public List<StandardRequest> getByFspId(Long fspId) {
    return fspMapper.toStandardRequestList(standardsRepository.findByFspId(fspId));
  }

  @Transactional
  public List<StandardRequest> saveAll(Long fspId, List<StandardRequest> requests) {
    standardsRepository.deleteByFspId(fspId);
    List<FspStockingStandard> entities = fspMapper.toStandardEntityList(requests);
    entities.forEach(e -> e.setFspId(fspId));
    standardsRepository.saveAll(entities);
    return fspMapper.toStandardRequestList(standardsRepository.findByFspId(fspId));
  }

  @Transactional
  public void delete(Long standardId) {
    if (!standardsRepository.existsById(standardId)) {
      throw new EntityNotFoundException(FspStockingStandard.class, "standardId", String.valueOf(standardId));
    }
    standardsRepository.deleteById(standardId);
  }
}