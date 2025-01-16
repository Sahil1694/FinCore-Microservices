package com.skillcanvas.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to represent Customer & Account details"
)
public class CustomerDto {

    @Schema(
            description = "Name of the Customer",
            example = "Sahil Khilari"
    )
    @Size(min = 5, max = 30, message = "Name should have atleast 5 characters")
    @NotEmpty(message = "Name is mandatory")
    private String name;

    @Schema(
            description = "Email of the Customer",
            example = "Sahil@gmail.com"
    )
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is mandatory")
    private String email;

    @Schema(
            description = "Mobile number of the Customer",
            example = "9876543210"
    )
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Accounts Details of the Customer"
    )
    private AccountsDto accountsDto;
}
