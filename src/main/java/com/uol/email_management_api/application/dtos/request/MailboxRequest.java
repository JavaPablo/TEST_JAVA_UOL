package com.uol.email_management_api.application.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailboxRequest {

    @NotBlank(message = "The name of the mass is mandatory.")
    @Email(message = "Box name must be a valid email")
    @Schema(example = "teste@dominio.com")
    private String name;
}
