package com.lucas;

import com.lucas.exception.*;
import com.lucas.model.*;
import com.lucas.service.PedidoService;
import com.lucas.service.ClienteService;
import com.lucas.service.LivroService;
import com.lucas.service.FaturaService;
import com.lucas.service.ItemDePedidoService;

import java.util.List;

public class PrincipalTestarSistema {

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final LivroService livroService = new LivroService();

    private final FaturaService faturaService = new FaturaService();

    private final ItemDePedidoService itemDePedidoService = new ItemDePedidoService();

    public void principal() {

        String dataEmissao_fevereiro = "28/02/2025/00:00:00";
        Pedido pedido1, pedido2, pedido3, pedido4, pedido5;
        ItemDePedido umItemDePedido;

        System.out.println('\n' + "--------------------------------------------------------");
        System.out.println('\n' + "Iniciando Teste...\n");

        System.out.println("ITEM 1) --Cadastrar os 5 livros--\n");
        Livro livro_1 = new Livro ("10", "Aaa", "Aaaa", 10, 100);
        Livro livro_2 = new Livro("20", "Bbb", "Bbbb", 20, 200);
        Livro livro_3 = new Livro("30", "Ccc", "Cccc", 30, 300);
        Livro livro_4 = new Livro("40", "Ddd", "Dddd", 40, 400);
        Livro livro_5 = new Livro("50", "Eee", "Eeee", 50, 500);

        livroService.incluir(livro_1);
        livroService.incluir(livro_2);
        livroService.incluir(livro_3);
        livroService.incluir(livro_4);
        livroService.incluir(livro_5);
        List<Livro> livrosCadastrados = livroService.recuperarLivros();
        System.out.println("Livros cadastrados com sucesso!\n");

        System.out.println("ITEM 2) --Listar os livros--\n");
        for (Livro livro : livrosCadastrados) {
            System.out.println(livro);
        }
        System.out.println("\n");

        System.out.println("ITEM 3) --Cadastrar 2 clientes--\n");
        Cliente cliente_1 = new Cliente("111", "Xxxx", "xxxx@gmail.com", "11111111");
        Cliente cliente_2 = new Cliente("222", "Yyyy", "yyyy@gmail.com", "22222222");

        clienteService.incluir(cliente_1);
        clienteService.incluir(cliente_2);
        System.out.println("Clientes cadastrados com sucesso!\n");

        System.out.println("ITEM 4&5) --Cadastrando e Listando os 5 pedidos--\n");

        // 4&5) Cadastrar/listar 5 pedidos
        //cadastrando pedido 1
        pedido1 = new Pedido("01/01/2025/23:00:00", cliente_1);
        pedido1.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(5, 5, livro_1.getPreco(), livro_1, pedido1);
        pedido1.getItemDePedidos().add(umItemDePedido);
        livro_1.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(15, 15, livro_2.getPreco(), livro_2, pedido1);
        pedido1.getItemDePedidos().add(umItemDePedido);
        livro_2.getItemDePedidos().add(umItemDePedido);
        pedidoService.incluir(pedido1); //for loop para incluir os itens de pedidos feito!

        //cadastrando pedido 2
        pedido2 = new Pedido("02/01/2025/23:00:00", cliente_1);
        pedido2.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(10, 10, livro_1.getPreco(), livro_1, pedido2);
        pedido2.getItemDePedidos().add(umItemDePedido);
        livro_1.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(40, 40, livro_3.getPreco(), livro_3, pedido2);
        pedido2.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);
        pedidoService.incluir(pedido2);

