package yoon.test.jwtTest4.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import yoon.test.jwtTest4.service.AccountDetailService;

@Component
@RequiredArgsConstructor
public class AccountAutheticationProvider implements AuthenticationProvider {

    private final AccountDetailService accountDetailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        UserDetails account = accountDetailService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password,account.getPassword())){
            throw new UsernameNotFoundException("비밀번호가 일치하지 않습니다");
        }

        return new UsernamePasswordAuthenticationToken(username, password, account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
