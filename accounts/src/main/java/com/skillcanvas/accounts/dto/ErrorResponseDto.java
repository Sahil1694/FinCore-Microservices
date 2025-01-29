package com.skillcanvas.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Schema to represent Error Response details"
)
@Data @AllArgsConstructor
public class ErrorResponseDto {

    @Schema(
            description = "API Path of the Request",
            example = "/api/create"
    )
    private String apiPath;

    @Schema(
            description = "HTTP Status Code of the Response",
            example = "500"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error Message of the Response",
            example = "Internal Server Error"
    )
    private String errorMessage;

    @Schema(
            description = "Error Time of the Response",
            example = "2021-09-01T12:00:00"
    )
    private LocalDateTime errorTime;

}
