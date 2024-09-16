package com.uol.email_management_api.api.controllers.v2.mailbox;

import com.uol.email_management_api.api.controllers.v2.swagger.mailbox.MailboxSwagger;
import com.uol.email_management_api.application.dtos.response.MailboxNameResponse;
import com.uol.email_management_api.application.services.MailboxService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("mailboxControllerV2")
@RequestMapping("/api/v2/mailboxes")
public class MailboxController implements MailboxSwagger {
    private final MailboxService mailboxService;

    public MailboxController(MailboxService mailboxService) {
        this.mailboxService = mailboxService;
    }

    @GetMapping
    public ResponseEntity<Page<MailboxNameResponse>>listMailboxes(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "name,asc") String sort) {
        String[] sortParams = sort.split(",");
        Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<MailboxNameResponse> response = mailboxService.listAllMailboxes(pageable);
        return ResponseEntity.ok(response);
    }
}
