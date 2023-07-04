package yoon.test.jwtTest4.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yoon.test.jwtTest4.service.AccountDetailService;
import yoon.test.jwtTest4.vo.response.AccountResponse;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final String SECRET_KEY = "madebyyoonseungkwon981217";
    private final Long exp = 10 * 60 * 1000L;

    private final AccountDetailService accountDetailService;

    public String createToken(AccountResponse account){

        Claims claims = Jwts.claims();
        claims
                .setIssuer("Yoonsk")
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +exp));
        claims.put("idx", account.getIdx());
        claims.put("email", account.getEmail());
        claims.put("name", account.getName());
        claims.put("role", account.getRole());
        claims.put("regdate", String.valueOf(account.getRegdate()));

        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JSON")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();

        return jwt;
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = accountDetailService.loadUserByUsername(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getId(String token){                                     //get Id From Token

        return (String)Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("email");
    }

    public boolean validateToken(String token){                             //Validate Check
        try{
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        }catch(Exception e){
            return false;
        }
    }
}
