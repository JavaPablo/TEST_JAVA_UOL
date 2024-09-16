package com.uol.email_management_api.application.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FolderRequest {

    @NotBlank(message = "The name of the mass is mandatory.")
    @Schema(example = "arquivos-de-testes")
    private String name;
}
