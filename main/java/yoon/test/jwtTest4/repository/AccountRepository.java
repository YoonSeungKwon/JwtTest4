package yoon.test.jwtTest4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.test.jwtTest4.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByEmail(String email);

}
