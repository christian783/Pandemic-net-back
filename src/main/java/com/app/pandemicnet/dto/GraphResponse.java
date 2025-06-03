package com.app.pandemicnet.dto;
import lombok.Data;

import java.util.List;

@Data
public class GraphResponse {
    private List<Node> nodes;
    private List<Edge> edges;
}

