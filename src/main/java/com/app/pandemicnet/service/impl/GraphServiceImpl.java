package com.app.pandemicnet.service.impl;

import com.app.pandemicnet.dto.GraphResponse;
import com.app.pandemicnet.dto.Node;
import com.app.pandemicnet.dto.Edge;
import com.app.pandemicnet.model.User;
import com.app.pandemicnet.repository.EntryRepository;
import com.app.pandemicnet.repository.UserRepository;
import com.app.pandemicnet.service.GraphService;
import com.app.pandemicnet.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public GraphResponse getAtRiskGraph() {
        List<User> atRiskUsers = userRepository.findByStatusIn(
                Arrays.asList(User.Status.INFECTED, User.Status.AT_RISK));
        Set<Long> userIds = atRiskUsers.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        List<Node> nodes = atRiskUsers.stream()
                .map(user -> {
                    Node node = new Node();
                    node.setId(user.getId());
                    node.setLabel("User-" + user.getId());
                    node.setStatus(User.Status.valueOf(user.getStatus().name()));
                    return node;
                })
                .collect(Collectors.toList());

        List<Edge> edges = new ArrayList<>();
        Set<String> edgeKeys = new HashSet<>();
        for (Long userId : userIds) {
            List<Object[]> interactions = entryRepository.findUserInteractions(userId);
            for (Object[] interaction : interactions) {
                Long userId1 = ((Number) interaction[0]).longValue();
                Long userId2 = ((Number) interaction[1]).longValue();
                double timeDiff = ((Number) interaction[2]).doubleValue();

                if (userIds.contains(userId2)) {
                    String edgeKey = userId1 < userId2 ? userId1 + "-" + userId2 : userId2 + "-" + userId1;
                    if (!edgeKeys.contains(edgeKey)) {
                        Edge edge = new Edge();
                        edge.setFrom(userId1);
                        edge.setTo(userId2);
                        edge.setWeight(timeDiff);
                        edges.add(edge);
                        edgeKeys.add(edgeKey);

                        markAtRiskIfNecessary(userId2, userId1, timeDiff);
                    }
                }
            }
        }

        GraphResponse graph = new GraphResponse();
        graph.setNodes(nodes);
        graph.setEdges(edges);
        return graph;
    }

    private void markAtRiskIfNecessary(Long userId, Long infectedUserId, double timeDiff) {
        if (timeDiff <= 3600) {
            userRepository.findById(userId).ifPresent(user -> {
                if (user.getStatus() == User.Status.HEALTHY) {
                    user.setStatus(User.Status.AT_RISK);
                    userRepository.save(user);
                    notificationService.sendNotification(userId,
                            String.format("You are at risk due to contact with User-%d within %d minutes.",
                                    infectedUserId, (int) (timeDiff / 60)));
                }
            });
        }
    }
}