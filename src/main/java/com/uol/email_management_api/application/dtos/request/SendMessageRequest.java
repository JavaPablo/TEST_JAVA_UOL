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
public class SendMessageRequest {

//    @NotBlank(message = "Recipient is required")
//    @Email(message = "The recipient must be a valid email")
    @Schema(example = "teste@dominio.com")
    private String recipient;

    @Size(max = 50, message = "The subject must have a maximum of 50 characters")
    @Schema(example = "Test subject")
    private String subject;

    @Schema(example = "Test Email Body")
    private String body;
}
