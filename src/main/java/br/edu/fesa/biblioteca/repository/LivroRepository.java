/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.edu.fesa.biblioteca.repository;

import br.edu.fesa.biblioteca.cadastro.model.Livro;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    boolean existsByTitulo(String titulo); // Spring gera essa consulta automaticamente!

    Optional<Livro> findByTitulo(String titulo);

    List<Livro> findByQuantidadeDisponivelGreaterThan(int quantidadeMinima);

    @Transactional
    @Modifying
    @Query("UPDATE Livro l SET l.quantidadeDisponivel = l.quantidadeDisponivel - 1 WHERE l.id = :livroId AND l.quantidadeDisponivel > 0")
    int diminuirQuantidadeDisponivel(@Param("livroId") UUID livroId);

    @Transactional
    @Modifying
    @Query("UPDATE Livro l SET l.quantidadeDisponivel = l.quantidadeDisponivel + 1 WHERE l.id = :livroId AND l.quantidadeDisponivel > 0")
    int aumentarQuantidadeDisponivel(@Param("livroId") UUID livroId);

    @Query("SELECT l.categoria, SUM(l.quantidadeTotal) FROM Livro l GROUP BY l.categoria")
    List<Object[]> countBooksByCategory();

    @Query("SELECT SUM(l.quantidadeTotal) FROM Livro l")
    Long sumTotalBooks();

    @Query("SELECT SUM(l.quantidadeDisponivel) FROM Livro l")
    Long sumAvailableBooks();
}