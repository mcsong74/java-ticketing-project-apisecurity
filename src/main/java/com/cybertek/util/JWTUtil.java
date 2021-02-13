package com.cybertek.util;

import com.cybertek.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {
    @Value("${security.jwt.secret-key}") //in application.yml
    private String secret = "cybertek";

    //create payload and encoded Token String
    public String generateToken(User user){
        // generating payload for Token
        //in spring, payload called claims. only for the payload
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",user.getUserName());
        claims.put("id",user.getId());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        return createToken(claims,user.getUserName());

    }
    //create Token return encoded Token string
    private String createToken(Map<String, Object> claims, String username){
        //creating a token

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //10 hr
                .signWith(SignatureAlgorithm.HS256, secret).compact();

    }

    //decode a token return as Claims object
    private Claims extractAllClaims(String token){

        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    /*        ---  Decoding token ---         */
    //get a claim value from a token by claimsResolver Function in param
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //get user name from token
    public String extractUsername(String token){

        return extractClaim(token, Claims::getSubject);
    }

    //get expiration from token
    public  Date extractExpiration(String token){

        return extractClaim(token, Claims::getExpiration);
    }

    //checks token expired or not
    private Boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());
    }
    // check claims value matching db user's profile
    public Boolean validateToken(String token, UserDetails userDetails){
        final String currentUser = extractAllClaims(token).get("id").toString(); //.getId("id").toString;
        return (currentUser.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
