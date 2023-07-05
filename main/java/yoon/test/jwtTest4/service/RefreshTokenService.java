package yoon.test.jwtTest4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoon.test.jwtTest4.entity.Account;
import yoon.test.jwtTest4.entity.RefreshToken;
import yoon.test.jwtTest4.jwt.JwtProvider;
import yoon.test.jwtTest4.repository.AccountRepository;
import yoon.test.jwtTest4.repository.RefreshTokenRepository;
import yoon.test.jwtTest4.vo.request.AccountDto;
import yoon.test.jwtTest4.vo.response.AccountResponse;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    public void saveToken(AccountDto dto, String token){

        Account account = accountService.findAccount(dto);

        RefreshToken refToken = RefreshToken.builder()
                .account(account)
                .token(token)
                .build();

        try{
           RefreshToken savedToken = tokenRepository.findRefreshTokenByAccountIdx(account.getIdx());
           if(savedToken != null){
               savedToken.setToken(token);
               tokenRepository.save(savedToken);
               return;
           }
        }catch (Exception e){
        }
        tokenRepository.save(refToken);
    }

    public boolean matchToken(String accToken, String refToken){
        try {
            RefreshToken savedToken = tokenRepository.findRefreshTokenByAccountIdx((long) jwtProvider.getIdx(accToken));
            if(savedToken.getToken().equals(refToken))
                return true;
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public String reToken(String accToken){

        Account account = accountRepository.findAccountByEmail(jwtProvider.getId(accToken));
        AccountResponse res = accountService.toResponse(account);
        return jwtProvider.createToken(jwtProvider.accessTokenClaim(res));
    }
}
