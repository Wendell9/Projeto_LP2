/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.cadastro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author guind
 */

@Entity
@Table(name = "ITEM_EMPRESTIMO", schema = "SISTEMA")
public class Item_Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column
    private UUID id_emprestimo;
    
    @Column 
    private UUID id_livro;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId_emprestimo() {
        return id_emprestimo;
    }

    public void setId_emprestimo(UUID id_emprestimo) {
        this.id_emprestimo = id_emprestimo;
    }

    public UUID getId_livro() {
        return id_livro;
    }

    public void setId_livro(UUID id_livro) {
        this.id_livro = id_livro;
    }
    
    
}
