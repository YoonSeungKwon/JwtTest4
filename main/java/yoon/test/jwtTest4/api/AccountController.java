package yoon.test.jwtTest4.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.test.jwtTest4.enums.StatusEnum;
import yoon.test.jwtTest4.jwt.JwtProvider;
import yoon.test.jwtTest4.service.AccountService;
import yoon.test.jwtTest4.service.RefreshTokenService;
import yoon.test.jwtTest4.vo.request.AccountDto;
import yoon.test.jwtTest4.vo.response.AccountResponse;
import yoon.test.jwtTest4.vo.response.Message;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;
    private final RefreshTokenService tokenService;
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

        String accToken = jwtProvider.createToken(jwtProvider.accessTokenClaim(accountResponse));
        String refToken = jwtProvider.createToken(jwtProvider.refreshTokenClaim());

        tokenService.saveToken(dto,refToken);

        res.setHeader("Authorization", "Bearer " + accToken);
        res.setHeader("X-Refresh-Token", "Bearer " + refToken);
        return ResponseEntity.ok(accountResponse);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> loginAccount(HttpServletRequest req, HttpServletResponse res){

        String refToken = jwtProvider.resolveRefreshToken(req);
        String accToken = jwtProvider.resolveAccessToken(req);

        if(refToken!= null && jwtProvider.validateToken(refToken)){
            System.out.println("hi");
            if(tokenService.matchToken(accToken, refToken)) {
                System.out.println("hi");
                String token = tokenService.reToken(accToken);
                res.setHeader("Authorization", "Bearer " + token);
                return ResponseEntity.ok().body(token);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/auth")
    public ResponseEntity<Authentication> auth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth);
    }

}
