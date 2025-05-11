/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.service.AutenticacaoService;
import br.edu.fesa.biblioteca.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private AutenticacaoService autenticacaoService;
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
    public String logar(@ModelAttribute Usuario usuario, Model model)
    {
    if (!usuarioService.emailExiste(usuario.getEmail())) {
        model.addAttribute("erro", "E-mail não existe");
        model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
        return "index"; // Volta para a página de cadastro com os dados já preenchidos
    }
    
     // Busca o usuário no banco

     if(usuarioService.autenticarUsuario(usuario.getEmail(), usuario.getSenha()))
     {
          boolean autenticado = autenticacaoService.autenticarUsuario(usuario.getEmail(), usuario.getSenha());
          if(autenticado)
          {
                  return "redirect:/home";
          }
     }

        return "index"; 
    }
}
