package com.uol.email_management_api.application.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageReadRequest {

//    @NotNull(message = "The read status is required")
//    @NotBlank(message = "Read is required")
    @Schema(example = "true")
    private Boolean read;
}
