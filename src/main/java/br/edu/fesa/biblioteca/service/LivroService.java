/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.service;

import br.edu.fesa.biblioteca.cadastro.model.Livro;
import br.edu.fesa.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public Optional<Livro> findById(UUID id) {
        return livroRepository.findById(id);
    }

    public Livro update(Livro livro) {
        return livroRepository.save(livro);
    }

    public void deleteById(UUID id) {
        livroRepository.deleteById(id);
    }

    public boolean tituloExiste(String titulo) {
        return livroRepository.existsByTitulo(titulo);
    }
}
