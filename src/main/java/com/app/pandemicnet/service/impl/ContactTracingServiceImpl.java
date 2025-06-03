package com.app.pandemicnet.service.impl;

import com.app.pandemicnet.model.ContactTrace;
import com.app.pandemicnet.repository.ContactTraceRepository;
import com.app.pandemicnet.repository.DataLocationRepository;
import com.app.pandemicnet.repository.EntryRepository;
import com.app.pandemicnet.repository.UserRepository;
import com.app.pandemicnet.service.ContactTracingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactTracingServiceImpl implements ContactTracingService {
    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private ContactTraceRepository contactTraceRepository;
    @Autowired
    private DataLocationRepository dataLocationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<ContactTrace> getContactTraces(Long userId, LocalDateTime start, LocalDateTime end) {
        List<Object[]> interactions = entryRepository.findUserInteractions(userId);
        List<ContactTrace> traces = new ArrayList<>();

        for (Object[] interaction : interactions) {
            Long userId1 = ((Number) interaction[0]).longValue();
            Long userId2 = ((Number) interaction[1]).longValue();
            Long timeDiff = ((Number) interaction[2]).longValue();
            Long dataCentreId = ((Number) interaction[3]).longValue();
            Timestamp timestamp1 = (Timestamp) interaction[4];
            LocalDateTime timestamp = timestamp1.toLocalDateTime();

            if (timestamp.isAfter(start) && timestamp.isBefore(end)) {
                ContactTrace trace = new ContactTrace();
                trace.setUser1(userRepository.findById(userId1).get());
                trace.setUser2(userRepository.findById(userId2).get());
                trace.setDataCentre(dataLocationRepository.findById(dataCentreId).get());
                trace.setTimeDiffSeconds(timeDiff);
                trace.setTimestamp(timestamp);
                traces.add(trace);

                // Notify AT_RISK users
                if (timeDiff <= 3600) { // 1-hour threshold
                    notificationService.sendNotification(userId2,
                            String.format("You were exposed to User-%d at %s on %s.",
                                    userId1, dataCentreId, timestamp));
                }
            }
        }

        contactTraceRepository.saveAll(traces);
        return traces;
    }

    @Override
    public List<Long> getShortestContactPath(Long startUserId, Long endUserId) {
        // Build an adjacency list from contact_traces
        Map<Long, List<Long>> graph = new HashMap<>();
        List<ContactTrace> traces = contactTraceRepository.findAll();
        for (ContactTrace trace : traces) {
            Long u1 = trace.getUser1().getId();
            Long u2 = trace.getUser2().getId();
            graph.computeIfAbsent(u1, k -> new ArrayList<>()).add(u2);
            graph.computeIfAbsent(u2, k -> new ArrayList<>()).add(u1);
        }

        // BFS for shortest path
        Queue<Long> queue = new LinkedList<>();
        Map<Long, Long> parent = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        queue.add(startUserId);
        visited.add(startUserId);

        while (!queue.isEmpty()) {
            Long current = queue.poll();
            if (current.equals(endUserId)) {
                break;
            }
            for (Long neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruct path
        List<Long> path = new ArrayList<>();
        Long current = endUserId;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path.isEmpty() || path.get(0) != startUserId ? Collections.emptyList() : path;
    }
}