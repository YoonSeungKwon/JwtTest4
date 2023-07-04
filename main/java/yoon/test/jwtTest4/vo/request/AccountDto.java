package yoon.test.jwtTest4.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDto {

    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;

}
