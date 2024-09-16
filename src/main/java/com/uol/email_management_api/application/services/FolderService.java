package com.uol.email_management_api.application.services;

import com.uol.email_management_api.application.constants.ApiConstantsFolderMessages;
import com.uol.email_management_api.application.constants.ApiConstantsMailBoxMessages;
import com.uol.email_management_api.application.constants.ApiConstantsStatus;
import com.uol.email_management_api.application.dtos.request.FolderRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.FolderResponse;
import com.uol.email_management_api.application.utils.ValidatorUtil;
import com.uol.email_management_api.domain.exceptions.InvalidRequestException;
import com.uol.email_management_api.domain.exceptions.ResourceAlreadyExistsException;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import com.uol.email_management_api.infrastructure.persistence.repositories.FolderRepository;
import com.uol.email_management_api.infrastructure.persistence.repositories.MailboxRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final MailboxRepository mailboxRepository;

    public FolderService(FolderRepository folderRepository, MailboxRepository mailboxRepository) {
        this.folderRepository = folderRepository;
        this.mailboxRepository = mailboxRepository;
    }

    @Transactional
    public ApiResponse createFolder(String mailboxName, FolderRequest request) {
        ValidatorUtil.validateEmail(mailboxName);
        if (!ValidatorUtil.isValidFolderName(request.getName())) {
            throw new InvalidRequestException(ApiConstantsFolderMessages.ERROR_MESSAGE_INVALID_NAME);
        }

        MailboxEntity mailbox = mailboxRepository.findByName(mailboxName)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND));

        if (folderRepository.existsByNameAndMailbox(request.getName(), mailbox)) {
            throw new ResourceAlreadyExistsException(ApiConstantsFolderMessages.ERROR_MESSAGE_ALREADY_EXISTS);
        }

        FolderEntity folderEntity = FolderEntity.builder()
                .name(request.getName())
                .mailbox(mailbox)
                .build();

        folderRepository.save(folderEntity);

        return new ApiResponse(ApiConstantsStatus.SUCCESS_CODE_01, ApiConstantsFolderMessages.SUCCESS_MESSAGE);
    }

    public List<FolderResponse> listFoldersByMailbox(String mailboxName) {
        ValidatorUtil.validateEmail(mailboxName);
        MailboxEntity mailbox = mailboxRepository.findByName(mailboxName)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND));

        List<FolderEntity> folders = folderRepository.findByMailboxId(mailbox.getId());

        if(folders.isEmpty()) {
            throw new ResourceNotFoundException(ApiConstantsFolderMessages.ERROR_MESSAGE_NOT_FOUND_FOLDERS);
        }

        return folders.stream()
                .map(folder -> new FolderResponse(folder.getId(), folder.getName()))
                .collect(Collectors.toList());
    }

    public Page<FolderResponse> listFoldersByMailbox(String mailboxName, Pageable pageable) {
        ValidatorUtil.validateEmail(mailboxName);
        MailboxEntity mailbox = mailboxRepository.findByName(mailboxName)
                .orElseThrow(() -> new ResourceNotFoundException(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND));

        Page<FolderEntity> folders = folderRepository.findByMailboxId(mailbox.getId(), pageable);

        if (folders.isEmpty()) {
            throw new ResourceNotFoundException(ApiConstantsFolderMessages.ERROR_MESSAGE_NOT_FOUND_FOLDERS);
        }

        return folders.map(folder -> new FolderResponse(folder.getId(), folder.getName()));
    }

}
