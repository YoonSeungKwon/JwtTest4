package yoon.test.jwtTest4.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import yoon.test.jwtTest4.entity.Account;
import yoon.test.jwtTest4.service.AccountDetailService;
import yoon.test.jwtTest4.vo.response.AccountResponse;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final String SECRET_KEY = "madebyyoonseungkwon981217";
    private final Long acc = 60 * 60 * 1000L;
    private final Long ref = 2 * 7 * 24 * 60 * 60 * 1000L;

    private final AccountDetailService accountDetailService;

    public Claims accessTokenClaim(AccountResponse account){
        Claims claims = Jwts.claims();
        claims
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + acc));
        claims.put("idx", account.getIdx());
        claims.put("email", account.getEmail());
        claims.put("name", account.getName());
        claims.put("role", account.getRole());
        claims.put("regdate", String.valueOf(account.getRegdate()));
        return claims;
    }

    public Claims refreshTokenClaim(){
        Claims claims = Jwts.claims();
        claims
                .setSubject("RefreshToken")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ref));
        return claims;
    }

    public String createToken(Claims claims){
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

    public Integer getIdx(String token){                                     //get Id From Token

        return (Integer) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("idx");
    }

    public boolean validateToken(String token){                             //Validate Check
        try{
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        }catch(Exception e){
            return false;
        }
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("X-Refresh-Token");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
