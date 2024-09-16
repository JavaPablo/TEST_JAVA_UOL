package com.uol.email_management_api.api.controllers.v1.emailmessage;

import com.uol.email_management_api.api.controllers.v1.swagger.emailmessage.EmailMessageSwagger;
import com.uol.email_management_api.application.dtos.request.MessageReadRequest;
import com.uol.email_management_api.application.dtos.request.ReceiveMessageRequest;
import com.uol.email_management_api.application.dtos.request.SendMessageRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.MessageDetailsResponse;
import com.uol.email_management_api.application.dtos.response.MessagesFolderResponse;
import com.uol.email_management_api.application.services.EmailMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("emailMessageControllerV1")
@RequestMapping("/api/v1/mailboxes")
public class EmailMessageController implements EmailMessageSwagger {

    private final EmailMessageService emailMessageService;

    public EmailMessageController(EmailMessageService emailMessageService) {
        this.emailMessageService = emailMessageService;
    }

    @Override
    @PostMapping("/{mailbox}/send-message")
    public ResponseEntity<ApiResponse> sendMenssage(@PathVariable String mailbox,
                                                    @RequestBody @Valid SendMessageRequest request) {
        ApiResponse response = emailMessageService.sendMessage(mailbox, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{mailbox}/receive-message")
    public ResponseEntity<ApiResponse> receiveMessage(@PathVariable String mailbox,
                                                      @RequestBody @Valid ReceiveMessageRequest request) {
        ApiResponse response = emailMessageService.receiveMessage(mailbox, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{mailbox}/folders/{folderId}/messages/{messageId}/read")
    public ResponseEntity<ApiResponse> updateReadFieldInMessage(
            @PathVariable String mailbox,
            @PathVariable Long folderId,
            @PathVariable Long messageId,
            @RequestBody @Valid MessageReadRequest request) {
        ApiResponse response = emailMessageService.updateReadFieldInMessage(mailbox, folderId, messageId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{mailbox}/folders/{folderIdt}/messages")
    public ResponseEntity<List<MessagesFolderResponse>> listMessages(
            @PathVariable String mailbox,
            @PathVariable Long folderIdt) {
        List<MessagesFolderResponse> messages = emailMessageService.listMessagesByMailboxAndFolder(mailbox, folderIdt);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{mailbox}/folders/{folderIdt}/messages/{messageIdt}")
    public ResponseEntity<MessageDetailsResponse> getMessageDetails(
            @PathVariable String mailbox,
            @PathVariable Long folderIdt,
            @PathVariable Long messageIdt) {
        MessageDetailsResponse message = emailMessageService.getMessageDetails(mailbox, folderIdt, messageIdt);
        return ResponseEntity.ok(message);
    }

}
