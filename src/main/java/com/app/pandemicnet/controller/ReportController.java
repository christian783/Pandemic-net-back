package com.app.pandemicnet.controller;

import com.app.pandemicnet.model.ContactTrace;
import com.app.pandemicnet.model.StatusReport;
import com.app.pandemicnet.model.User;
import com.app.pandemicnet.service.impl.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/contact-tracing/{personId}")
    public ResponseEntity<List<ContactTrace>> getContactTraces(
            @PathVariable Long personId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(reportService.getContactTraces(personId, start, end));
    }

    @GetMapping("/contact-path")
    public ResponseEntity<List<Long>> getContactPath(
            @RequestParam Long startUserId,
            @RequestParam Long endUserId) {
        return ResponseEntity.ok(reportService.getContactPath(startUserId, endUserId));
    }

    @GetMapping("/persons/search")
    public ResponseEntity<List<User>> searchPersons(@RequestParam String query) {
        return ResponseEntity.ok(reportService.searchPersons(query));
    }

    @GetMapping("/status")
    public ResponseEntity<List<StatusReport>> getStatusReports(
            @RequestParam String status,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(reportService.getStatusReports(status, start, end, location));
    }

    @GetMapping("/contact-tracing/export")
    public ResponseEntity<byte[]> exportContactTraces(
            @RequestParam Long personId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam String format) throws IOException {
        if ("pdf".equalsIgnoreCase(format)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contact_traces.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(reportService.exportContactTracesToPdf(personId, start, end));
        } else if ("csv".equalsIgnoreCase(format)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contact_traces.csv")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(reportService.exportContactTracesToCsv(personId, start, end).getBytes());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/status/export")
    public ResponseEntity<byte[]> exportStatusReports(
            @RequestParam String status,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) String location,
            @RequestParam String format) {
        if ("pdf".equalsIgnoreCase(format)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=status_reports.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(reportService.exportStatusReportsToPdf(status, start, end, location));
        } else if ("csv".equalsIgnoreCase(format)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=status_reports.csv")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(reportService.exportStatusReportsToCsv(status, start, end, location).getBytes());
        }
        return ResponseEntity.badRequest().build();
    }
}
