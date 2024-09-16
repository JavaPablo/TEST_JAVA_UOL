package com.uol.email_management_api.application.utils;

import com.uol.email_management_api.application.constants.ApiConstantsEmailMessages;
import com.uol.email_management_api.application.constants.ApiConstantsFolderMessages;
import com.uol.email_management_api.application.constants.ApiConstantsMailBoxMessages;
import com.uol.email_management_api.domain.exceptions.ResourceNotFoundException;
import com.uol.email_management_api.infrastructure.persistence.entities.EmailMessageEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.FolderEntity;
import com.uol.email_management_api.infrastructure.persistence.entities.MailboxEntity;

import java.util.Optional;

public class OptionalUtil {

    public static <T> T getOrThrow(Optional<T> optional, String errorMessage) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }

    public static MailboxEntity getMailboxOrThrow(Optional<MailboxEntity> optional, String mailboxName) {
        return getOrThrow(optional, String.format("%s: %s", ApiConstantsMailBoxMessages.ERROR_MESSAGE_NOT_FOUND, mailboxName));
    }

    public static FolderEntity getFolderOrThrowById(Optional<FolderEntity> optional, Long folderId) {
        return getOrThrow(optional, String.format("%s: %s", ApiConstantsFolderMessages.ERROR_MESSAGE_NOT_FOUND, folderId));
    }

    public static EmailMessageEntity getMessageOrThrow(Optional<EmailMessageEntity> optional, Long messageId) {
        return getOrThrow(optional, String.format("%s: %s", ApiConstantsEmailMessages.ERROR_MESSAGE_NOT_FOUND, messageId));
    }

    public static FolderEntity getFolderOrThrow(Optional<FolderEntity> optional, String folderName) {
        return getOrThrow(optional, String.format("%s: %s", ApiConstantsFolderMessages.ERROR_MESSAGE_NOT_FOUND, folderName));
    }
}
