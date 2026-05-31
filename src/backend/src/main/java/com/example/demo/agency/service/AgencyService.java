package com.example.demo.agency.service;

import com.example.demo.agency.dto.AgencyCreateUpdateDTO;
import com.example.demo.agency.dto.AgencySearchCriteria;
import com.example.demo.agency.mapper.AgencyCommandMapper;
import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.agency.repository.AgencySpecifications;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.models.agency.Agency;
import com.example.demo.models.enums.AgencyStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgencyService {
    private final AgencyRepository agencyRepository;
    private final AgencyCommandMapper agencyCommandMapper;

    public List<Agency> getAllAgencies() {
        return agencyRepository.findAll();
    }

    public Page<Agency> searchAgencies(AgencySearchCriteria criteria, Pageable pageable) {
        Specification<Agency> spec = AgencySpecifications.from(criteria);
        return agencyRepository.findAll(spec, pageable);
    }

    public Agency getById(String agencyId) {
        return agencyRepository.findById(agencyId)
            .orElseThrow(() -> new ResourceNotFoundException("Agency not found: " + agencyId));
    }

    public Agency getByIdAndStatus(String agencyId, AgencyStatus status) {
        return agencyRepository.findByIdAndStatus(agencyId, status)
            .orElseThrow(() -> new ResourceNotFoundException("Agency not found: " + agencyId));
    }

    @Transactional
    public Agency createAgency(AgencyCreateUpdateDTO request) {
        Agency agency = agencyCommandMapper.toEntity(request);
        if (agency.getStatus() == null) {
            agency.setStatus(AgencyStatus.PENDING);
        }
        if (agency.getInscriptionDate() == null) {
            agency.setInscriptionDate(LocalDate.now());
        }
        return agencyRepository.save(agency);
    }

    @Transactional
    public Agency updateAgency(String agencyId, AgencyCreateUpdateDTO request) {
        Agency agency = getById(agencyId);
        agencyCommandMapper.updateEntity(request, agency);
        return agencyRepository.save(agency);
    }

    @Transactional
    public void deleteAgency(String agencyId) {
        Agency agency = getById(agencyId);
        agencyRepository.delete(agency);
    }
}