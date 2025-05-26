/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.DTO.EmprestimoDTO;
import br.edu.fesa.biblioteca.cadastro.model.Emprestimo;
import br.edu.fesa.biblioteca.cadastro.model.Livro;
import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.EmprestimoRepository;
import br.edu.fesa.biblioteca.repository.LivroRepository;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author guind
 */
@Controller
@RequestMapping("/Emprestimo")
@PreAuthorize("hasRole('ADMIN')")
public class EmprestimoController {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired 
    private EmprestimoRepository emprestimoRepository;

    @GetMapping("/buscarUsuario")
    @ResponseBody
    public List<Usuario> buscarUsuarios(@RequestParam String email) {
        return usuarioRepository.findByEmailContaining(email);
    }

    @GetMapping("/home")
    public String home() {
        return "Emprestimo/home";
    }

    @GetMapping("/carregaDadosUsuario")
    public String carregarDadosUsuario(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        usuario.setImagemEmbase64();

        List<Livro> livros = livroRepository.findByQuantidadeDisponivelGreaterThan(0).stream()
                .sorted((l1, l2) -> l1.getTitulo().compareToIgnoreCase(l2.getTitulo()))
                .peek(livro -> livro.setCapaEmbase64()) // <<< Aqui você seta a imagem base64
                .collect(Collectors.toList());

        model.addAttribute("livros", livros);
        model.addAttribute("usuario", usuario);
        return "Emprestimo/detalhesUsuario :: detalhesUsuario"; // Retorna apenas o trecho necessário
    }

    @PostMapping("/realizar")
    public ResponseEntity<String> realizarEmprestimo(@RequestBody EmprestimoDTO emprestimoDTO) {
        Usuario usuario = usuarioRepository.findByEmail(emprestimoDTO.getEmail()).orElseThrow();
        List<Livro> livros = livroRepository.findAllById(emprestimoDTO.getLivros());
        
        Emprestimo emprestimo = new Emprestimo();
        
        emprestimo.setId_usuario(usuario.getId());
        emprestimo.setData_emprestimo(Date.valueOf(LocalDate.now()));
        emprestimo.setData_prevista_devolucao(Date.valueOf(LocalDate.now().plusDays(7)));
        
        emprestimoRepository.save(emprestimo);
        
        return ResponseEntity.ok("Empréstimo realizado com sucesso!");
    }
}
