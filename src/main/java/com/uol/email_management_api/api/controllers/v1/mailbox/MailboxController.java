package com.uol.email_management_api.api.controllers.v1.mailbox;

import com.uol.email_management_api.api.controllers.v1.swagger.mailbox.MailboxSwagger;
import com.uol.email_management_api.application.dtos.request.MailboxRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.MailboxNameResponse;
import com.uol.email_management_api.application.services.MailboxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mailboxes")
public class MailboxController implements MailboxSwagger {
    private final MailboxService mailboxService;

    public MailboxController(MailboxService mailboxService) {
        this.mailboxService = mailboxService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse> createMailbox(@RequestBody @Valid MailboxRequest request) {
        ApiResponse response = mailboxService.createMailbox(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MailboxNameResponse>> listMailboxes() {
        List<MailboxNameResponse> response = mailboxService.listAllMailboxes();
        return ResponseEntity.ok(response);
    }
}
