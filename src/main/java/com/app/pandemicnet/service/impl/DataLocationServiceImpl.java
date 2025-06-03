package com.app.pandemicnet.service.impl;


import com.app.pandemicnet.Exception.ResourceNotFoundException;
import com.app.pandemicnet.dto.DataLocationRequest;
import com.app.pandemicnet.dto.DataLocationResponse;
import com.app.pandemicnet.model.DataLocation;
import com.app.pandemicnet.repository.DataLocationRepository;
import com.app.pandemicnet.service.DataLocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataLocationServiceImpl implements DataLocationService {

    private DataLocationRepository dataLocationRepository;

    @Transactional
    public DataLocationResponse createDataCentre(DataLocationRequest request) {
        DataLocation dataCentre = new DataLocation();
        dataCentre.setName(request.getName());
        dataCentre.setLatitude(request.getLatitude());
        dataCentre.setLongitude(request.getLongitude());
        dataCentre.setAddress(request.getAddress());
        dataCentre.setUniqueId(UUID.randomUUID());
        DataLocation savedDataCentre = dataLocationRepository.save(dataCentre);
        return mapToDataCentreResponse(savedDataCentre);
    }

    public DataLocationResponse getDataCentre(Long id) {
        DataLocation dataCentre = dataLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Data Centre not found"));
        return mapToDataCentreResponse(dataCentre);
    }

    public DataLocationResponse getDataCentreByUniqueId(UUID uniqueId) {
        DataLocation dataCentre = dataLocationRepository.findByUniqueId(uniqueId).orElseThrow(() ->
                new ResourceNotFoundException("Data Centre not found"));

        return mapToDataCentreResponse(dataCentre);
    }

    public List<DataLocationResponse> getAllDataCentres() {
        return dataLocationRepository.findAll().stream()
                .map(this::mapToDataCentreResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DataLocationResponse updateDataCentre(Long id, DataLocationRequest request) {
        DataLocation dataCentre = dataLocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data Centre not found"));
        dataCentre.setName(request.getName());
        dataCentre.setLatitude(request.getLatitude());
        dataCentre.setLongitude(request.getLongitude());
        dataCentre.setAddress(request.getAddress());
        DataLocation updatedDataCentre = dataLocationRepository.save(dataCentre);
        return mapToDataCentreResponse(updatedDataCentre);
    }

    @Transactional
    public void deleteDataCentre(Long id) {
        if (!dataLocationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Data Centre not found");
        }
        dataLocationRepository.deleteById(id);
    }

    private DataLocationResponse mapToDataCentreResponse(DataLocation dataCentre) {
        DataLocationResponse response = new DataLocationResponse();
        response.setId(dataCentre.getId());
        response.setUniqueId(dataCentre.getUniqueId());
        response.setName(dataCentre.getName());
        response.setLatitude(dataCentre.getLatitude());
        response.setLongitude(dataCentre.getLongitude());
        response.setAddress(dataCentre.getAddress());
        return response;
    }
}