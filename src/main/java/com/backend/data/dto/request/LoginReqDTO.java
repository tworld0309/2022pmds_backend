package com.backend.data.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Data
public class LoginReqDTO {
    @NotNull()
    @Size(min = 2)
    @Email
    private String email;

    @NotNull()
    @Size(min = 8)
    private String password;
}
