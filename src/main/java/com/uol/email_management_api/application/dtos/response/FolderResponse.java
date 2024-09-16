package com.uol.email_management_api.application.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponse {
    @Schema(example = "10")
    private Long idt;

    @Schema(example = "teste@dominio.com")
    private String name;
}
