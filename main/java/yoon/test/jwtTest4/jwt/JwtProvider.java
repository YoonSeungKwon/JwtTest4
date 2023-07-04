package yoon.test.jwtTest4.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yoon.test.jwtTest4.entity.Account;
import yoon.test.jwtTest4.service.AccountDetailService;
import yoon.test.jwtTest4.vo.response.AccountResponse;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final String SECRET_KEY = "madebyyoonseungkwon981217";
    private final Long exp = 10 * 60 * 1000L;

    private final AccountDetailService accountDetailService;

    public String createToken(AccountResponse accout){

        Claims claims = Jwts.claims();
        claims
                .setIssuer("Yoonsk")
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +exp));
        claims.put("idx", accout.getIdx());
        claims.put("email", accout.getEmail());
        claims.put("name", accout.getName());
        claims.put("role", accout.getRole());
        claims.put("regdate", String.valueOf(accout.getRegdate()));

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

        return (String)Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(token).getBody().get("email");
    }

    public boolean validateToken(String token){                             //Validate Check
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch(Exception e){
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request){                 //get Token From Request
        if(request.getHeader("authorization") != null ) {
            return request.getHeader("authorization");
        }
        return null;
    }
}
