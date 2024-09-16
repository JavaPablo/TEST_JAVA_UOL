package com.uol.email_management_api.api.controllers.v1.swagger.folder;

import com.uol.email_management_api.application.dtos.request.FolderRequest;
import com.uol.email_management_api.application.dtos.response.FolderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FolderSwagger {

    @Operation(summary = "Create a new folder", description = "Creates a new folder in the specified mailbox. Folders must have unique names within the same mailbox and can only contain letters (a-z, A-Z), hyphens (-), and underscores (_), limited to 100 characters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Folder created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"201\",\"message\":\"Pasta criada com sucesso\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "409", description = "Folder already exists",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"409\",\"message\":\"Pasta já existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid folder name",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"400\",\"message\":\"Pasta inválida\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<?> createFolder(String mailbox, FolderRequest request);

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
    ResponseEntity<List<FolderResponse>> listFolders(String mailbox);
}
