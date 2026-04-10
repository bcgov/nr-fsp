package ca.bc.gov.nrs.fsp.api.service.v1;

import ca.bc.gov.nrs.fsp.api.dao.v1.FspStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.model.v1.Fsp;
import ca.bc.gov.nrs.fsp.api.exception.EntityNotFoundException;
import ca.bc.gov.nrs.fsp.api.mapper.v1.FspMapper;
import ca.bc.gov.nrs.fsp.api.repository.v1.FspRepository;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchRequest;
import ca.bc.gov.nrs.fsp.api.struct.v1.FspSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FspService {

    private final FspRepository fspRepository;
    private final FspStoredProcedureDao storedProcedureDao;
    private final FspMapper fspMapper;

    public List<FspSearchResult> search(FspSearchRequest request) {
        List<Map<String, Object>> rows = storedProcedureDao.fsp100Search(
                request.getClientNumber(),
                request.getOrgUnitNo(),
                request.getStatusCode()
        );

        return rows.stream().map(row -> FspSearchResult.builder()
                .fspId(((Number) row.get("FSP_ID")).longValue())
                .clientNumber((String) row.get("CLIENT_NUMBER"))
                .clientName((String) row.get("CLIENT_NAME"))
                .fspStatusCode((String) row.get("FSP_STATUS_CODE"))
                .districtName((String) row.get("DISTRICT_NAME"))
                .build()
        ).collect(Collectors.toList());
    }

    public FspRequest getById(Long fspId) {
        return fspMapper.toStruct(findOrThrow(fspId));
    }

    @Transactional
    public FspRequest update(Long fspId, FspRequest request) {
        Fsp fsp = findOrThrow(fspId);
        fspMapper.updateEntity(request, fsp);
        return fspMapper.toStruct(fspRepository.save(fsp));
    }

    private Fsp findOrThrow(Long fspId) {
        return fspRepository.findById(fspId)
                .orElseThrow(() -> new EntityNotFoundException(Fsp.class, "fspId", String.valueOf(fspId)));
    }
}