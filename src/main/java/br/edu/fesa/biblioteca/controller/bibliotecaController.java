/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.infraSecurity.TokenService;
import br.edu.fesa.biblioteca.service.CookieService;
import br.edu.fesa.biblioteca.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author guind
 */
@Controller
@RequestMapping("/biblioteca-fesa")
public class BibliotecaController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TokenService tokenService;

    @GetMapping({"", "/", "/inicio"})
    public String principal(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "/index";
    }

    // Novo método para redirecionar para a controller de usuário
    @GetMapping("/cadastro")
    public String redirecionarParaUsuarios() {
        return "redirect:/Usuario/cadastro";  // Assumindo que a rota da controller de usuário é "/usuario"
    }

    @PostMapping("/login")
    public String logar(Usuario usuario, RedirectAttributes redirectAttributes, HttpServletResponse response,Model model) throws UnsupportedEncodingException {
        if (!usuarioService.emailExiste(usuario.getEmail())) {
            model.addAttribute("erro", "E-mail não existe");
            model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
            return "index"; // Volta para a página de cadastro com os dados já preenchidos
        }

        Usuario usuarioLogado = usuarioService.login(usuario.getEmail(), usuario.getSenha());
        // Busca o usuário no banco
        
        
        
        if (usuarioLogado != null) {
            String token = this.tokenService.generateToken(usuarioLogado);
            
            CookieService.setCookie(response, "token", token, 10000);
            CookieService.setCookie(response, "usuarioId", String.valueOf(usuarioLogado.getId()), 10000);
            return "redirect:/home";
        }
        model.addAttribute("erro", "E-mail ou senha inválidas");

        return "index";
    }

    @GetMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {
        limpaCookies(response);
        return  "redirect:/biblioteca-fesa";
    }

    void limpaCookies(HttpServletResponse response) throws UnsupportedEncodingException {
        CookieService.setCookie(response, "usuarioId", "", 0);
        CookieService.setCookie(response, "token", "", 0);
    }
}
