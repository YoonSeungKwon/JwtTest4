package yoon.test.jwtTest4.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import yoon.test.jwtTest4.enums.StatusEnum;
import yoon.test.jwtTest4.jwt.JwtProvider;
import yoon.test.jwtTest4.service.AccountService;
import yoon.test.jwtTest4.vo.request.AccountDto;
import yoon.test.jwtTest4.vo.response.AccountResponse;
import yoon.test.jwtTest4.vo.response.Message;

import java.security.Principal;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;
    private final Validator validator;
    private final JwtProvider jwtProvider;


    @PostMapping("/join")
    public ResponseEntity<Message> joinAccount(@Validated AccountDto dto){
        Message message = new Message();
        AccountResponse accountRes = accountService.join(dto);

        message.setStatus(StatusEnum.OK);
        message.setMessage("회원가입 완료");
        message.setData(accountRes);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountResponse> loginAccount(@Validated AccountDto dto, HttpServletResponse res){
        AccountResponse accountResponse = accountService.login(dto);
        if(accountResponse == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String jwt = jwtProvider.createToken(accountResponse);
        res.setHeader("authorization", jwt);
        return ResponseEntity.ok(accountResponse);
    }

    @GetMapping("/auth")
    public ResponseEntity<Authentication> auth(HttpServletRequest req){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth);
    }
}
