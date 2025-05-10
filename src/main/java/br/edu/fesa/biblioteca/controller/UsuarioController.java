package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import br.edu.fesa.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Usuario") 
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Exibe a página de cadastro
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "cadastro";
    }

    // Processa o formulário e salva o usuário
    @PostMapping("/salvar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setId();
        usuarioService.save(usuario);
        return "redirect:/cadastro?sucesso"; // Redireciona após sucesso
    }
}

