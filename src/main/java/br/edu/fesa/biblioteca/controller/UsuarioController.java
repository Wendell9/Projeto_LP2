package br.edu.fesa.biblioteca.controller;
import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.service.UsuarioService;
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
import java.util.Optional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Exibe a página de cadastro
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(@RequestParam(name = "sucesso", required = false) String sucesso, Model model) {

        if (sucesso != null) {
            model.addAttribute("sucesso", true);
        } else {
            model.addAttribute("sucesso", false);
        }

        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model, @RequestParam("arquivoImagem") MultipartFile arquivoImagem,
            RedirectAttributes redirectAttributes) {
        // Verifica se o e-mail já existe
        // Passa o valor de sucesso para o modelo (se existir)
        try {
            // Processa a imagem
            if (!arquivoImagem.isEmpty()) {
                usuario.setImagem(arquivoImagem.getBytes());
            }
            if (usuarioService.emailExiste(usuario.getEmail())) {
                model.addAttribute("erro", "E-mail já está cadastrado.");
                model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
                return "cadastro"; // Volta para a página de cadastro com os dados já preenchidos
            }

            // Salva o usuário se o e-mail não existir
            usuarioService.save(usuario);

            return "redirect:/Usuario/cadastro?sucesso"; // Redireciona após sucesso
        } catch (IOException e) {
            model.addAttribute("erro", "Falha ao processar imagem: " + e.getMessage());
            model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
            return "cadastro"; // Volta para a página de cadastro com os dados já preenchidos
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap model) {
        List<Usuario> Usuarios = usuarioService.findAll();

        List<Usuario> sortedUsuarios = Usuarios.stream()
                .sorted((padrao1, padrao2) -> padrao1.getNome().compareTo(padrao2.getNome()))
                .collect(Collectors.toList());
        model.addAttribute("usuarios", sortedUsuarios);
        return "listarUsuario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable UUID id, ModelMap model) {
        Usuario user= usuarioService.findById(id).get();
        user.setImagemEmbase64();
        model.addAttribute("usuario", user);
        return "editarUsuario";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable UUID id, @Valid @ModelAttribute Usuario usuario, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "/usuario/editar";
        }
        Usuario user = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;
        user.setEmail(usuario.getEmail());
        user.setNome(usuario.getNome());
        user.setSenha(usuario.getSenha());
        user.setTelefone(usuario.getTelefone());
        usuarioService.update(user);
        return "redirect:/Usuario/lista";
    }

    @GetMapping("/salvar")
    public String salvar(Usuario usuario) {
        usuarioService.save(usuario);
        return "listarUsuario";
    }

    @PostMapping("/excluir/{id}")
    public String confirmarExclusao(@PathVariable UUID id) {
        usuarioService.deleteById(id);
        return "redirect:/Usuario/lista";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable UUID id, ModelMap model) {
        model.addAttribute("usuario", usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        return "remover";
    }
}
