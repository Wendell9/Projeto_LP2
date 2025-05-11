/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author guind
 */
@Service
public class AutenticacaoService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método de autenticação
    public boolean autenticarUsuario(String email, String senha) {
        try {
            // Tenta autenticar com o AuthenticationManager
            Authentication authentication = (Authentication) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
            );

            // Se o código chegar até aqui, a autenticação foi bem-sucedida
            SecurityContextHolder.getContext().setAuthentication((org.springframework.security.core.Authentication) authentication);
            return true;
        } catch (BadCredentialsException e) {
            // Senha incorreta
            return false;
        }
    }
}
