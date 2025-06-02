package com.lucas;

import com.lucas.exception.*;
import com.lucas.model.Pedido;
import com.lucas.service.PedidoService;
import com.lucas.model.Cliente;
import com.lucas.service.ClienteService;
import com.lucas.model.Fatura;
import com.lucas.service.FaturaService;
import corejava.Console;

public class PrincipalFatura {

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final FaturaService faturaService = new FaturaService();

    public void principal(){

        String dataEmissao;
        String dataCancelamento;
        Cliente umCliente;
        Pedido umPedido;
        Fatura umaFatura;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você quer fazer?");
            System.out.println('\n' + "1 - Faturar um Pedido");
            System.out.println("2 - Cancelar uma Fatura");
            System.out.println("3 - Remover uma Fatura");
            System.out.println("4 - Listar todas as Faturas de um Cliente");
            System.out.println("5 - Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch(opcao){
                case 1 ->{
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

                    dataEmissao = Console.readLine("Informe a data e hora da Fatura no formato dd/MM/aaaa/hh:MM:ss");
                    try{
                        pedidoService.verificaData(dataEmissao);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        faturaService.faturarPedido(umCliente, umPedido, dataEmissao);
                    } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
                        System.out.println(e.getMessage());
                    }

                    System.out.println("Faturamento concluído!");
                }

                case 2 ->{
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idFatura = Console.readInt("Informe o id da Fatura:");

                    try{
                        umaFatura = faturaService.buscarFaturaPorId(idFatura);
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
                        faturaService.cancelarFatura(umCliente, umaFatura, dataCancelamento);
                    } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                            ClienteComNumeroInsuficienteDeFaturasException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nFatura cancelada com sucesso!");
                }

                case 3 ->{
                    int idFatura = Console.readInt("Informe o id da Fatura:");

                    try {
                        faturaService.excluirFatura(idFatura);
                    } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nFatura removida com sucesso!");
                }

                case 4 ->{
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        umCliente = clienteService.recuperarClientePorId(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        faturaService.listarFaturasFormatado(umCliente.getFaturas());
                    } catch(ClienteSemFaturaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("Faturas listadas com sucesso!");
                }

                case 5 ->{
                    continua = false;
                }

                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
