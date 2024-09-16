package com.uol.email_management_api.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoldersEnum {

    INBOX("INBOX"),
    SENT("SENT"),
    JUNK("JUNK");

    private final String folderName;
}
