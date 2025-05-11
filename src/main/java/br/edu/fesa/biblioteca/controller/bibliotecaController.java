/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.service.CookieService;
import br.edu.fesa.biblioteca.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author guind
 */
@Controller
@RequestMapping("/biblioteca-fesa")
public class BibliotecaController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"", "/", "/inicio"})
    public String principal(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index";
    }

    // Novo método para redirecionar para a controller de usuário
    @GetMapping("/cadastro")
    public String redirecionarParaUsuarios() {
        return "redirect:/Usuario/cadastro";  // Assumindo que a rota da controller de usuário é "/usuario"
    }

    @PostMapping("/login")
    public String logar(Usuario usuario, Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        if (!usuarioService.emailExiste(usuario.getEmail())) {
            model.addAttribute("erro", "E-mail não existe");
            model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
            return "redirect:/biblioteca-fesa"; // Volta para a página de cadastro com os dados já preenchidos
        }

        Usuario usuarioLogado = usuarioService.login(usuario.getEmail(), usuario.getSenha());
        // Busca o usuário no banco
        if (usuarioLogado != null) {
            CookieService.setCookie(response, "usuarioId", String.valueOf(usuarioLogado.getId()), 10000);
            CookieService.setCookie(response, "usuarioEmail", String.valueOf(usuarioLogado.getEmail()), 10000);
            return "redirect:/home";
        }
        model.addAttribute("erro", "E-mail ou senha inválidas");

        return "redirect:/biblioteca-fesa";
    }

    @GetMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {

        CookieService.setCookie(response, "ususarioId", "", 0);
        return "index";
    }
}
