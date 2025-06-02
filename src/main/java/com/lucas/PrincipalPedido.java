package com.lucas;

import com.lucas.exception.*;
import com.lucas.model.ItemDePedido;
import com.lucas.model.Pedido;
import com.lucas.service.ItemDePedidoService;
import com.lucas.service.PedidoService;
import com.lucas.model.Cliente;
import com.lucas.service.ClienteService;
import com.lucas.model.Livro;
import com.lucas.service.LivroService;
import corejava.Console;

public class PrincipalPedido {

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final LivroService livroService = new LivroService();

    private final ItemDePedidoService itemDePedidoService = new ItemDePedidoService();

    public void principal(){

        Pedido umPedido;
        Cliente umCliente;
        Livro umLivro;

        String dataEmissao;
        String dataCancelamento;

        int qtdPedida;
        int idLivro;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você quer fazer?");
            System.out.println('\n' + "1 - Fazer um Pedido");
            System.out.println("2 - Cancelar um Pedido");
            System.out.println("3 - Listar todos os Pedidos de um Cliente");
            System.out.println("4 - Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch (opcao) {
                case 1 -> {
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataEmissao = Console.readLine("Informe a data e hora do Pedido no formato dd/MM/aaaa/hh:MM:ss");
                    try{
                        pedidoService.verificaData(dataEmissao);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    qtdPedida = Console.readInt("Informe a quantidade desejada: ");
                    idLivro = Console.readInt("Informe o id do livro: ");

                    try{
                        umLivro = livroService.recuperarLivroPorId(idLivro);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    umPedido = new Pedido(dataEmissao, umCliente);
                    ItemDePedido umItemDePedido = new ItemDePedido(qtdPedida, qtdPedida, umLivro.getPreco(), umLivro, umPedido);
                    umPedido.getItemDePedidos().add(umItemDePedido);
                    umLivro.getItemDePedidos().add(umItemDePedido);
                    umPedido.setStatus("Não faturado");
                    System.out.println("\nPedido realizado com sucesso!");

                    while(true){
                        String op = Console.readLine("\nDeseja adicionar mais itens de pedido? s/n");
                        if(op.equals("n") || op.equals("N")){
                            pedidoService.incluir(umPedido);
                            System.out.println("\nPedido cadastrado com sucesso!");
                            break;
                        }

                        if(op.equals("s") || op.equals("S")){
                            qtdPedida = Console.readInt("Informe a quantidade desejada: ");
                            idLivro = Console.readInt("Informe o id do livro: ");

                            try{
                                umLivro = livroService.recuperarLivroPorId(idLivro);
                            } catch(EntidadeNaoEncontradaException e){
                                System.out.println(e.getMessage());
                                break;
                            }

                            umItemDePedido = new ItemDePedido(qtdPedida, qtdPedida, umLivro.getPreco(), umLivro, umPedido);
                            umPedido.getItemDePedidos().add(umItemDePedido);
                            umLivro.getItemDePedidos().add(umItemDePedido);
                            System.out.println("\nItem de pedido adicionado com sucesso!");
                        }

                        else {
                            System.out.println('\n' + "Opção inválida! Item não contabilizado!");
                        }
                    }
                }

                case 2 -> {
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idPedido = Console.readInt("Informe o id do Pedido:");

                    try{
                        umPedido = pedidoService.recuperarPedidoPorId(idPedido);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataCancelamento = Console.readLine("Informe a data e hora do Cancelamento do Pedido no formato dd/MM/aaaa/hh:MM:ss");
                    try{
                        pedidoService.verificaData(dataCancelamento);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        pedidoService.cancelarPedido(umCliente, umPedido, dataCancelamento);
                    } catch(ClienteSemPedidosException | PedidoCanceladoException | PedidoFaturadoException |
                            ImpossivelCancelarPedidoException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nO Pedido " + umPedido.getId() + " foi cancelado com sucesso!\n");

                }

                case 3 -> {
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        pedidoService.listarPedidosDeUmCliente(umCliente);
                    } catch(ClienteSemPedidosException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nTodos os pedidos listados com sucesso!");
                }
                case 4 ->{
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
