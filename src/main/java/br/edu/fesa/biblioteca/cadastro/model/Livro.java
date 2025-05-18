package br.edu.fesa.biblioteca.cadastro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "LIVRO", schema = "SISTEMA")
public class Livro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Usando AUTO para gerar UUID
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(length = 100)
    private String editora;

    @Column(length = 20)
    private String isbn;

    @Column(length = 4)
    private String anoPublicacao;

    @Column(length = 100)
    private String categoria;

    @Column(nullable = false)
    private Integer quantidadeTotal;

    @Column(nullable = false)
    private Integer quantidadeDisponivel;

    private String CapaEmbase64;

    @Lob
    @Column(name = "Capa", columnDefinition = "VARBINARY(MAX)")
    private byte[] capa;

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // Método para gerar UUID automaticamente
    public void gerarId() {
        this.id = UUID.randomUUID();  // Gera um UUID único
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(String anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public String getCapaEmbase64() {
        return CapaEmbase64;
    }

    public void setCapaEmbase64() {
        if (capa != null) {
            this.CapaEmbase64 = Base64.getEncoder().encodeToString(capa);
        } else {
            this.CapaEmbase64 = null;
        }
    }

    public byte[] getCapa() {
        return capa;
    }

    public void setCapa(byte[] capa) {
        this.capa = capa;
    }
}