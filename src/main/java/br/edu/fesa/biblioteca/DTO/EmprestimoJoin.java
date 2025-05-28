/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.DTO;

import java.sql.Date;
import java.util.UUID;

/**
 *
 * @author 082220017
 */
public class EmprestimoJoin {
    private UUID id;
    
    private String email;
    
    private Date Data_Emprestimo;
    
    private Date Data_Prevista_Devolucao;
    
    private Date Data_Devolucao;
    
    private String status;

    public EmprestimoJoin(UUID id, String email, Date Data_Emprestimo, Date Data_Prevista_Devolucao, Date Data_Devolucao, String status) {
        this.id = id;
        this.email = email;
        this.Data_Emprestimo = Data_Emprestimo;
        this.Data_Prevista_Devolucao = Data_Prevista_Devolucao;
        this.Data_Devolucao = Data_Devolucao;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getData_Emprestimo() {
        return Data_Emprestimo;
    }

    public void setData_Emprestimo(Date Data_Emprestimo) {
        this.Data_Emprestimo = Data_Emprestimo;
    }

    public Date getData_Prevista_Devolucao() {
        return Data_Prevista_Devolucao;
    }

    public void setData_Prevista_Devolucao(Date Data_Prevista_Devolucao) {
        this.Data_Prevista_Devolucao = Data_Prevista_Devolucao;
    }

    public Date getData_Devolucao() {
        return Data_Devolucao;
    }

    public void setData_Devolucao(Date Data_Devolucao) {
        this.Data_Devolucao = Data_Devolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
