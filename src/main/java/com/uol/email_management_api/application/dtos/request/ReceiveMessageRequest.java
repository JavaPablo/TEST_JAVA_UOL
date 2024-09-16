package com.uol.email_management_api.application.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ReceiveMessageRequest {
//    @NotBlank(message = "Sender is required")
//    @Email(message = "Sender must be a valid email address")
    @Schema(example = "teste@dominio.com")
    private String sender;

    @Size(max = 50, message = "Subject must not exceed 50 characters")
    @Schema(example = "Test subject")
    private String subject;

    @Schema(example = "Test Email Body")
    private String body;

    @Schema(example = "JUNK")
    private String folder;
}
