package com.skillcanvas.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Response",
        description = "Schema to represent Response details"
)
@Data @AllArgsConstructor
public class ResponseDto {

    @Schema(
            description = "Status Code of the Response"
    )
    private String statusCode;

    @Schema(
            description = "Status Message of the Response"
    )
    private String statusMsg;
}
