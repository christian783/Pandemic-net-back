package com.app.pandemicnet.dto;

import lombok.Data;

@Data
public class Edge {
    private Long from;
    private Long to;
    private Double weight; // Time difference in seconds
}
