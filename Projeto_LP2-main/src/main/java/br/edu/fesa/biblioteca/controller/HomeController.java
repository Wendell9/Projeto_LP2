/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author guind
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping({"", "/"})
    public String principal(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        String adminCookie = CookieService.getCookie(request, "ADMIN");
        model.addAttribute("isAdmin", "true".equals(adminCookie)); // Define a variável
        return "home";
    }
}
