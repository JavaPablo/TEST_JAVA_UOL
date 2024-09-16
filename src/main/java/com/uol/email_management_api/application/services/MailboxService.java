package com.uol.email_management_api.application.services;

import com.uol.email_management_api.application.constants.ApiConstantsMailBoxMessages;
import com.uol.email_management_api.application.constants.ApiConstantsStatus;
import com.uol.email_management_api.application.dtos.request.MailboxRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.MailboxNameResponse;
import com.uol.email_management_api.domain.enums.FoldersEnum;
import com.uol.email_management_api.domain.exceptions.InvalidRequestException;
import com.uol.email_management_api.domain.exceptions.ResourceAlreadyExistsException;
import com.uol.email_management_api.application.utils.ValidatorUtil;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import com.uol.email_management_api.infrastructure.persistence.repositories.MailboxRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MailboxService {
    private final MailboxRepository mailboxRepository;

    public MailboxService(MailboxRepository mailboxRepository) {
        this.mailboxRepository = mailboxRepository;
    }

    @Transactional
    public ApiResponse createMailbox(MailboxRequest request) {
        if (ValidatorUtil.isValidEmail(request.getName())) {
            throw new InvalidRequestException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_INVALID_NAME);
        }

        if (mailboxRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_ALREADY_EXISTS);
        }

        MailboxEntity mailboxEntity = MailboxEntity.builder()
                .name(request.getName())
                .build();

        mailboxEntity.setFolder(createDefaultFolders(mailboxEntity));
        mailboxRepository.save(mailboxEntity);

        return new ApiResponse(ApiConstantsStatus.SUCCESS_CODE_01, ApiConstantsMailBoxMessages.SUCCESS_MESSAGE);
    }

    List<FolderEntity> createDefaultFolders(MailboxEntity mailboxEntity) {
        return Stream.of(FoldersEnum.values())
                .map(folder -> FolderEntity.builder()
                        .name(folder.getFolderName())
                        .mailbox(mailboxEntity)
                        .build())
                .collect(Collectors.toList());
    }

    public List<MailboxNameResponse> listAllMailboxes() {
        List<MailboxEntity> mailboxes = mailboxRepository.findAll();

        if (mailboxes.isEmpty()) {
            throw new ResourceNotFoundException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND_MAILBOXES);
        }

        return mailboxes.stream()
                .map(mailbox -> new MailboxNameResponse(mailbox.getName()))
                .collect(Collectors.toList());
    }

    public Page<MailboxNameResponse> listAllMailboxes(Pageable pageable) {
        Page<MailboxEntity>  mailboxes = mailboxRepository.findAll(pageable);

        if (mailboxes.isEmpty()) {
            throw new ResourceNotFoundException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND_MAILBOXES);
        }

        return mailboxes.map(mailbox -> new MailboxNameResponse(mailbox.getName()));
    }
}
