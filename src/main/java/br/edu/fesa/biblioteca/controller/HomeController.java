/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Livro;
import br.edu.fesa.biblioteca.service.LivroService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author guind
 */
@Controller
@RequestMapping("/home")

public class HomeController {

    @Autowired
    private LivroService livroService;

    @GetMapping({"", "/"})
    public String principal(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        List<Livro> livros = livroService.findAll().stream()
                .sorted((l1, l2) -> l1.getTitulo().compareToIgnoreCase(l2.getTitulo()))
                .peek(livro -> livro.setCapaEmbase64()) // <<< Aqui você seta a imagem base64
                .collect(Collectors.toList());

        model.addAttribute("livros", livros);
        return "home";
    }
    
    @GetMapping("/detalhes/{id}")
    public String editar(@PathVariable UUID id, ModelMap model, HttpServletRequest request) throws UnsupportedEncodingException {

        Livro livro = livroService.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        livro.setCapaEmbase64();
        model.addAttribute("livro", livro);
        return "detalhesLivro";
    }
}
