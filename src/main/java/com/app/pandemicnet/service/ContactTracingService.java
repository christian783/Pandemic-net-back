package com.app.pandemicnet.service;

import com.app.pandemicnet.model.ContactTrace;

import java.time.LocalDateTime;
import java.util.List;

public interface ContactTracingService {
    List<ContactTrace> getContactTraces(Long userId, LocalDateTime start, LocalDateTime end);
    List<Long> getShortestContactPath(Long startUserId, Long endUserId);
}