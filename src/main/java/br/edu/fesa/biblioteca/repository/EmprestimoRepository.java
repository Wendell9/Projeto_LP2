/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.repository;

import br.edu.fesa.biblioteca.cadastro.model.Emprestimo;
import br.edu.fesa.biblioteca.DTO.EmprestimoJoin;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author guind
 */
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {
    @Query("SELECT new com.edu.fesa.biblioteca.DTO.EmprestimoJoin(e.id, e.dataEmprestimo, e.dataPrevistaDevolucao, e.dataDevolucao, e.status, u.email) " +
       "FROM Emprestimo e JOIN e.usuario u")
    List<EmprestimoJoin> ListarEmprestimosDetalhados();
}
