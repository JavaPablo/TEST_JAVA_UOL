package com.uol.email_management_api.infrastructure.persistence.repositories;

import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailboxRepository extends JpaRepository<MailboxEntity, Long> {
    Optional<MailboxEntity> findByName(String name);
    boolean existsByName(String name);
}
