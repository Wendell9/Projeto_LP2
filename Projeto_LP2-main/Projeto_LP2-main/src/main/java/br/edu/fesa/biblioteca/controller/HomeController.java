/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.service.CookieService;
import br.edu.fesa.biblioteca.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.UUID;

/**
 *
 * @author guind
 */

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"", "/"})
    public String principal(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("isAdmin", "true".equals(CookieService.getCookie(request, "ADMIN")));

        String usuarioId = CookieService.getCookie(request, "usuarioId");
        if (usuarioId != null && !usuarioId.isEmpty()) {
            Usuario usuario = usuarioService.findById(UUID.fromString(usuarioId)).orElse(null);
            if (usuario != null) {
                model.addAttribute("usuario", usuario);
            }
        }

        return "home";
    }

}


