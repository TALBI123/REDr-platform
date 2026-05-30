package com.example.demo.agency.service;

import com.example.demo.agency.repository.AgencyRepository;
import com.example.demo.models.agency.Agency;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencyService {
    private final AgencyRepository agencyRepository;

    public List<Agency> getAllAgencies() {
        return agencyRepository.findAll();
    }
}