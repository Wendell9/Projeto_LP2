/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.repository;

import br.edu.fesa.biblioteca.cadastro.model.Item_Emprestimo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author guind
 */
public interface Item_EmprestimoRepository extends JpaRepository<Item_Emprestimo, UUID> {

    
    // Método que retorna todos os itens de um empréstimo específico
    //List<Item_Emprestimo> findByEmprestimoId(UUID emprestimoId);
    
       @Query("SELECT i FROM Item_Emprestimo i JOIN FETCH i.livro WHERE i.emprestimo.id = :emprestimoId")
    List<Item_Emprestimo> findItensComLivroPorEmprestimoId(@Param("emprestimoId") UUID emprestimoId);
}
