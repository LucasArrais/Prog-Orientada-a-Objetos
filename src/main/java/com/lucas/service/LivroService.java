package com.lucas.service;

import com.lucas.dao.LivroDAO;
import com.lucas.exception.EntidadeComListaNaoVaziaException;
import com.lucas.exception.EntidadeNaoEncontradaException;
import com.lucas.exception.SemLivrosException;
import com.lucas.model.Livro;
import com.lucas.util.FabricaDeDaos;

import java.util.List;

public class LivroService {
    private final LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);

    public Livro incluir(Livro livro){
        return livroDAO.incluir(livro);
    }

    public Livro recuperarLivroPorId(int id) {
        Livro livro = livroDAO.recuperarPorId(id);
        if (livro == null)
            throw new EntidadeNaoEncontradaException(" \n Livro inexistente.");
        return livro;
    }

    public Livro remover(int id){
        Livro livro = recuperarLivroPorId(id);
        if (livro == null) {
            throw new EntidadeNaoEncontradaException("Livro não encontrado.");
        }
        if(!(livro.getItemDePedidos().isEmpty())){
            throw new EntidadeComListaNaoVaziaException(
                    " \n Este livro possui pedido e não pode ser removido!");
        }
        livroDAO.remover(livro.getId());
        return livro;
    }

    public Livro alterarIsbn(Livro livro, String novoIsbn){
        livro.setIsbn(novoIsbn);
        return livro;
    }

    public Livro alterarTitulo(Livro livro, String novoTitulo){
        livro.setTitulo(novoTitulo);
        return livro;
    }

    public Livro alterarDescricao(Livro livro, String novaDescricao){
        livro.setDescricao(novaDescricao);
        return livro;
    }

    public Livro alterarQtdEstoque(Livro livro, int novaQtdEstoque){
        livro.setQtdEstoque(novaQtdEstoque);
        return livro;
    }

    public Livro alterarPreco(Livro livro, double novoPreco){
        livro.setPreco(novoPreco);
        return livro;
    }

    public void listarLivros(List<Livro> livros){
        if(livros.isEmpty()){
            throw new SemLivrosException("\nNão há livros!");
        }
        else{
            for (Livro livro : livros) {
                System.out.println(livro);
            }
        }
    }

    public void atualizarEstoque(List<Livro> livros, int qtd){
        int contador = qtd;
        for(Livro livro : livros){
            livro.setQtdEstoque(livro.getQtdEstoque() + contador);
            contador += qtd;
        }
    }

    public List<Livro> recuperarLivros(){
        return livroDAO.recuperarTodos();
    }
}
