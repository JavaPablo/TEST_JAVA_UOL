package com.uol.email_management_api.application.services;

import com.uol.email_management_api.application.constants.ApiConstantsFolderMessages;
import com.uol.email_management_api.application.constants.ApiConstantsMailBoxMessages;
import com.uol.email_management_api.application.constants.ApiConstantsStatus;
import com.uol.email_management_api.application.dtos.request.FolderRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.FolderResponse;
import com.uol.email_management_api.domain.exceptions.InvalidRequestException;
import com.uol.email_management_api.domain.exceptions.ResourceAlreadyExistsException;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import com.uol.email_management_api.infrastructure.persistence.repositories.FolderRepository;
import com.uol.email_management_api.infrastructure.persistence.repositories.MailboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private MailboxRepository mailboxRepository;

    @InjectMocks
    private FolderService folderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFolder_Success() {
        String mailboxName = "test@example.com";
        FolderRequest request = new FolderRequest();
        request.setName("Inbox");

        MailboxEntity mailboxEntity = new MailboxEntity();
        mailboxEntity.setName(mailboxName);

        Mockito.when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailboxEntity));
        Mockito.when(folderRepository.existsByNameAndMailbox(request.getName(), mailboxEntity)).thenReturn(false);

        ApiResponse response = folderService.createFolder(mailboxName, request);

        assertEquals(ApiConstantsStatus.SUCCESS_CODE_01, response.getCode());
        assertEquals(ApiConstantsFolderMessages.SUCCESS_MESSAGE, response.getMessage());
        Mockito.verify(folderRepository, times(1)).save(Mockito.any(FolderEntity.class));
    }

    @Test
    public void testCreateFolder_MailboxNotFound() {
        String mailboxName = "test@example.com";
        FolderRequest request = new FolderRequest();
        request.setName("Inbox");

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            folderService.createFolder(mailboxName, request);
        });

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testCreateFolder_FolderAlreadyExists() {
        String mailboxName = "test@example.com";
        FolderRequest request = new FolderRequest();
        request.setName("Inbox");

        MailboxEntity mailboxEntity = new MailboxEntity();
        mailboxEntity.setName(mailboxName);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailboxEntity));
        when(folderRepository.existsByNameAndMailbox(request.getName(), mailboxEntity)).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            folderService.createFolder(mailboxName, request);
        });

        assertEquals(ApiConstantsFolderMessages.ERROR_MESSAGE_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    public void testCreateFolder_InvalidFolderName() {
        String mailboxName = "test@example.com";
        FolderRequest request = new FolderRequest();
        request.setName("Invalid@Folder");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            folderService.createFolder(mailboxName, request);
        });

        assertEquals(ApiConstantsFolderMessages.ERROR_MESSAGE_INVALID_NAME, exception.getMessage());
    }

    @Test
    public void testListFoldersByMailbox_Success() {
        String mailboxName = "test@example.com";

        MailboxEntity mailboxEntity = new MailboxEntity();
        mailboxEntity.setName(mailboxName);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setId(1L);
        folderEntity.setName("Inbox");

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailboxEntity));
        when(folderRepository.findByMailboxId(mailboxEntity.getId())).thenReturn(List.of(folderEntity));

        List<FolderResponse> folders = folderService.listFoldersByMailbox(mailboxName);

        assertNotNull(folders);
        assertEquals(1, folders.size());
        assertEquals("Inbox", folders.get(0).getName());
    }

    @Test
    public void testListFoldersByMailbox_MailboxNotFound() {
        String mailboxName = "test@example.com";

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            folderService.listFoldersByMailbox(mailboxName);
        });

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testListFoldersByMailbox_NoFoldersFound() {
        String mailboxName = "test@example.com";

        MailboxEntity mailboxEntity = new MailboxEntity();
        mailboxEntity.setName(mailboxName);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailboxEntity));
        when(folderRepository.findByMailboxId(mailboxEntity.getId())).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            folderService.listFoldersByMailbox(mailboxName);
        });

        assertEquals(ApiConstantsFolderMessages.ERROR_MESSAGE_NOT_FOUND_FOLDERS, exception.getMessage());
    }

    @Test
    public void testListFoldersByMailboxWithPagination_Success() {
        String mailboxName = "test@example.com";
        Pageable pageable = (Pageable) PageRequest.of(0, 10);

        MailboxEntity mailboxEntity = new MailboxEntity();
        mailboxEntity.setName(mailboxName);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setId(1L);
        folderEntity.setName("Inbox");

        Page<FolderEntity> pageFolders = new PageImpl<>(List.of(folderEntity), pageable, 1);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailboxEntity));
        when(folderRepository.findByMailboxId(mailboxEntity.getId(), pageable)).thenReturn(pageFolders);

        Page<FolderResponse> folders = folderService.listFoldersByMailbox(mailboxName, pageable);

        assertNotNull(folders);
        assertEquals(1, folders.getTotalElements());
        assertEquals("Inbox", folders.getContent().get(0).getName());
    }

    @Test
    public void testListFoldersByMailboxWithPagination_MailboxNotFound() {
        String mailboxName = "test@example.com";
        Pageable pageable = (Pageable) PageRequest.of(0, 10);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            folderService.listFoldersByMailbox(mailboxName, pageable);
        });

        assertEquals(ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testListFoldersByMailboxWithPagination_NoFoldersFound() {
        String mailboxName = "test@example.com";
        Pageable pageable = (Pageable) PageRequest.of(0, 10);

        MailboxEntity mailboxEntity = new MailboxEntity();
        mailboxEntity.setName(mailboxName);

        Page<FolderEntity> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailboxEntity));
        when(folderRepository.findByMailboxId(mailboxEntity.getId(), pageable)).thenReturn(emptyPage);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            folderService.listFoldersByMailbox(mailboxName, pageable);
        });

        assertEquals(ApiConstantsFolderMessages.ERROR_MESSAGE_NOT_FOUND_FOLDERS, exception.getMessage());
    }
}