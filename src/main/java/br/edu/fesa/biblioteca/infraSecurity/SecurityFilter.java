/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.infraSecurity;

/**
 *
 * @author guind
 */
import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import br.edu.fesa.biblioteca.service.CookieService;
import static br.edu.fesa.biblioteca.service.CookieService.getCookie;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;
    @Autowired
    UsuarioRepository userRepository;
    @Autowired
    CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println(">>> SecurityFilter URI: '" + request.getRequestURI() + "'");

        // Ignora as rotas públicas
        if (path.equals("/auth/login") || path.equals("/auth/register") || path.equals("/Usuario/cadastro") || path.startsWith("/images/") || path.equals("/biblioteca-fesa/")
                || path.equals("/biblioteca-fesa") || path.equals("/biblioteca-fesa/login") || path.equals("/Usuario/cadastrar")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if (login != null) {
            Usuario user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found"));

            List<GrantedAuthority> authorities; // Declarada fora do if/else

            if (user.isAdmin()) {
                authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) throws UnsupportedEncodingException {
        var tolkien = this.cookieService.getCookie(request, "token");
        return tolkien;
    }
}
