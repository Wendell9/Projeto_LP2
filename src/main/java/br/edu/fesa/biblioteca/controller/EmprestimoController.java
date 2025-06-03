/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.DTO.EmprestimoDTO;
import br.edu.fesa.biblioteca.DTO.EmprestimoJoin;
import br.edu.fesa.biblioteca.cadastro.model.Emprestimo;
import br.edu.fesa.biblioteca.cadastro.model.Item_Emprestimo;
import br.edu.fesa.biblioteca.cadastro.model.Livro;
import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.EmprestimoRepository;
import br.edu.fesa.biblioteca.repository.LivroRepository;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import java.sql.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import br.edu.fesa.biblioteca.repository.Item_EmprestimoRepository;
import br.edu.fesa.biblioteca.service.EmprestimoService;
import br.edu.fesa.biblioteca.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    EmprestimoService emprestimoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private Item_EmprestimoRepository ItemEmprestimoRepository;

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

    @GetMapping("/listaDeEmprestimos")
    public String listarEmprestimosGeral(Model model) {
        List<EmprestimoJoin> listaDeEmprestimos = emprestimoRepository.ListarEmprestimosDetalhados();

        model.addAttribute("listaEmprestimos", listaDeEmprestimos);
        return "Emprestimo/listaGeralEmprestimos";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesEmprestimo(@PathVariable UUID id, Model model) {
        EmprestimoJoin emprestimo = emprestimoRepository.emprestimoDetalhado(id);
        List<Item_Emprestimo> itens;
        List<Livro> livros = new ArrayList<>();
        itens = ItemEmprestimoRepository.findItensComLivroPorEmprestimoId(id);
        for (Item_Emprestimo item : itens) {
            Livro l = item.getLivro();
            l.setCapaEmbase64();
            livros.add(l);
        }

        model.addAttribute("livros", livros);
        model.addAttribute("emprestimo", emprestimo);

        return "Emprestimo/DetalhesEmprestimo";

    }

    @PreAuthorize("hasRole('USER')") 
    @GetMapping("/MeusDetalhesEmprestimo/{id}")
    public String MeusDetalhesEmprestimo(@PathVariable UUID id, Model model) {
        EmprestimoJoin emprestimo = emprestimoRepository.emprestimoDetalhado(id);
        List<Item_Emprestimo> itens;
        List<Livro> livros = new ArrayList<>();
        itens = ItemEmprestimoRepository.findItensComLivroPorEmprestimoId(id);
        for (Item_Emprestimo item : itens) {
            Livro l = item.getLivro();
            l.setCapaEmbase64();
            livros.add(l);
        }

        model.addAttribute("livros", livros);
        model.addAttribute("emprestimo", emprestimo);

        return "Emprestimo/DetalhesEmprestimo";

    }

    @GetMapping("/AtualizarStatus")
    public String AtualizarStatus() {
        emprestimoService.atualizarLivrosAtrasados();
        return "redirect:/Emprestimo/listaDeEmprestimos";
    }

    @PreAuthorize("hasRole('USER')") 
    @GetMapping("/MeusEmprestimos")
    public String MeusEmprestimos(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) throws UnsupportedEncodingException {
        try {

            String usuarioId = (String) request.getSession(false).getAttribute("usuarioId");
            UUID uuid = UUID.fromString(usuarioId);

            List<EmprestimoJoin> listaDeEmprestimos = emprestimoRepository.listarMeusEmprestimos(uuid);

            model.addAttribute("listaEmprestimos", listaDeEmprestimos);
            return "Emprestimo/MeusEmprestimos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", "ID de usuário inválido");
            return "redirect:/biblioteca-fesa";
        }
    }

    @GetMapping("/Devolver/{id}")
    public String devolverLivro(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Emprestimo emprestimoAlterado = emprestimoRepository.findById(id).orElse(null);
        if (emprestimoAlterado.getStatus().equals("Devolvido")) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível devolver um livro que ja foi devolvido");
            return "redirect:/Emprestimo/listaDeEmprestimos";
        }

        emprestimoAlterado.setStatus("Devolvido");
        emprestimoRepository.save(emprestimoAlterado);

        List<Item_Emprestimo> itens;
        itens = ItemEmprestimoRepository.findItensComLivroPorEmprestimoId(id);
        for (Item_Emprestimo item : itens) {
            Livro l = item.getLivro();
            livroRepository.aumentarQuantidadeDisponivel(l.getId());
        }

        Usuario usuario = usuarioRepository.findById(emprestimoAlterado.getUsuario().getId()).orElse(null);

        if (usuario != null) {
            if (!usuario.getStatus()) {
                usuario.setStatus(true);
                usuarioRepository.save(usuario);
            }
        }

        return "redirect:/Emprestimo/listaDeEmprestimos";
    }

    @PostMapping("/realizar")
    public ResponseEntity<String> realizarEmprestimo(@RequestBody EmprestimoDTO emprestimoDTO, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findByEmail(emprestimoDTO.getEmail()).orElseThrow();

        if (!usuario.getStatus()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Usuário bloqueado. Favor devolver os empréstimos em atraso!");
        }

        List<Livro> livros = livroRepository.findAllById(emprestimoDTO.getLivros());

        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setUsuario(usuario);
        emprestimo.setData_emprestimo(Date.valueOf(LocalDate.now()));
        emprestimo.setData_prevista_devolucao(Date.valueOf(LocalDate.now().plusDays(7)));
        emprestimo.setStatus("Emprestado");

        emprestimoRepository.save(emprestimo);

        for (Livro livro : livros) {
            Item_Emprestimo itemEmprestimo = new Item_Emprestimo();
            itemEmprestimo.setEmprestimo(emprestimo);
            itemEmprestimo.setLivro(livro);

            ItemEmprestimoRepository.save(itemEmprestimo);
            livroRepository.diminuirQuantidadeDisponivel(livro.getId());
        }

        return ResponseEntity.ok("Empréstimo realizado com sucesso!");
    }

}
