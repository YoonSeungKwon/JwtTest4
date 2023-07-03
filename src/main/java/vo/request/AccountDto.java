package vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDto {

    private String email;

    private String username;

    private String password;
}
