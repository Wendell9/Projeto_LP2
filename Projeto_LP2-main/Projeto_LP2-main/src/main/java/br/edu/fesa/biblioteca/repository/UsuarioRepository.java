/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.repository;

import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmail(String email); // Spring gera essa consulta automaticamente!

    Optional<Usuario> findByEmail(String email); // Deve retornar Optional

    @Query(value = "select * from SISTEMA.USUARIO where email = :email", nativeQuery = true)
    public Usuario login(String email);
}
