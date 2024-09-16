package com.uol.email_management_api.infrastructure.persistence.repositories;

import com.uol.email_management_api.infrastructure.persistence.entities.EmailMessageEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessageEntity, Long> {
    Optional<EmailMessageEntity> findByIdAndFolder(Long id, FolderEntity folder);
    List<EmailMessageEntity> findByFolder_Id(Long folderIdt);
    Page<EmailMessageEntity> findByFolder_Id(Long folderId, Pageable pageable);
}
