package com.mohirdev.mohirdevlesson.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private long tokenValidateMillisecondRemember;
    private static final String auth="auth";
    private long tokenValidateMilliseconds;
    private final Key key;

    private final JwtParser jwtParser;

    public TokenProvider() {
        byte[] keyByte;
        String secret = "YWhyb3JudXJpZGRpbm92MjYzMDExaHVzbmlkZGlubydnJ2xpZGFzdHVyY2hpdGFsYWJhdmFob3ppcnRpbXVyYmlsYW5uaXJrdmFydGlyYWRhY2hpcmNoaXFkYXlhc2hheWFwdGl5YXFpbmRhaXRhbGl5YXZpemFkYW5hdGthem9sZGlrZXlpbmdpeWlsaHVkb2hvaGxhc2FjaGV0ZWxnYWtldGFkaQ==";
        keyByte= Decoders.BASE64.decode(secret);
        key= Keys.hmacShaKeyFor(keyByte);
        jwtParser=Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidateMilliseconds=1000*3600;
        this.tokenValidateMillisecondRemember=1000*86400;
    }

    public String createToken(Authentication authentication, boolean rememberMe) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now=(new Date()).getTime();
        Date validate;
        if (rememberMe){
            validate = new Date(now+tokenValidateMillisecondRemember);
        }else {
            validate= new Date(now+tokenValidateMilliseconds);
        }

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(auth,authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validate)
                .compact();
    }

    public boolean validateToken(String jwt) {

        try {
            jwtParser.parseClaimsJws(jwt);
            return true;
        }catch (ExpiredJwtException e){
            logger.error("ExpiredJwtException");
        }catch (UnsupportedJwtException e){
            logger.error("UnsupportedJwtException");
        }catch (MalformedJwtException m){
            logger.error("MalformedJwtException");
        }catch (SignatureException s){
            logger.error("SignatureException");
        }catch (IllegalArgumentException i){
            logger.error("IllegalArgumentException");
        }
        return false;
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = jwtParser.parseClaimsJws(jwt).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(auth).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principle = new User(claims.getSubject(),"",authorities);
        return new UsernamePasswordAuthenticationToken(principle,jwt,authorities);
    }
}
