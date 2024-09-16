package com.uol.email_management_api.api.controllers.v1.folder;

import com.uol.email_management_api.api.controllers.v1.swagger.folder.FolderSwagger;
import com.uol.email_management_api.application.dtos.request.FolderRequest;
import com.uol.email_management_api.application.dtos.response.ApiResponse;
import com.uol.email_management_api.application.dtos.response.FolderResponse;
import com.uol.email_management_api.application.services.FolderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mailboxes")
public class FolderController implements FolderSwagger {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @Override
    @PostMapping("/{mailbox}/folders")
    public ResponseEntity<ApiResponse> createFolder(@PathVariable String mailbox,
                                                    @Valid @RequestBody FolderRequest request) {
        ApiResponse response = folderService.createFolder(mailbox, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{mailbox}/folders")
    public ResponseEntity<List<FolderResponse>> listFolders(
            @PathVariable String mailbox) {
        List<FolderResponse> folders = folderService.listFoldersByMailbox(mailbox);
        return ResponseEntity.ok(folders);
    }
}
