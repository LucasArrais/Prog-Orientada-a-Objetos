package com.lucas.model;

import com.lucas.util.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {
    @Id
    private int id;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private List<Pedido> pedidos;
    private List<Fatura> faturas;
    private String endereco;

    public Cliente(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.pedidos = new ArrayList<>();
        this.faturas = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone=" + telefone +
                '}';
    }

    public int getQtdFaturasNaoCanceladas(List<Fatura> faturas){
        int cont = 0;
        for (Fatura fatura : faturas) {
            if (fatura.getDataCancelamento() == null){
                cont += 1;
            }
        }
        return cont;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public List<Fatura> getFaturas(){
        return faturas;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() { return endereco; }

    public void setEndereco(String endereco) { this.endereco = endereco; }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
