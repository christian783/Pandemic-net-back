package com.app.pandemicnet.controller;

import com.app.pandemicnet.dto.GraphResponse;
import com.app.pandemicnet.service.impl.GraphServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph")
@AllArgsConstructor
public class GraphController {

    private GraphServiceImpl graphService;

    @GetMapping("/at-risk")
    public ResponseEntity<GraphResponse> getAtRiskGraph() {
        GraphResponse response = graphService.getAtRiskGraph();
        return ResponseEntity.ok(response);
    }
}