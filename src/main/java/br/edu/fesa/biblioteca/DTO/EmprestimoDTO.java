/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.DTO;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author guind
 */
public class EmprestimoDTO {
    private String email;
    private List<UUID> livros;

    public String getEmail() { return email; }
    public List<UUID> getLivros() { return livros; }
}
