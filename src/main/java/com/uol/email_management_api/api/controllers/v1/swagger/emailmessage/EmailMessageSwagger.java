package com.uol.email_management_api.api.controllers.v1.swagger.emailmessage;

import com.uol.email_management_api.application.dtos.request.MessageReadRequest;
import com.uol.email_management_api.application.dtos.request.ReceiveMessageRequest;
import com.uol.email_management_api.application.dtos.request.SendMessageRequest;
import com.uol.email_management_api.application.dtos.response.MessageDetailsResponse;
import com.uol.email_management_api.application.dtos.response.MessagesFolderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmailMessageSwagger {

    @Operation(summary = "Send an email message", description = "Sends an email message from the specified mailbox to the recipient. Both the mailbox name and the recipient's email must be in valid email format.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"201\",\"message\":\"Mensagem armazenada\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid recipient email",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"400\",\"message\":\"Destinatário e ou Assunto inválido\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<?> sendMenssage(String mailbox, SendMessageRequest request);

    @Operation(summary = "Receive an email message", description = "Receives an email message sent to the specified mailbox. The sender's email must be in valid email format.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message received successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"201\",\"message\":\"Mensagem armazenada\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid sender email",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"400\",\"message\":\"Destinatário inválido\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<?> receiveMessage(String mailbox, ReceiveMessageRequest request);

    @Operation(summary = "Mark email message as read", description = "Marks a specific email message as read in a particular folder within the mailbox.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message read status updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"204\",\"message\":\"Mensagem atualizada\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox or folder does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox, Pasta e ou Mensagem não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<?> updateReadFieldInMessage(String mailbox, Long folderId, Long messageId, MessageReadRequest request);

    @Operation(summary = "List messages in a folder", description = "Lists all messages in the specified mailbox and folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message read status updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"200\",\"message\":\"Messages retrieved successfully\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid emailbox",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"400\",\"message\":\"MailBox inválido\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox or folder does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox e ou Folder não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<List<MessagesFolderResponse>> listMessages(String mailbox, Long folderId);

    @Operation(summary = "Get details of a specific email message",
            description = "Retrieves the details of a specific email message in the specified mailbox and folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message details retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MessageDetailsResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"200\",\"message\":\"Mensagem recuperada com sucesso\",\"timestamp\":\"2024-09-15T22:13:59.078Z\",\"data\":{\"idt\":10,\"sender\":\"teste@dominio.com\",\"recipient\":\"teste@dominio.com\",\"subject\":\"Assunto de testado\",\"body\":\"Corpo da mensagem\",\"sent_at\":\"2023-01-02 15:04:05\",\"read\":\"true\"}}") // Exemplo de resposta com dados
                    )),
            @ApiResponse(responseCode = "404", description = "Mailbox or folder or message does not exist",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"404\",\"message\":\"Mailbox, Pasta e ou Mensagem não existe\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"code\":\"500\",\"message\":\"Internal server error\",\"timestamp\":\"2024-09-15T22:13:59.078Z\"}")
                    ))
    })
    ResponseEntity<MessageDetailsResponse> getMessageDetails(String mailbox, Long folderId, Long messageId);
}
