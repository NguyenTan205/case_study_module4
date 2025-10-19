package com.codegym.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordForm {
    @NotBlank(message = "{register.username.notblank}")
    private String oldPassword;

    @NotBlank(message = "{register.username.notblank}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "{register.password.invalid}")
    private String newPassword;

    @NotBlank(message = "{register.username.notblank}")
    private String confirmPassword;
}
