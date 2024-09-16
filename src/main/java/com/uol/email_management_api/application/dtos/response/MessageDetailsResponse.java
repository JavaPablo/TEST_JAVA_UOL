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
public class MessageDetailsResponse {

    @Schema(example = "10")
    private Long idt;

    @Schema(example = "teste@dominio.com")
    private String sender;

    @Schema(example = "teste@dominio.com")
    private String recipient;

    @Schema(example = "Assunto de testado")
    private String subject;

    @Schema(example = "Corpo da mensagem")
    private String body;

    @Schema(example = "2023-01-02 15:04:05")
    private String sent_at;

    @Schema(example = "true")
    private String read;
}
