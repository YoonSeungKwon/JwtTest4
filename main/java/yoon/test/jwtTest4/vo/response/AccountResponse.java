package yoon.test.jwtTest4.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yoon.test.jwtTest4.enums.Role;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccountResponse {

    private long idx;

    private String email;

    private String name;

    private Role role;

    private LocalDateTime regdate;

}
