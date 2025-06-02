/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.service;

import br.edu.fesa.biblioteca.cadastro.model.Emprestimo;
import br.edu.fesa.biblioteca.cadastro.model.Usuario;
import br.edu.fesa.biblioteca.repository.EmprestimoRepository;
import br.edu.fesa.biblioteca.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author guind
 */
@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimor;
    
    @Autowired
    private UsuarioRepository usuarioR;
    
    

    @Scheduled(cron = "0 0 0 * * *") // Executa todo dia à meia-noite
    public void atualizarLivrosAtrasados() {
        List<Emprestimo> emprestimos = emprestimor.findAll();
        LocalDate hoje = LocalDate.now();
        Usuario usuario;

        for (Emprestimo emprestimo : emprestimos) {
            Date dataprevista = emprestimo.getData_prevista_devolucao();
            if (dataprevista != null) {
                LocalDate dataPrevistaLocal = dataprevista.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (dataPrevistaLocal.isBefore(hoje) && !"atrasado".equalsIgnoreCase(emprestimo.getStatus())) {
                    emprestimo.setStatus("Atrasado");
                    emprestimor.save(emprestimo);
                    usuario = usuarioR.findById(emprestimo.getUsuario().getId()).orElseThrow();
                    usuario.setStatus(false);
                    usuarioR.save(usuario);
                }
            }

        }

        System.out.println("Status de emprestimos atrasados atualizado.");
    }
}
