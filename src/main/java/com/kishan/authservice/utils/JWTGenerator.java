package com.kishan.authservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

public class JWTGenerator {
    private static final String ISSUER = "Kishan";
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static String generateJwtForUsernameAndRole(String username, List<String> roles, Date expiry) {

        return Jwts.builder()
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(expiry)
                .claim("username", username)
                .claim("roles", roles)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims parseJwtToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parser()
                .requireIssuer(ISSUER)
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwtToken);

        return claims.getPayload();
    }
}
