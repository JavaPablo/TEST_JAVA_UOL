package com.uol.email_management_api.api.controllers.v2.folder;

import com.uol.email_management_api.api.controllers.v2.swagger.folder.FolderSwagger;
import com.uol.email_management_api.application.dtos.response.FolderResponse;
import com.uol.email_management_api.application.services.FolderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("folderControllerV2")
@RequestMapping("/api/v2/mailboxes")
public class FolderController implements FolderSwagger {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/{mailbox}/folders")
    public ResponseEntity<Page<FolderResponse>>listFolders(
            @PathVariable String mailbox,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {
        String[] sortParams = sort.split(",");
        Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<FolderResponse> folders = folderService.listFoldersByMailbox(mailbox, pageable);
        return ResponseEntity.ok(folders);
    }
}
