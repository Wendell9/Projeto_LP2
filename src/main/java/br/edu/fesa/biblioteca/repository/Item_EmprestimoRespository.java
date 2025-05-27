/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.repository;

import br.edu.fesa.biblioteca.cadastro.model.Item_Emprestimo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author guind
 */
public interface Item_EmprestimoRespository extends JpaRepository<Item_Emprestimo, UUID> {
    
}
