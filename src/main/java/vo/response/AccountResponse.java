package vo.response;

import enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountResponse {

    private long id;

    private String email;

    private String username;

    private Role role;
}
