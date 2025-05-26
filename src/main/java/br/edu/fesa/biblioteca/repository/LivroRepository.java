/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package br.edu.fesa.biblioteca.repository;

import br.edu.fesa.biblioteca.cadastro.model.Livro;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    boolean existsByTitulo(String titulo); // Spring gera essa consulta automaticamente!

    Optional<Livro> findByTitulo(String titulo); 
    
    List<Livro> findByQuantidadeDisponivelGreaterThan(int quantidadeMinima);
}






