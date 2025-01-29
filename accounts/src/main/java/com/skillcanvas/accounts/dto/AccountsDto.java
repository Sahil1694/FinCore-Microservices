package com.skillcanvas.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to represent Account details"
)
public class AccountsDto {

    @Schema(
            description = "Account number of the Customer"
    )
    @NotEmpty(message = "Account number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Account number should be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account type of the Customer",
            example = "Savings"
    )
    @NotEmpty(message = "Account type is mandatory")
    private String accountType;

    @Schema(
            description = "Branch address of the Customer"
    )
    @NotEmpty(message = "Branch address is mandatory")
    private String branchAddress;

}
