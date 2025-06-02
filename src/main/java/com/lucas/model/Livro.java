package com.lucas.model;

import com.lucas.util.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Livro implements Serializable {
    @Id
    private int id;
    private String isbn;
    private String titulo;
    private String descricao;
    private int qtdEstoque;
    private double preco;
    private List<ItemDePedido> itemDePedidos;

    public Livro(String isbn, String titulo, String descricao, int qtdEstoque, double preco) {
        this.qtdEstoque = qtdEstoque;
        this.descricao = descricao;
        this.titulo = titulo;
        this.isbn = isbn;
        this.preco = preco;
        this.itemDePedidos = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", qtdEstoque=" + qtdEstoque +
                ", preco=" + preco +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<ItemDePedido> getItemDePedidos(){
        return itemDePedidos;
    }
}
