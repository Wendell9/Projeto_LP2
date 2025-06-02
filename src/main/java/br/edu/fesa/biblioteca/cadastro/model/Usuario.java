/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.cadastro.model;

/**
 *
 * @author guind
 */
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
@Table(name = "USUARIO", schema = "SISTEMA")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String senha;

    @Column(length = 20)
    private String telefone;

    @Column(columnDefinition = "boolean default true")
    private boolean status;

    @Column(length = 20)
    private String tipo_imagem;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean admin; // Valor padrão false

    private String ImagemEmbase64;

    @Lob
    @Column(name = "IMAGEM", columnDefinition = "VARBINARY(MAX)") // Para SQL Server
    // Ou alternativamente para outros bancos:
    // @Column(columnDefinition = "LONGBLOB") // MySQL
    // @Column(columnDefinition = "BYTEA") // PostgreSQL
    private byte[] imagem;

    public String getTipo_imagem() {
        return tipo_imagem;
    }

    public void setTipo_imagem(String tipo_imagem) {
        this.tipo_imagem = tipo_imagem;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    // Getters e Setters
    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID();  // Gera um UUID
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImagemEmbase64() {
        return ImagemEmbase64;
    }

    public void setImagemEmbase64() {
        if (imagem != null) {
            this.ImagemEmbase64 = Base64.getEncoder().encodeToString(imagem);
        } else {
            this.ImagemEmbase64 = null;
        }
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
    
}

