package com.uol.email_management_api.application.utils;

import com.uol.email_management_api.application.constants.ApiConstantsEmailMessages;
import com.uol.email_management_api.domain.exceptions.InvalidRequestException;

import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern FOLDER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]{1,100}$");
    private static final int SUBJECT_MAX_LENGTH = 50;

    public static boolean isValidEmail(String email) {
        return email == null || !EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidFolderName(String name) {
        return name != null && FOLDER_NAME_PATTERN.matcher(name).matches();
    }

    public static void validateSubject(String subject) {
        if (subject != null && subject.length() > SUBJECT_MAX_LENGTH) {
            throw new InvalidRequestException(ApiConstantsEmailMessages.ERROR_MESSAGE_INVALID_SUBJECT);
        }
    }

    public static void validateEmail(String email) {
        if (ValidatorUtil.isValidEmail(email)) {
            throw new InvalidRequestException(ApiConstantsEmailMessages.ERROR_MESSAGE_INVALID_MAILBOX);
        }
    }

    public static void validateEmailRecipient(String email) {
        if (ValidatorUtil.isValidEmail(email)) {
            throw new InvalidRequestException(ApiConstantsEmailMessages.ERROR_MESSAGE_INVALID_RECIPIENT);
        }
    }

    public static void validateEmailSender(String email) {
        if (ValidatorUtil.isValidEmail(email)) {
            throw new InvalidRequestException(ApiConstantsEmailMessages.ERROR_MESSAGE_INVALID_SENDER);
        }
    }
}
