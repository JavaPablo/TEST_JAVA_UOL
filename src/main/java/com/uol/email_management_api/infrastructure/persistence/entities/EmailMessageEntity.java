package com.uol.email_management_api.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "message")
public class EmailMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt")
    private Long id;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "read", nullable = false)
    private Boolean read;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    @ManyToOne
    @JoinColumn(name = "folder_idt", nullable = false)
    private FolderEntity folder;

    public EmailMessageEntity() {}
}
