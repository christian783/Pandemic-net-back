package com.app.pandemicnet.service.impl;

import com.app.pandemicnet.model.ContactTrace;
import com.app.pandemicnet.model.StatusReport;
import com.app.pandemicnet.model.User;
import com.app.pandemicnet.repository.StatusReportRepository;
import com.app.pandemicnet.repository.UserRepository;
import com.app.pandemicnet.service.ContactTracingService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ContactTracingService contactTracingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusReportRepository statusReportRepository;

    public List<ContactTrace> getContactTraces(Long userId, LocalDateTime start, LocalDateTime end) {
        return contactTracingService.getContactTraces(userId, start, end);
    }

    public List<Long> getContactPath(Long startUserId, Long endUserId) {
        return contactTracingService.getShortestContactPath(startUserId, endUserId);
    }

    public List<User> searchPersons(String query) {
        return userRepository.findByPhoneNumberContainingOrEmailContaining(query, query);
    }

    public List<StatusReport> getStatusReports(String status, LocalDateTime start, LocalDateTime end, String location) {
        return statusReportRepository.findByStatusAndCreatedAtBetweenAndLocation(
                status, start, end, location);
    }

    public byte[] exportContactTracesToPdf(Long userId, LocalDateTime start, LocalDateTime end) {
        List<ContactTrace> traces = getContactTraces(userId, start, end);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Contact Tracing Report for User-" + userId));
        for (ContactTrace trace : traces) {
            document.add(new Paragraph(
                    String.format("User-%d to User-%d at %s, Time Diff: %d seconds, Date: %s",
                            trace.getUser1().getId(), trace.getUser2().getId(),
                            trace.getDataCentre().getName(), trace.getTimeDiffSeconds(),
                            trace.getTimestamp())));
        }
        document.close();
        return baos.toByteArray();
    }

    public String exportContactTracesToCsv(Long userId, LocalDateTime start, LocalDateTime end) throws IOException {
        List<ContactTrace> traces = getContactTraces(userId, start, end);
        StringWriter sw = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(sw)) {
            csvWriter.writeNext(new String[]{"User1", "User2", "DataCentre", "TimeDiff", "Timestamp"});
            for (ContactTrace trace : traces) {
                csvWriter.writeNext(new String[]{
                        trace.getUser1().getId().toString(),
                        trace.getUser2().getId().toString(),
                        trace.getDataCentre().getName(),
                        trace.getTimeDiffSeconds().toString(),
                        trace.getTimestamp().toString()
                });
            }
        }
        return sw.toString();
    }

    public byte[] exportStatusReportsToPdf(String status, LocalDateTime start, LocalDateTime end, String location) {
        List<StatusReport> reports = getStatusReports(status, start, end, location);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Status Report"));
        for (StatusReport report : reports) {
            document.add(new Paragraph(
                    String.format("User-%d: %s at %s, Date: %s",
                            report.getUserId(), report.getStatus(), report.getLocation(),
                            report.getCreatedAt())));
        }
        document.close();
        return baos.toByteArray();
    }

    public String exportStatusReportsToCsv(String status, LocalDateTime start, LocalDateTime end, String location) {
        List<StatusReport> reports = getStatusReports(status, start, end, location);
        StringWriter sw = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(sw)) {
            csvWriter.writeNext(new String[]{"UserId", "Status", "Location", "Timestamp"});
            for (StatusReport report : reports) {
                csvWriter.writeNext(new String[]{
                        report.getUserId().toString(),
                        report.getStatus(),
                        report.getLocation(),
                        report.getCreatedAt().toString()
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sw.toString();
    }
}
