package com.lucas.service;

import com.lucas.dao.ClienteDAO;
import com.lucas.exception.ClienteComPedidosException;
import com.lucas.exception.EntidadeNaoEncontradaException;
import com.lucas.exception.SemClientesException;
import com.lucas.model.Cliente;
import com.lucas.util.FabricaDeDaos;

import java.util.List;

public class ClienteService {
    private final ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);

    public Cliente incluir(Cliente cliente) { return clienteDAO.incluir(cliente); }

    public Cliente recuperarClientePorId(int id){
        Cliente cliente = clienteDAO.recuperarPorId(id);
        if (cliente == null)
            throw new EntidadeNaoEncontradaException(" \n Cliente inexistente!");
        return cliente;
    }

    public Cliente remover(int id) {
        Cliente cliente = recuperarClientePorId(id);
        if (cliente == null) {
            throw new EntidadeNaoEncontradaException("Cliente inexistente.");
        }
        if (!(cliente.getPedidos().isEmpty())) {
            throw new ClienteComPedidosException(
                    "Este cliente possui pedidos e não pode ser removido.");
        }
        clienteDAO.remover(cliente.getId());
        return cliente;
    }

    public Cliente alterarCPF(Cliente cliente, String novoCPF){
        cliente.setCpf(novoCPF);
        return cliente;
    }

    public Cliente alterarNome(Cliente cliente, String novoNome){
        cliente.setNome(novoNome);
        return cliente;
    }

    public Cliente alterarEmail(Cliente cliente, String novoEmail){
        cliente.setEmail(novoEmail);
        return cliente;
    }

    public Cliente alterarTelefone(Cliente cliente, String novoTelefone){
        cliente.setTelefone(novoTelefone);
        return cliente;
    }

    public void listarClientes(List<Cliente> clientes){
        if(clientes.isEmpty()){
            throw new SemClientesException("\nNão há clientes!");
        }
        else{
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
        }
    }

    public Cliente cadastrarCliente(String cpf, String nome, String email, String telefone){
        Cliente cliente = new Cliente(cpf, nome, email, telefone);
        clienteDAO.incluir(cliente);
        return cliente;
    }

    public List<Cliente> recuperarClientes(){
        return clienteDAO.recuperarTodos();
    }

}
