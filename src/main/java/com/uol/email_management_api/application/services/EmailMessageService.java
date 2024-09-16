package com.uol.email_management_api.application.services;

import com.uol.email_management_api.application.constants.ApiConstantsEmailMessages;
import com.uol.email_management_api.application.constants.ApiConstantsStatus;
import com.uol.email_management_api.application.dtos.request.MessageReadRequest;
import com.uol.email_management_api.application.dtos.request.ReceiveMessageRequest;
import com.uol.email_management_api.application.dtos.request.SendMessageRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.MessageDetailsResponse;
import com.uol.email_management_api.application.dtos.response.MessagesFolderResponse;
import com.uol.email_management_api.application.utils.DateUtil;
import com.uol.email_management_api.application.utils.OptionalUtil;
import com.uol.email_management_api.application.utils.ValidatorUtil;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.EmailMessageEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;
import com.uol.email_management_api.infrastructure.persistence.repositories.EmailMessageRepository;
import com.uol.email_management_api.infrastructure.persistence.repositories.FolderRepository;
import com.uol.email_management_api.infrastructure.persistence.repositories.MailboxRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailMessageService {
    private static final String SENT_FOLDER_NAME = "SENT";
    private static final String INBOX_FOLDER_NAME = "INBOX";

    private final EmailMessageRepository emailMessageRepository;
    private final MailboxRepository mailboxRepository;
    private final FolderRepository folderRepository;

    public EmailMessageService(EmailMessageRepository emailMessageRepository,
                               MailboxRepository mailboxRepository,
                               FolderRepository folderRepository) {
        this.emailMessageRepository = emailMessageRepository;
        this.mailboxRepository = mailboxRepository;
        this.folderRepository = folderRepository;
    }

    @Transactional
    public ApiResponse sendMessage(String mailboxName, SendMessageRequest request) {
        ValidatorUtil.validateEmailSender(mailboxName);
        ValidatorUtil.validateEmailRecipient(request.getRecipient());

        MailboxEntity mailbox = getMailboxOrThrow(mailboxName);
        FolderEntity sentFolder = getFolderOrThrow(SENT_FOLDER_NAME, mailbox);

        EmailMessageEntity emailMessage = createEmailMessage(mailboxName, request.getRecipient(),
                request.getSubject(), request.getBody(), sentFolder, true);

        emailMessageRepository.save(emailMessage);

        return new ApiResponse(ApiConstantsStatus.SUCCESS_CODE_01, ApiConstantsEmailMessages.SUCCESS_MESSAGE);
    }

    @Transactional
    public ApiResponse receiveMessage(String mailboxName, ReceiveMessageRequest request) {
        ValidatorUtil.validateEmailRecipient(mailboxName);
        ValidatorUtil.validateEmailSender(request.getSender());

        MailboxEntity mailbox = getMailboxOrThrow(mailboxName);
        FolderEntity folder = getFolderOrThrow(request.getFolder() != null ? request.getFolder() : INBOX_FOLDER_NAME, mailbox);

        EmailMessageEntity emailMessage = createEmailMessage(request.getSender(), mailboxName,
                request.getSubject(), request.getBody(), folder, false);

        emailMessageRepository.save(emailMessage);

        return new ApiResponse(ApiConstantsStatus.SUCCESS_CODE_01, ApiConstantsEmailMessages.SUCCESS_MESSAGE);
    }

    @Transactional
    public ApiResponse updateReadFieldInMessage(String mailboxName, Long folderIdt, Long messageIdt, MessageReadRequest request) {
        MailboxEntity mailbox = getMailboxOrThrow(mailboxName);
        FolderEntity folder = getFolderOrThrowById(folderIdt, mailbox);
        EmailMessageEntity message = getMessageOrThrow(messageIdt, folder);

        if (request.getRead() == null) {
            throw new ResourceNotFoundException(ApiConstantsEmailMessages.ERROR_MESSAGE_NOT_FOUND_READ);
        }

        message.setRead(request.getRead());
        emailMessageRepository.save(message);

        return new ApiResponse(ApiConstantsStatus.SUCCESS_CODE_04, ApiConstantsEmailMessages.SUCCESS_MESSAGE);
    }

    public List<MessagesFolderResponse> listMessagesByMailboxAndFolder(String mailboxName, Long folderIdt) {
        ValidatorUtil.validateEmail(mailboxName);
        MailboxEntity mailbox = getMailboxOrThrow(mailboxName);
        FolderEntity folder = getFolderOrThrowById(folderIdt, mailbox);

        List<EmailMessageEntity> messages = emailMessageRepository.findByFolder_Id(folder.getId());

        return convertToMessagesFolderResponse(messages);
    }

    public Page<MessagesFolderResponse> listMessagesByMailboxAndFolder(String mailboxName, Long folderIdt, Pageable pageable) {
        ValidatorUtil.validateEmail(mailboxName);
        MailboxEntity mailbox = getMailboxOrThrow(mailboxName);
        FolderEntity folder = getFolderOrThrowById(folderIdt, mailbox);

        Page<EmailMessageEntity> messagesPage = emailMessageRepository.findByFolder_Id(folder.getId(), pageable);

        return messagesPage.map(this::convertToMessagesFolderResponse);
    }

    private MessagesFolderResponse convertToMessagesFolderResponse(EmailMessageEntity message) {
        return new MessagesFolderResponse(
                message.getId(),
                message.getSender(),
                message.getSubject(),
                DateUtil.formatDate(message.getSendAt()),
                Boolean.toString(message.getRead())
        );
    }

    private List<MessagesFolderResponse> convertToMessagesFolderResponse(List<EmailMessageEntity> messages) {
        return messages.stream()
                .map(this::convertToMessagesFolderResponse)
                .collect(Collectors.toList());
    }

    public MessageDetailsResponse getMessageDetails(String mailboxName, Long folderIdt, Long messageIdt) {
        ValidatorUtil.validateEmail(mailboxName);
        MailboxEntity mailbox = getMailboxOrThrow(mailboxName);
        FolderEntity folder = getFolderOrThrowById(folderIdt, mailbox);
        EmailMessageEntity message = getMessageOrThrow(messageIdt, folder);

        return new MessageDetailsResponse(
                message.getId(),
                message.getSender(),
                message.getRecipient(),
                message.getSubject(),
                message.getBody(),
                DateUtil.formatDate(message.getSendAt()),
                Boolean.toString(message.getRead())
        );
    }

    private MailboxEntity getMailboxOrThrow(String mailboxName) {
        return OptionalUtil.getMailboxOrThrow(mailboxRepository.findByName(mailboxName), mailboxName);
    }

    private FolderEntity getFolderOrThrowById(Long folderIdt, MailboxEntity mailbox) {
        return OptionalUtil.getFolderOrThrowById(folderRepository.findByIdAndMailbox(folderIdt, mailbox), folderIdt);
    }

    private EmailMessageEntity getMessageOrThrow(Long messageIdt, FolderEntity folder) {
        return OptionalUtil.getMessageOrThrow(emailMessageRepository.findByIdAndFolder(messageIdt, folder), messageIdt);
    }

    private FolderEntity getFolderOrThrow(String folderName, MailboxEntity mailbox) {
        return OptionalUtil.getFolderOrThrow(folderRepository.findByNameAndMailbox(folderName, mailbox), folderName);
    }

    private EmailMessageEntity createEmailMessage(String sender, String recipient, String subject, String body,
                                                  FolderEntity folder, boolean isSent) {
        ValidatorUtil.validateSubject(subject);

        return EmailMessageEntity.builder()
                .sender(sender)
                .recipient(recipient)
                .subject(subject)
                .body(body)
                .read(isSent)
                .sendAt(getCurrentTime())
                .folder(folder)
                .build();
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}