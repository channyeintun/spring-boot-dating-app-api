package com.pledge.app.config.jwt;

import com.pledge.app.entity.Role;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {


    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtExpiration}")
    private long jwtExpiration;

    @Value("${app.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    public Map<String, Object> createToken(String username, List<Role> roles) {
        log.info("createToken is invoked");
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("authorities",
                roles.stream()
                        .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                        .filter(Objects::nonNull)
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
        );
        log.info("authorities added to claims");
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpiration);
        Date refreshTokenValidity = new Date(now.getTime() + refreshTokenExpiration);
        Map<String, Object> tokens = new HashMap<>();
        String accessToken = buildToken(claims, now, validity);
        String refreshToken = buildToken(claims, now, refreshTokenValidity);
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("expireAt", validity.getTime());

        log.info("token is created");
        return tokens;
    }

    public String buildToken(Claims claims, Date issue, Date validity) {
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(issue)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())//
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        Claims claims = getClaimsFromJWT(token);
        String username = getUsername(token);
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, "", authorities);
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public Claims getClaimsFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

}
