package com.uol.email_management_api.api.controllers.v2.emailmessage;

import com.uol.email_management_api.api.controllers.v2.swagger.emailmessage.EmailMessageSwagger;
import com.uol.email_management_api.application.dtos.response.MessagesFolderResponse;
import com.uol.email_management_api.application.services.EmailMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController("emailMessageControllerV2")
@RequestMapping("/api/v2/mailboxes")
public class EmailMessageController implements EmailMessageSwagger {

    private final EmailMessageService emailMessageService;

    public EmailMessageController(EmailMessageService emailMessageService) {
        this.emailMessageService = emailMessageService;
    }

    @Override
    @GetMapping("/{mailbox}/folders/{folderIdt}/messages")
    public ResponseEntity<Page<MessagesFolderResponse>> listMessages(
            @PathVariable String mailbox,
            @PathVariable Long folderIdt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sendAt,asc") String sort) {
        String[] sortParams = sort.split(",");
        Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<MessagesFolderResponse> messages = emailMessageService.listMessagesByMailboxAndFolder(mailbox, folderIdt, pageable);
        return ResponseEntity.ok(messages);
    }
}
