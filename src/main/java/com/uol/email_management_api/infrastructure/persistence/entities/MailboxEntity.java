package com.uol.email_management_api.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mailbox")
public class MailboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idt")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "mailbox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolderEntity> folder;
}
