package br.edu.fesa.biblioteca.service;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario save(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));;

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER") // Você pode definir roles específicas aqui
                .build();
    }

    public boolean autenticarUsuario(String email, String senha) {
        UserDetails userDetails = loadUserByUsername(email);
        return passwordEncoder.matches(senha, userDetails.getPassword());
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(UUID id) {
        Optional<Usuario> user = usuarioRepository.findById(id);
        user.ifPresent(p -> p.setSenha(""));
        return usuarioRepository.findById(id);
    }

    public Usuario update(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public void deleteById(UUID id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario login(String email, String senha) {
        if (autenticarUsuario(email, senha)) {
            return usuarioRepository.login(email);
        }
        return null;
    }

}
