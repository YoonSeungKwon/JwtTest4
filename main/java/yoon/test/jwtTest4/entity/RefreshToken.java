package yoon.test.jwtTest4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="refresh_token")
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @OneToOne
    @JoinColumn(name="account_token")
    private Account account;

    @Column(nullable = false)
    private String token;

    public RefreshToken(Account account, String token){
        this.account = account;
        this.token = token;
    }

}
