package uz.pdp.apisecurityhrmanagement.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {

    @NotNull
    @Email
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String role;
}
