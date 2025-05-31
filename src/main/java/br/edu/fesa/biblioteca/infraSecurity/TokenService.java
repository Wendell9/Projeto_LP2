/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.infraSecurity;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.JWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;


/**
 *
 * @author guind
 */
@Service
public class TokenService {
    @Value("${api.security.token}")
    private String secret;
    
    public String generateToken(Usuario user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            String token = JWT.create().withIssuer("login-auth-api").withSubject(user.getEmail()).withExpiresAt(this.generateExpirationDate()).sign(algorithm);
            return token;
        }
        catch(JWTCreationException exception)
                {
            throw new RuntimeException("Error while authenticating");
        }
    }
    
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api").build().verify(token).getSubject();
        }
        catch(JWTCreationException exception){
            return null;
        }
    }
    
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusSeconds(600).toInstant(ZoneOffset.of("-03:00"));
    }
}