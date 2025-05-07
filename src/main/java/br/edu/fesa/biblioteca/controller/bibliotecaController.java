/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author guind
 */
@Controller
@RequestMapping("/biblioteca-fesa")
public class bibliotecaController {
    @GetMapping({"", "/", "/home", "/inicio"})
    public String principal() {
        return "/index";
    }
}
