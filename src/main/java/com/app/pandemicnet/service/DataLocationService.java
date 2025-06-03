package com.app.pandemicnet.service;

import com.app.pandemicnet.dto.DataLocationRequest;
import com.app.pandemicnet.dto.DataLocationResponse;

import java.util.List;
import java.util.UUID;

public interface DataLocationService {
    DataLocationResponse createDataCentre(DataLocationRequest dataCentre);
    DataLocationResponse getDataCentre(Long id);
    DataLocationResponse getDataCentreByUniqueId(UUID uniqueId);
    List<DataLocationResponse> getAllDataCentres();
    DataLocationResponse updateDataCentre(Long id, DataLocationRequest request);
    void deleteDataCentre(Long id);

}
