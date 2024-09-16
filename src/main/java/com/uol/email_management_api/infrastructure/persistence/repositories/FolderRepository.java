package com.uol.email_management_api.infrastructure.persistence.repositories;

import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    boolean existsByNameAndMailbox(String name, MailboxEntity mailbox);
    Optional<FolderEntity> findByNameAndMailbox(String name, MailboxEntity mailbox);
    Optional<FolderEntity> findByIdAndMailbox(Long id, MailboxEntity mailbox);
    List<FolderEntity> findByMailboxId(Long mailboxId);
    Page<FolderEntity> findByMailboxId(Long mailboxId, Pageable pageable);
}