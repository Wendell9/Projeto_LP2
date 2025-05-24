package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Livro;
import br.edu.fesa.biblioteca.service.CookieService;
import br.edu.fesa.biblioteca.service.LivroService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
@RequestMapping("/Livro")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(@RequestParam(name = "sucesso", required = false) String sucesso, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("sucesso", sucesso != null);
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie));
        model.addAttribute("livro", new Livro());
        return "Livro/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarLivro(@ModelAttribute Livro livro, Model model,
                                 @RequestParam("arquivoImagem") MultipartFile arquivoImagem,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie));
        try {
            if (!arquivoImagem.isEmpty()) {
                livro.setCapa(arquivoImagem.getBytes());
            }

            livroService.save(livro);
            return "redirect:/Livro/cadastro?sucesso";
        } catch (IOException e) {
            model.addAttribute("erro", "Falha ao processar imagem: " + e.getMessage());
            model.addAttribute("livro", livro);
            return "Livro/cadastro";
        }
    }

    @GetMapping("/listarLivro")
    public String listar(ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {

    List<Livro> livros = livroService.findAll().stream()
            .sorted((l1, l2) -> l1.getTitulo().compareToIgnoreCase(l2.getTitulo()))
            .peek(livro -> livro.setCapaEmbase64()) // <<< Aqui você seta a imagem base64
            .collect(Collectors.toList());

    model.addAttribute("livros", livros);
    return "Livro/listarLivro";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable UUID id, ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {

        Livro livro = livroService.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        livro.setCapaEmbase64();
        model.addAttribute("livro", livro);
        model.addAttribute("origem", "Livro/listarLivro");
        return "Livro/editar";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable UUID id, @Valid @ModelAttribute Livro livro, ModelMap model,
                            @RequestParam("arquivoImagem") MultipartFile arquivoImagem,
                            HttpServletRequest request) throws IOException, UnsupportedEncodingException {
        Livro livroExistente = livroService.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        livroExistente.setTitulo(livro.getTitulo());
        livroExistente.setAutor(livro.getAutor());
        livroExistente.setEditora(livro.getEditora());
        livroExistente.setIsbn(livro.getIsbn());
        livroExistente.setAnoPublicacao(livro.getAnoPublicacao());
        livroExistente.setCategoria(livro.getCategoria());
        livroExistente.setQuantidadeTotal(livro.getQuantidadeTotal());
        livroExistente.setQuantidadeDisponivel(livro.getQuantidadeDisponivel());
        livroExistente.setSinopse(livro.getSinopse());

        if (!arquivoImagem.isEmpty()) {
            livroExistente.setCapa(arquivoImagem.getBytes());
        }

        livroService.update(livroExistente);
        return "redirect:/Livro/listarLivro";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable UUID id, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie));
        livroService.deleteById(id);
        return "redirect:/Livro/listarLivro";
    }

    @GetMapping("/remover/{id}")
    public String confirmarRemocao(@PathVariable UUID id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie));

        Livro livro = livroService.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        model.addAttribute("livro", livro);
        return "Livro/remover";
    }
}
