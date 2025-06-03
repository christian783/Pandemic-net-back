package com.app.pandemicnet.controller;

import com.app.pandemicnet.dto.DataLocationRequest;
import com.app.pandemicnet.dto.DataLocationResponse;
import com.app.pandemicnet.service.DataLocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/data-centres")
public class DataLocationController {

    @Autowired
    private DataLocationService dataLocationService;

    @PostMapping
    public ResponseEntity<DataLocationResponse> createDataCentre(@Valid @RequestBody DataLocationRequest request) {
        DataLocationResponse response = dataLocationService.createDataCentre(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataLocationResponse> getDataCentre(@PathVariable Long id) {
        DataLocationResponse response = dataLocationService.getDataCentre(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unique/{uniqueId}")
    public ResponseEntity<DataLocationResponse> getDataCentreByUniqueId(@PathVariable UUID uniqueId) {
        DataLocationResponse response = dataLocationService.getDataCentreByUniqueId(uniqueId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DataLocationResponse>> getAllDataCentres() {
        List<DataLocationResponse> responses = dataLocationService.getAllDataCentres();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataLocationResponse> updateDataCentre(@PathVariable Long id, @Valid @RequestBody DataLocationRequest request) {
        DataLocationResponse response = dataLocationService.updateDataCentre(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDataCentre(@PathVariable Long id) {
        dataLocationService.deleteDataCentre(id);
        return ResponseEntity.ok("Data Centre deleted");
    }
}
