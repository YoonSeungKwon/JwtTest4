package service;

import entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repository.AccountRepository;
import vo.request.AccountDto;
import vo.response.AccountResponse;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountResponse join(AccountDto dto){
        if(dto.getEmail() == null || dto.getUsername()==null || dto.getPassword() == null){
            return null;
        }
        Account account = Account.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
        accountRepository.save(account);
        return new AccountResponse(account.getId(), account.getEmail(), account.getUsername(), account.getRole());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
        }
        return account;
    }
}
