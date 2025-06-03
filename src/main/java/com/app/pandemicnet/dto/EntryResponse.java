package com.app.pandemicnet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntryResponse {
    private Long id;
    private Long userId;
    private Long dataCentreId;
    private LocalDateTime timestamp;
}