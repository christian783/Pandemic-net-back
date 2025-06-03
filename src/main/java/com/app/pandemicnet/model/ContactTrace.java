package com.app.pandemicnet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_traces")
@Data
public class ContactTrace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id2", nullable = false)
    private User user2;

    @ManyToOne
    @JoinColumn(name = "data_centre_id", nullable = false)
    private DataLocation dataCentre;

    @Column(name = "time_diff_seconds", nullable = false)
    private Long timeDiffSeconds;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}