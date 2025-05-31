/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.DTO;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author 082220017
 */
public class EmprestimoJoin {
    private UUID id;
    
    private String email;
    
    private Date data_emprestimo;
    
    private Date data_prevista_devolucao;
    
    private Date data_devolucao;
    
    private String status;

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

    public Date getData_emprestimo() {
        return data_emprestimo;
    }

    public void setData_emprestimo(Date data_emprestimo) {
        this.data_emprestimo = data_emprestimo;
    }

    public Date getData_prevista_devolucao() {
        return data_prevista_devolucao;
    }

    public void setData_prevista_devolucao(Date data_prevista_devolucao) {
        this.data_prevista_devolucao = data_prevista_devolucao;
    }

    public Date getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(Date data_devolucao) {
        this.data_devolucao = data_devolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmprestimoJoin(UUID id, String email, Date data_emprestimo, Date data_prevista_devolucao, Date data_devolucao, String status) {
        this.id = id;
        this.email = email;
        this.data_emprestimo = data_emprestimo;
        this.data_prevista_devolucao = data_prevista_devolucao;
        this.data_devolucao = data_devolucao;
        this.status = status;
    }

}
