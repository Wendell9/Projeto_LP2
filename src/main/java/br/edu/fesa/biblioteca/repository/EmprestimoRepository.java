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
import org.springframework.data.repository.query.Param;

/**
 *
 * @author guind
 */
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    @Query("SELECT new br.edu.fesa.biblioteca.DTO.EmprestimoJoin(e.id, u.email, e.data_emprestimo, e.data_prevista_devolucao, e.data_devolucao, e.status) FROM Emprestimo e JOIN e.usuario u")
    List<EmprestimoJoin> ListarEmprestimosDetalhados();

    @Query("SELECT new br.edu.fesa.biblioteca.DTO.EmprestimoJoin(e.id, u.email, e.data_emprestimo, e.data_prevista_devolucao, e.data_devolucao, e.status) "
            + "FROM Emprestimo e JOIN e.usuario u WHERE u.id = :id")
    List<EmprestimoJoin> listarMeusEmprestimos(@Param("id") UUID id);

    @Query("SELECT new br.edu.fesa.biblioteca.DTO.EmprestimoJoin(e.id, u.email, e.data_emprestimo, e.data_prevista_devolucao, e.data_devolucao, e.status) FROM Emprestimo e JOIN e.usuario u "
            + "where e.id=:id")
    EmprestimoJoin emprestimoDetalhado(@Param("id") UUID id);
}
