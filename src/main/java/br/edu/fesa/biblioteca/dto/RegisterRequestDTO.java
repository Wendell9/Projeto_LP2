/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.fesa.biblioteca.dto;

/**
 *
 * @author guind
 */
public record RegisterRequestDTO (String name, String email, String senha, String telefone, byte[] imagem) {
} 