        //cadastrando pedido 3
        pedido3 = new Pedido("03/01/2025/23:00:00", cliente_1);
        pedido3.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(5, 5, livro_1.getPreco(), livro_1, pedido3);
        pedido3.getItemDePedidos().add(umItemDePedido);
        livro_1.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(10, 10, livro_3.getPreco(), livro_3, pedido3);
        pedido3.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);
        pedidoService.incluir(pedido3);

        //cadastrando pedido 4
        pedido4 = new Pedido("04/01/2025/23:59:00", cliente_1);
        pedido4.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(10, 10, livro_2.getPreco(), livro_2, pedido4);
        pedido4.getItemDePedidos().add(umItemDePedido);
        livro_2.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(10, 10, livro_3.getPreco(), livro_3, pedido4);
        pedido4.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(10, 10, livro_4.getPreco(), livro_4, pedido4);
        pedido4.getItemDePedidos().add(umItemDePedido);
        livro_4.getItemDePedidos().add(umItemDePedido);
        pedidoService.incluir(pedido4);

        //cadastrando pedido 5
        pedido5 = new Pedido("05/01/2025/23:59:00", cliente_1);
        pedido5.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(5, 5, livro_2.getPreco(), livro_2, pedido5);
        pedido5.getItemDePedidos().add(umItemDePedido);
        livro_2.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(5, 5, livro_3.getPreco(), livro_3, pedido5);
        pedido5.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(5, 5, livro_4.getPreco(), livro_4, pedido5);
        pedido5.getItemDePedidos().add(umItemDePedido);
        livro_4.getItemDePedidos().add(umItemDePedido);
        pedidoService.incluir(pedido5);

        pedidoService.listarTodosOsPedidos(pedidoService.recuperarPedidos());
        System.out.println("\n");

        System.out.println("ITEM 6) --Listando os livros--\n");
        livroService.listarLivros(livrosCadastrados);
        System.out.println("\n");

        System.out.println("ITEM 7) --Faturando os pedidos 1 e 2 para 10 de janeiro de 2025--\n");
        try{
            faturaService.faturarPedido(cliente_1, pedido1, "10/01/2025/20:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturarPedido(cliente_1, pedido2, "10/01/2025/20:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 8) --Cancelar a Fatura 2--\n");
        try{
            Fatura fatura_cop = faturaService.buscarFaturaPorId(2);
            faturaService.cancelarFatura(cliente_1, fatura_cop, "12/01/2025/20:00:00");
        } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                ClienteComNumeroInsuficienteDeFaturasException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 9) --Faturando os pedidos 3 e 4 para 20 de janeiro de 2025--\n");
        try{
            faturaService.faturarPedido(cliente_1, pedido3, "20/01/2025/20:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturarPedido(cliente_1, pedido4, "20/01/2025/20:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");


        System.out.println("ITEM 10) --Faturando o pedido 5 para 28 de fevereiro de 2025--\n");
        try{
            faturaService.faturarPedido(cliente_1, pedido5, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        System.out.println("\n");

        System.out.println("ITEM 11) --Listar todos os livros--\n");
        livroService.listarLivros(livrosCadastrados);
        System.out.println("\n");

        System.out.println("ITEM 12) --Listar todas as faturas--\n");
        faturaService.listarFaturasFormatado(faturaService.recuperarFaturas());

        System.out.println("ITEM 13) --Cancelando o pedido 5 para o dia 28/02/2025--\n");
        try{
            pedidoService.cancelarPedido(cliente_1, pedido5, "28/02/2025/20:00:00");
            System.out.println("Pedido de ID: " + pedido5.getId() + " cancelado com sucesso!");
        } catch(ClienteSemPedidosException | PedidoCanceladoException | PedidoFaturadoException |
                ImpossivelCancelarPedidoException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 14) --Cancelando a fatura 3 para o dia 06/01/2025--\n");
        try{
            faturaService.cancelarFatura(cliente_1, faturaService.buscarFaturaPorId(3), "06/01/2025/20:00:00");
        } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                ClienteComNumeroInsuficienteDeFaturasException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n");
        System.out.println("ITEM 15) --Removendo a fatura 3--\n");
        try {
            faturaService.excluirFatura(3);
        } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 16) --Removendo a fatura 4--\n");
        try {
            faturaService.excluirFatura(4);
        } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 17) --Listar todos os livros--\n");
        try {
            livroService.listarLivros(livrosCadastrados);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 18) --Abastecendo o estoque--\n");
        livroService.atualizarEstoque(livrosCadastrados, 100);
        System.out.println("Estoques Abastecidos com sucesso!");
        System.out.println("\n");

        System.out.println("ITEM 19) --Listando todos os livros--\n");
        try {
            livroService.listarLivros(livrosCadastrados);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 20) --Faturando os pedidos 1 a 5 para o mês de fevereiro de 2025--\n");

        try{
            faturaService.faturarPedido(cliente_1, pedido1, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturarPedido(cliente_1, pedido2, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturarPedido(cliente_1, pedido3, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturarPedido(cliente_1, pedido4, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturarPedido(cliente_1, pedido5, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException | ClienteSemPedidosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println("ITEM 21) --Executar Relatório 1--\n");
        faturaService.executarRelatorio_1(cliente_1.getFaturas(), livro_1, 1, 2025);
        System.out.println("\n");

        System.out.println("ITEM 22) --Executar Relátorio 2--\n");
        faturaService.executarRelatorio_2(livrosCadastrados);
        System.out.println("\n");

        System.out.println("ITEM 23) --Executar Relatório 3--\n");
        faturaService.executarRelatorio_3(cliente_1.getFaturas(), livrosCadastrados, 2, 2025);

        System.out.println('\n' + "--------------------------------------------------------\n");
        System.out.println('\n' + "Teste Finalizado!");

    }
}
