package com.uol.email_management_api.api.controllers.v2.swagger.mailbox;

import com.uol.email_management_api.application.dtos.response.MailboxNameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MailboxSwagger {

    @Operation(summary = "List all mailboxes", description = "Retrieves a list of all mailboxes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of mailboxes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MailboxNameResponse.class,
                                    type = "array"))
            ),
            @ApiResponse(responseCode = "404", description = "Does not exist mailboxes",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Não existe mailboxes\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<Page<MailboxNameResponse>> listMailboxes(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "name,asc") String sort);
}
