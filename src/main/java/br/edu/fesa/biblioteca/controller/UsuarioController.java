package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import br.edu.fesa.biblioteca.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    

    // Exibe a página de cadastro
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(@RequestParam(name = "sucesso", required = false) String sucesso, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        
        if (sucesso != null) {
            model.addAttribute("sucesso", true);
        } else {
            model.addAttribute("sucesso", false);
        }
        model.addAttribute("usuario", new Usuario());
        return "Usuario/cadastro";
    }
    
    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model, @RequestParam("arquivoImagem") MultipartFile arquivoImagem,
            RedirectAttributes redirectAttributes, HttpServletRequest request) throws UnsupportedEncodingException {
        try {
            Usuario usuarioNovo = new Usuario();
            ModelMap erros = verificaerros(usuario);
            
            if (!erros.isEmpty()) {
                // Copia os erros para o model principal
                model.addAllAttributes(erros);
                model.addAttribute("usuario", usuario); // opcional: manter os dados preenchidos
                return "Usuario/cadastro";
            }
            
            if (!arquivoImagem.isEmpty()) {
                String nomeArquivo = arquivoImagem.getOriginalFilename();
                usuario.setImagem(arquivoImagem.getBytes());
                usuario.setTipo_imagem(nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1));
            }
            if (usuarioService.emailExiste(usuario.getEmail())) {
                model.addAttribute("erro", "E-mail já está cadastrado.");
                model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
                return "Usuario/cadastro"; // Volta para a página de cadastro com os dados já preenchidos
            } else {
                usuario.setStatus(true);
                usuarioService.save(usuario);
                model.addAttribute("sucesso", true);
                model.addAttribute("usuario", usuarioNovo);
                return "Usuario/cadastro"; // Sem parâmetro na URL
            }
        } catch (IOException e) {
            model.addAttribute("erro", "Falha ao processar imagem: " + e.getMessage());
            model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
            return "Usuario/cadastro"; // Volta para a página de cadastro com os dados já preenchidos
        }
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        List<Usuario> Usuarios = usuarioService.findAll();
        
        List<Usuario> sortedUsuarios = Usuarios.stream()
                .sorted((padrao1, padrao2) -> padrao1.getNome().compareTo(padrao2.getNome()))
                .collect(Collectors.toList());
        model.addAttribute("usuarios", sortedUsuarios);
        return "Usuario/listarUsuario";
    }
    
    @GetMapping("/Meu_usuario")
    public String meuUsuario(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) throws UnsupportedEncodingException {
        try {
            
            String usuarioId = (String) request.getSession(false).getAttribute("usuarioId");
            UUID uuid = UUID.fromString(usuarioId);
            Usuario user = usuarioService.findById(uuid).get();
            user.setImagemEmbase64();
            model.addAttribute("origem", "/home");
            model.addAttribute("usuario", user);
            return "Usuario/editarUsuario";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", "ID de usuário inválido");
            return "redirect:/biblioteca-fesa";
        }
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable UUID id, ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        Usuario user = usuarioService.findById(id).get();
        user.setImagemEmbase64();
        model.addAttribute("usuario", user);
        model.addAttribute("origem", "/Usuario/lista");
        return "Usuario/editarUsuario";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable UUID id, @Valid
            @ModelAttribute Usuario usuario, BindingResult bindingResult, ModelMap model, HttpServletRequest request, @RequestParam("arquivoImagem") MultipartFile arquivoImagem) throws UnsupportedEncodingException, IOException {
        
        int numeroAdmin;
        
        Usuario user = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;
        numeroAdmin = (int) usuarioRepository.countByAdminTrue();
        
        if (user.isAdmin() && numeroAdmin == 1 && !usuario.isAdmin()) {
            user.setImagemEmbase64();
            model.addAttribute("usuario", user);
            model.addAttribute("erro", "É necessário no minimo um usuário administrador no sistema");
            return "Usuario/editarUsuario";
        }
        
        if (!arquivoImagem.isEmpty()) {
            String nomeArquivo = arquivoImagem.getOriginalFilename();
            user.setImagem(arquivoImagem.getBytes());
            user.setTipo_imagem(nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "Usuario/editarUsuario";
        }
        
        user.setEmail(usuario.getEmail());
        user.setNome(usuario.getNome());
        user.setSenha(usuario.getSenha());
        user.setTelefone(usuario.getTelefone());
        user.setAdmin(usuario.isAdmin());
        usuarioService.update(user);
        if (user.isAdmin()) {
            return "redirect:/Usuario/lista";
        } else {
            return "redirect:/home";
        }
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/salvar")
    public String salvar(Usuario usuario, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        usuarioService.save(usuario);
        return "Usuario/listarUsuario";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/excluir/{id}")
    public String confirmarExclusao(@PathVariable UUID id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        int numeroAdmin;
        
        Usuario user = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;
        numeroAdmin = (int) usuarioRepository.countByAdminTrue();
        
        if (user.isAdmin() && numeroAdmin == 1) {
            model.addAttribute("usuario", user);
            model.addAttribute("erro", "É necessário no minimo um usuário administrador no sistema");
            return "Usuario/remover";
        }
         
        usuarioService.deleteById(id);
        return "redirect:/Usuario/lista";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable UUID id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("usuario", usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        return "Usuario/remover";
    }
    
    public ModelMap verificaerros(Usuario usuario) {
        ModelMap erros = new ModelMap();
        
        if (usuario.getNome().length() < 10) {
            erros.addAttribute("ErrorNome", "Favor insira o nome completo");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            erros.addAttribute("ErrorSenha", "A senha deve ter ao menos 6 caracteres");
        }
        if (usuario.getSenha() == null || usuario.getTelefone().length() > 11 || usuario.getTelefone().length() < 10) {
            erros.addAttribute("ErrorTelefone", "O telefone deve ter de 10 a 11 digitos");
        }
        return erros;
    }
}
