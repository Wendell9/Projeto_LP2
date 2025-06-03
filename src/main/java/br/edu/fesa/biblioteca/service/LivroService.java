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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public long getTotalBooks() {
        return livroRepository.sumTotalBooks() != null ? livroRepository.sumTotalBooks() : 0;
    }

    public long getAvailableBooks() {
        return livroRepository.sumAvailableBooks() != null ? livroRepository.sumAvailableBooks() : 0;
    }

    public Map<String, Long> getBooksByCategory() {
        return livroRepository.countBooksByCategory().stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> ((Number) obj[1]).longValue()
                ));
    }
}