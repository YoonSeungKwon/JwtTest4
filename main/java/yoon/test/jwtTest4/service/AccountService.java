package yoon.test.jwtTest4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import yoon.test.jwtTest4.config.AccountAutheticationProvider;
import yoon.test.jwtTest4.entity.Account;
import yoon.test.jwtTest4.enums.Role;
import yoon.test.jwtTest4.repository.AccountRepository;
import yoon.test.jwtTest4.vo.request.AccountDto;
import yoon.test.jwtTest4.vo.response.AccountResponse;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountAutheticationProvider autheticationProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    public Account findAccount(AccountDto dto){
        return accountRepository.findAccountByEmail(dto.getEmail());
    }


    public AccountResponse join(AccountDto dto) {
        Account account = Account.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .role(Role.USER)
                .build();

        accountRepository.save(account);

        return this.toResponse(account);
    }


    public AccountResponse toResponse(Account account){
        return new AccountResponse(
                account.getIdx(),
                account.getEmail(),
                account.getName(),
                account.getRole(),
                account.getRegdate()
        );
    }

    public AccountResponse login(AccountDto dto) {

        try{
            Authentication authentication = autheticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getEmail(), dto.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Account account = this.findAccount(dto);
            return this.toResponse(account);
        }catch(BadCredentialsException e){
            return null;
        }
    }

}
