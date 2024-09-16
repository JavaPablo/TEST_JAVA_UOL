package com.uol.email_management_api.api.controllers.v2.swagger.emailmessage;

import com.uol.email_management_api.application.dtos.response.MessagesFolderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


public interface EmailMessageSwagger {

    @Operation(summary = "List messages in a folder",
            description = "Lists all messages in the specified mailbox and folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MessagesFolderResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"200\",\"message\":\"Messages retrieved successfully\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid mailbox",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"400\",\"message\":\"Mailbox inválido\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}"))),
            @ApiResponse(responseCode = "404", description = "Mailbox or folder does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox e ou Folder não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")))
    })
    ResponseEntity<Page<MessagesFolderResponse>> listMessages(
            @PathVariable String mailbox,
            @PathVariable Long folderIdt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sendAt,asc") String sort);

}
