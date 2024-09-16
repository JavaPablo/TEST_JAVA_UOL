package com.uol.email_management_api.api.controllers.v2.swagger.folder;

import com.uol.email_management_api.application.dtos.response.FolderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FolderSwagger {

    @Operation(summary = "List folders in a mailbox", description = "Retrieves a list of folders within the specified mailbox.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of folders",
                    content = @Content(schema = @Schema(implementation = FolderResponse[].class),
                            examples = @ExampleObject(value = "[{\"idt\": 1, \"name\": \"INBOX\"}, {\"idt\": 2, \"name\": \"JUNK\"}, {\"idt\": 3, \"name\": \"SENT\"}]")
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid mailbox name",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"400\",\"message\":\"MailBox inválido\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<Page<FolderResponse>> listFolders(String mailbox, @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "sendAt,asc") String sort);
}
