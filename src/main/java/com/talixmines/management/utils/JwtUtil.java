package com.talixmines.management.utils;

import com.talixmines.management.usermanager.model.auth.ExtendedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  @Value("${jwt.secretKey}")
  private String SECRET_KEY;
  @Value("${jwt.timeInMs}")
  private int MILLIS_TIME;
  @Value("${jwt.timeInHour}")
  private int HOUR_TIME;
  @Value("${jwt.timeInMinute}")
  private int MINUTE_TIME;
  @Value("${jwt.timeExpired}")
  private int TIME_EXPIRED;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public String extractIdEntreprise(String token) {
    final Claims claims = extractAllClaims(token);

    return claims.get("idEntreprise", String.class);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(ExtendedUser userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails);
  }

  private String createToken(Map<String, Object> claims, ExtendedUser userDetails) {

    return Jwts.builder().setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + (long) MILLIS_TIME * MINUTE_TIME * HOUR_TIME * TIME_EXPIRED))
        .claim("idEntreprise", userDetails.getIdEntreprise().toString())
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
