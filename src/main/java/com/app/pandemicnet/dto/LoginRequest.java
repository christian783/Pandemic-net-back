package com.app.pandemicnet.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequest {
    @NotEmpty(message = "email field should not be empty")
    private String email;
    @NotEmpty(message = "password field should not be empty")
    private String password;

}