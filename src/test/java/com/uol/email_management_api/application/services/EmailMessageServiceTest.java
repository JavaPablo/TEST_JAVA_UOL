package com.uol.email_management_api.application.services;

import com.uol.email_management_api.application.constants.ApiConstantsEmailMessages;
import com.uol.email_management_api.application.constants.ApiConstantsStatus;
import com.uol.email_management_api.application.dtos.request.MessageReadRequest;
import com.uol.email_management_api.application.dtos.request.ReceiveMessageRequest;
import com.uol.email_management_api.application.dtos.request.SendMessageRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.MessageDetailsResponse;
import com.uol.email_management_api.application.dtos.response.MessagesFolderResponse;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.EmailMessageEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import com.uol.email_management_api.infrastructure.persistence.repositories.EmailMessageRepository;
import com.uol.email_management_api.infrastructure.persistence.repositories.FolderRepository;
import com.uol.email_management_api.infrastructure.persistence.repositories.MailboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class EmailMessageServiceTest {

    @InjectMocks
    private EmailMessageService emailMessageService;

    @Mock
    private EmailMessageRepository emailMessageRepository;

    @Mock
    private MailboxRepository mailboxRepository;

    @Mock
    private FolderRepository folderRepository;

    private MailboxEntity mailbox;
    private FolderEntity folder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mailbox = new MailboxEntity();
        mailbox.setName("test@mail.com");
        folder = new FolderEntity();
        folder.setId(1L);
        folder.setName("INBOX");
        folder.setMailbox(mailbox);
        mailbox.setFolder(Collections.singletonList(folder));
    }

    @Test
    void testSendMessageSuccess() {
        SendMessageRequest request = new SendMessageRequest();
        request.setRecipient("recipient@mail.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        Mockito.when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        Mockito.when(folderRepository.findByNameAndMailbox("SENT", mailbox)).thenReturn(Optional.of(new FolderEntity()));

        ApiResponse response = emailMessageService.sendMessage("test@mail.com", request);

        assertEquals(ApiConstantsStatus.SUCCESS_CODE_01, response.getCode());
        assertEquals(ApiConstantsEmailMessages.SUCCESS_MESSAGE, response.getMessage());
        Mockito.verify(emailMessageRepository, times(1)).save(Mockito.any(EmailMessageEntity.class));
    }

    @Test
    void testReceiveMessageSuccess() {
        ReceiveMessageRequest request = new ReceiveMessageRequest();
        request.setSender("sender@mail.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByNameAndMailbox("INBOX", mailbox)).thenReturn(Optional.of(folder));

        ApiResponse response = emailMessageService.receiveMessage("test@mail.com", request);

        assertEquals(ApiConstantsStatus.SUCCESS_CODE_01, response.getCode());
        assertEquals(ApiConstantsEmailMessages.SUCCESS_MESSAGE, response.getMessage());
        Mockito.verify(emailMessageRepository, times(1)).save(Mockito.any(EmailMessageEntity.class));
    }

    @Test
    void testUpdateReadFieldInMessageSuccess() {
        Long messageId = 1L;
        MessageReadRequest request = new MessageReadRequest();
        request.setRead(true);

        EmailMessageEntity message = new EmailMessageEntity();
        message.setId(messageId);
        message.setRead(false);
        folder.setMessage(Collections.singletonList(message));

        when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folder.getId(), mailbox)).thenReturn(Optional.of(folder));
        when(emailMessageRepository.findByIdAndFolder(messageId, folder)).thenReturn(Optional.of(message));

        ApiResponse response = emailMessageService.updateReadFieldInMessage("test@mail.com", folder.getId(), messageId, request);

        assertEquals(ApiConstantsStatus.SUCCESS_CODE_04, response.getCode());
        assertEquals(ApiConstantsEmailMessages.SUCCESS_MESSAGE, response.getMessage());
        assertTrue(message.getRead());
        Mockito.verify(emailMessageRepository, times(1)).save(Mockito.any(EmailMessageEntity.class));
    }

    @Test
    void testListMessagesByMailboxAndFolderSuccess() {
        EmailMessageEntity message = new EmailMessageEntity();
        message.setId(1L);
        message.setSender("sender@mail.com");
        message.setSubject("Test Subject");
        message.setSendAt(LocalDateTime.now());
        message.setRead(false);

        when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folder.getId(), mailbox)).thenReturn(Optional.of(folder));
        when(emailMessageRepository.findByFolder_Id(folder.getId())).thenReturn(Arrays.asList(message));

        List<MessagesFolderResponse> response = emailMessageService.listMessagesByMailboxAndFolder("test@mail.com", folder.getId());

        assertEquals(1, ((List<?>) response).size());
        assertEquals(message.getId(), response.get(0).getIdt());
        assertEquals(message.getSender(), response.get(0).getSender());
    }

    @Test
    void testGetMessageDetailsSuccess() {
        Long messageId = 1L;
        EmailMessageEntity message = new EmailMessageEntity();
        message.setId(messageId);
        message.setSender("sender@mail.com");
        message.setRecipient("recipient@mail.com");
        message.setSubject("Test Subject");
        message.setBody("Test Body");
        message.setSendAt(LocalDateTime.now());
        message.setRead(false);

        when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folder.getId(), mailbox)).thenReturn(Optional.of(folder));
        when(emailMessageRepository.findByIdAndFolder(messageId, folder)).thenReturn(Optional.of(message));

        MessageDetailsResponse response = emailMessageService.getMessageDetails("test@mail.com", folder.getId(), messageId);

        assertEquals(message.getId(), response.getIdt());
        assertEquals(message.getSender(), response.getSender());
        assertEquals(message.getRecipient(), response.getRecipient());
    }

    @Test
    void testSendMessageInvalidMailbox() {
        SendMessageRequest request = new SendMessageRequest();
        request.setRecipient("recipient@mail.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        when(mailboxRepository.findByName("invalid@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailMessageService.sendMessage("invalid@mail.com", request);
        });
    }

    @Test
    void testUpdateReadFieldInMessageNotFound() {
        Long messageId = 1L;
        MessageReadRequest request = new MessageReadRequest();
        request.setRead(true);

        when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folder.getId(), mailbox)).thenReturn(Optional.of(folder));
        when(emailMessageRepository.findByIdAndFolder(messageId, folder)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailMessageService.updateReadFieldInMessage("test@mail.com", folder.getId(), messageId, request);
        });

    }

    @Test
    void testGetMessageDetailsNotFound() {
        Long messageId = 1L;

        when(mailboxRepository.findByName("test@mail.com")).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folder.getId(), mailbox)).thenReturn(Optional.of(folder));
        when(emailMessageRepository.findByIdAndFolder(messageId, folder)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            emailMessageService.getMessageDetails("test@mail.com", folder.getId(), messageId);
        });

    }

    @Test
    void testListMessagesByMailboxAndFolderInvalidMailbox() {
        String mailboxName = "invalid@mail.com";
        Long folderIdt = 1L;
        Pageable pageable = Pageable.ofSize(10);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            emailMessageService.listMessagesByMailboxAndFolder(mailboxName, folderIdt, pageable);
        });
    }

    @Test
    void testListMessagesByMailboxAndFolderInvalidFolder() {
        String mailboxName = "valid@mail.com";
        Long folderIdt = 1L;
        Pageable pageable = Pageable.ofSize(10);

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folderIdt, mailbox)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            emailMessageService.listMessagesByMailboxAndFolder(mailboxName, folderIdt, pageable);
        });
    }

    @Test
    void testListMessagesByMailboxAndFolderEmptyMessages() {
        String mailboxName = "valid@mail.com";
        Long folderIdt = 1L;
        Pageable pageable = Pageable.ofSize(10);
        Page<EmailMessageEntity> messagesPage = new PageImpl<>(new ArrayList<>());

        when(mailboxRepository.findByName(mailboxName)).thenReturn(Optional.of(mailbox));
        when(folderRepository.findByIdAndMailbox(folderIdt, mailbox)).thenReturn(Optional.of(folder));
        when(emailMessageRepository.findByFolder_Id(folder.getId(), pageable)).thenReturn(messagesPage);

        Page<MessagesFolderResponse> response = emailMessageService.listMessagesByMailboxAndFolder(mailboxName, folderIdt, pageable);

        assertEquals(0, response.getTotalElements());
    }
}