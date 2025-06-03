package br.edu.fesa.biblioteca.infraSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible paths (no authentication required)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/Usuario/cadastrar").permitAll()
                        .requestMatchers("/h2-console").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/Usuario/cadastro").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/biblioteca-fesa/").permitAll()
                        .requestMatchers("/biblioteca-fesa").permitAll()
                        .requestMatchers("/biblioteca-fesa/login").permitAll()
                        // Paths accessible by ADMIN role only
                        .requestMatchers("/Livro/cadastro").hasRole("ADMIN")
                        .requestMatchers("/Usuario/lista").hasRole("ADMIN")
                        .requestMatchers("/Livro/dashboard").hasRole("ADMIN")
                        .requestMatchers("/Livro/listarLivro").hasRole("ADMIN") // Make this ADMIN only as well for consistency based on previous changes
                        .requestMatchers("/Livro/editar/**").hasRole("ADMIN") // Specific path, ensure it's before general rules
                        .requestMatchers("/Livro/remover/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/Livro/atualizar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/Livro/excluir/**").hasRole("ADMIN")
                        .requestMatchers("/Emprestimo/home").hasRole("ADMIN")
                        .requestMatchers("/Emprestimo/listaDeEmprestimos").hasRole("ADMIN")
                        .requestMatchers("/Emprestimo/detalhes/**").hasRole("ADMIN")
                        .requestMatchers("/Emprestimo/AtualizarStatus").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/Emprestimo/realizar").hasRole("ADMIN")
                        .requestMatchers("/Emprestimo/Devolver/**").hasRole("ADMIN")


                        // Paths accessible by any authenticated user (USER or ADMIN)
                        .requestMatchers("/home").authenticated() // Home page requires authentication
                        .requestMatchers("/home/detalhes/**").authenticated() // Details page requires authentication
                        .requestMatchers("/Usuario/Meu_usuario").authenticated()
                        .requestMatchers("/Usuario/editar/**").authenticated()
                        .requestMatchers("/Usuario/remover/**").authenticated() // User can remove self
                        .requestMatchers(HttpMethod.POST, "/Usuario/atualizar/**").authenticated()
                        .requestMatchers("/Emprestimo/MeusEmprestimos").authenticated()
                        .requestMatchers("/Emprestimo/MeusDetalhesEmprestimo/**").authenticated()


                        // All other requests must be authenticated (catch-all)
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) // Permite iframes da mesma origem
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}