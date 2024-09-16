package com.uol.email_management_api.application.services;

import com.uol.email_management_api.application.constants.ApiConstantsMailBoxMessages;
import com.uol.email_management_api.application.constants.ApiConstantsStatus;
import com.uol.email_management_api.application.dtos.request.MailboxRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.MailboxNameResponse;
import com.uol.email_management_api.domain.enums.FoldersEnum;
import com.uol.email_management_api.domain.exceptions.InvalidRequestException;
import com.uol.email_management_api.domain.exceptions.ResourceAlreadyExistsException;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import com.uol.email_management_api.infrastructure.persistence.repositories.MailboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailboxServiceTest {
    private MailboxRepository mailboxRepository;
    private MailboxService mailboxService;

    @BeforeEach
    void setUp() {
        mailboxRepository = mock(MailboxRepository.class);
        mailboxService = new MailboxService(mailboxRepository);
    }

    @Test
    void testCreateMailbox_Success() {
        MailboxRequest request = new MailboxRequest();
        request.setName("test@mail.com");

        when(mailboxRepository.existsByName(request.getName())).thenReturn(false);

        ApiResponse response = mailboxService.createMailbox(request);

        assertEquals(ApiConstantsStatus.SUCCESS_CODE_01, response.getCode());
        assertEquals(ApiConstantsMailBoxMessages.SUCCESS_MESSAGE, response.getMessage());
        verify(mailboxRepository, times(1)).save(any(MailboxEntity.class));

        ArgumentCaptor<MailboxEntity> mailboxEntityCaptor = ArgumentCaptor.forClass(MailboxEntity.class);
        verify(mailboxRepository).save(mailboxEntityCaptor.capture());
        MailboxEntity savedMailbox = mailboxEntityCaptor.getValue();
        List<FolderEntity> folders = savedMailbox.getFolder();

        assertEquals(FoldersEnum.values().length, folders.size());
        for (FoldersEnum folder : FoldersEnum.values()) {
            assertTrue(folders.stream().anyMatch(f -> f.getName().equals(folder.getFolderName())));
        }
    }

    @Test
    void testCreateMailbox_InvalidEmail() {
        MailboxRequest request = new MailboxRequest();
        request.setName("invalid-email");

        Exception exception = assertThrows(InvalidRequestException.class, () -> mailboxService.createMailbox(request));

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_INVALID_NAME, exception.getMessage());
        verify(mailboxRepository, never()).save(any());
    }

    @Test
    void testCreateMailbox_AlreadyExists() {
        MailboxRequest request = new MailboxRequest();
        request.setName("test@mail.com");

        when(mailboxRepository.existsByName(request.getName())).thenReturn(true);

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> mailboxService.createMailbox(request));

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_ALREADY_EXISTS, exception.getMessage());
        verify(mailboxRepository, never()).save(any());
    }

    @Test
    void testListAllMailboxes_Success() {
        MailboxEntity mailbox1 = new MailboxEntity();
        mailbox1.setName("test1@mail.com");
        MailboxEntity mailbox2 = new MailboxEntity();
        mailbox2.setName("test2@mail.com");

        when(mailboxRepository.findAll()).thenReturn(Arrays.asList(mailbox1, mailbox2));

        List<MailboxNameResponse> response = mailboxService.listAllMailboxes();

        assertEquals(2, response.size());
        assertEquals("test1@mail.com", response.get(0).getName());
        assertEquals("test2@mail.com", response.get(1).getName());
    }

    @Test
    void testListAllMailboxes_NotFound() {
        when(mailboxRepository.findAll()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> mailboxService.listAllMailboxes());

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND_MAILBOXES, exception.getMessage());
    }

    @Test
    void testListAllMailboxesWithPagination_Success() {
        Pageable pageable = mock(Pageable.class);
        MailboxEntity mailbox = new MailboxEntity();
        mailbox.setName("test@mail.com");
        Page<MailboxEntity> page = new PageImpl<>(Collections.singletonList(mailbox), pageable, 1);

        when(mailboxRepository.findAll(pageable)).thenReturn(page);

        Page<MailboxNameResponse> response = mailboxService.listAllMailboxes(pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals("test@mail.com", response.getContent().get(0).getName());
    }

    @Test
    void testListAllMailboxesWithPagination_NotFound() {
        Pageable pageable = mock(Pageable.class);
        Page<MailboxEntity> page = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(mailboxRepository.findAll(pageable)).thenReturn(page);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> mailboxService.listAllMailboxes(pageable));

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND_MAILBOXES, exception.getMessage());
    }
}