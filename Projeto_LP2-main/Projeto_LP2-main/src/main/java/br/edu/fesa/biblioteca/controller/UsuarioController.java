package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.service.CookieService;
import static br.edu.fesa.biblioteca.service.CookieService.getCookie;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Exibe a página de cadastro
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(@RequestParam(name = "sucesso", required = false) String sucesso, Model model, HttpServletRequest request) throws UnsupportedEncodingException {

        if (sucesso != null) {
            model.addAttribute("sucesso", true);
        } else {
            model.addAttribute("sucesso", false);
        }
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável no m
        model.addAttribute("usuario", new Usuario());
        return "Usuario/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model, @RequestParam("arquivoImagem") MultipartFile arquivoImagem,
            RedirectAttributes redirectAttributes, HttpServletRequest request) throws UnsupportedEncodingException {
        // Verifica se o e-mail já existe
        // Passa o valor de sucesso para o modelo (se existir)
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável no m
        try {
            // Processa a imagem
            if (!arquivoImagem.isEmpty()) {
                String nomeArquivo = arquivoImagem.getOriginalFilename();
                usuario.setImagem(arquivoImagem.getBytes());
                usuario.setTipo_imagem(nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1));
            }
            if (usuarioService.emailExiste(usuario.getEmail())) {
                model.addAttribute("erro", "E-mail já está cadastrado.");
                model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
                return "Usuario/cadastro"; // Volta para a página de cadastro com os dados já preenchidos
            }

            // Salva o usuário se o e-mail não existir
            usuarioService.save(usuario);

            return "redirect:/Usuario/cadastro?sucesso"; // Redireciona após sucesso
        } catch (IOException e) {
            model.addAttribute("erro", "Falha ao processar imagem: " + e.getMessage());
            model.addAttribute("usuario", usuario); // Preenche o formulário com os dados
            return "Usuario/cadastro"; // Volta para a página de cadastro com os dados já preenchidos
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        List<Usuario> Usuarios = usuarioService.findAll();

        List<Usuario> sortedUsuarios = Usuarios.stream()
                .sorted((padrao1, padrao2) -> padrao1.getNome().compareTo(padrao2.getNome()))
                .collect(Collectors.toList());
        model.addAttribute("usuarios", sortedUsuarios);
        return "Usuario/listarUsuario";
    }

    @GetMapping("/Meu_usuario")
    public String meuUsuario(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        try {

            String usuarioId = CookieService.getCookie(request, "usuarioId");
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

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable UUID id, ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        Usuario user = usuarioService.findById(id).get();
        user.setImagemEmbase64();
        model.addAttribute("usuario", user);
        model.addAttribute("origem", "/Usuario/lista");
        return "Usuario/editarUsuario";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable UUID id, @Valid
            @ModelAttribute Usuario usuario, BindingResult bindingResult, ModelMap model, HttpServletRequest request, @RequestParam("arquivoImagem") MultipartFile arquivoImagem) throws UnsupportedEncodingException, IOException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        if (!arquivoImagem.isEmpty()) {
            String nomeArquivo = arquivoImagem.getOriginalFilename();
            usuario.setImagem(arquivoImagem.getBytes());
            usuario.setTipo_imagem(nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "/usuario/editar";
        }
        Usuario user = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;
        user.setEmail(usuario.getEmail());
        user.setNome(usuario.getNome());
        user.setSenha(usuario.getSenha());
        user.setTelefone(usuario.getTelefone());
        user.setImagem(usuario.getImagem());
        user.setTipo_imagem(usuario.getTipo_imagem());
        usuarioService.update(user);
        return "redirect:/Usuario/lista";
    }

    @GetMapping("/salvar")
    public String salvar(Usuario usuario, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        usuarioService.save(usuario);
        return "Usuario/listarUsuario";
    }

    @PostMapping("/excluir/{id}")
    public String confirmarExclusao(@PathVariable UUID id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        usuarioService.deleteById(id);
        return "redirect:/Usuario/lista";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable UUID id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variávelF
        model.addAttribute("usuario", usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        return "Usuario/remover";
    }
}
