package com.lucas.service;

import com.lucas.dao.FaturaDAO;
import com.lucas.dao.ItemFaturadoDAO;
import com.lucas.exception.*;
import com.lucas.model.*;
import com.lucas.util.FabricaDeDaos;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FaturaService {
    private final FaturaDAO daoFatura = FabricaDeDaos.getDAO(FaturaDAO.class);
    private final ItemFaturadoDAO daoItemFaturado = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);

    public Fatura adicionarFatura(Fatura fatura){
        daoFatura.incluir(fatura);
        fatura.getCliente().getFaturas().add(fatura);
        if(fatura.getCliente().getQtdFaturasNaoCanceladas(fatura.getCliente().getFaturas()) >= 4) fatura.setValorTotalDoDesconto(fatura.getValortotal() * 0.05);
        return fatura;
    }

    public Fatura buscarFaturaPorId(int id) {
        Fatura fatura = daoFatura.recuperarPorId(id);
        if (fatura == null)
            throw new EntidadeNaoEncontradaException(" \n Fatura inexistente.");
        return fatura;
    }

    public Fatura excluirFatura(int id){ //remove e atualiza o estoque
        Fatura fatura = buscarFaturaPorId(id);
        if (fatura == null) {
            throw new EntidadeNaoEncontradaException("Fatura não encontrada.");
        }

        if (fatura.getDataCancelamento() != null){
            throw new FaturaCanceladaException("\nA fatura " + id + " está cancelada! Impossível remover!\n");
        }

        if(fatura.getCliente().getFaturas().isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente " + fatura.getCliente().getId() + " não possui faturas!\n");
        }

        List<ItemFaturado> itensFaturados = fatura.getItensFaturados();

        if(!itensFaturados.isEmpty()) {
            for (ItemFaturado itemFaturado : itensFaturados) {
                Livro umLivro = itemFaturado.getItemDePedido().getLivro();
                umLivro.setQtdEstoque(umLivro.getQtdEstoque() + itemFaturado.getQtdFaturada());
                daoItemFaturado.remover(itemFaturado.getId());
            }
        }

        for(Fatura umaFatura : fatura.getCliente().getFaturas()){
            if(umaFatura.getId() == fatura.getId()){
                fatura.getCliente().getFaturas().remove(umaFatura);
                break;
            }
        }

        daoFatura.remover(fatura.getId());
        System.out.println("Fatura de id " + fatura.getId() + " removida com sucesso!\n");
        return fatura;
    }

    public Fatura cancelarFatura(Cliente umCliente, Fatura fatura, String dataCancelamento){
        if(umCliente.getFaturas().isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente " + umCliente.getId() + " não possui faturas!\n");
        }

        if (fatura == null) {
            throw new EntidadeNaoEncontradaException("Fatura não encontrada.");
        }

        if (fatura.getDataCancelamento() != null){
            throw new FaturaCanceladaException("\nA fatura " + fatura.getId() + " já está cancelado!\n");
        }

        if(umCliente.getFaturas().size() < 3){
            throw new ClienteComNumeroInsuficienteDeFaturasException("\nPara cancelar uma fatura é necessário o faturamento de pelo menos 3 pedidos!");
        }

        List<ItemFaturado> itemFaturados = fatura.getItensFaturados();

        for (ItemFaturado itemFaturado : itemFaturados){
            Livro umLivro = itemFaturado.getItemDePedido().getLivro();
            umLivro.setQtdEstoque(umLivro.getQtdEstoque() + itemFaturado.getQtdFaturada());
        }

        fatura.setDataCancelamento(dataCancelamento);
        System.out.println("Fatura de id " + fatura.getId() + " cancelada com sucesso!\n");

        return fatura;

    }

    public Fatura faturarPedido(Cliente umCliente, Pedido umPedido, String dataEmissao){

        ItemFaturadoService itemFaturadoService = new ItemFaturadoService();

        if(umCliente.getPedidos().isEmpty())
            throw new ClienteSemPedidosException("O cliente não tem pedidos!\n");


        if(umPedido.getStatus().equals("Cancelado"))
            throw new PedidoCanceladoException("Pedido cancelado! impossível faturar!\n");

        if(umPedido.getStatus().equals("Integralmente faturado"))
            throw new PedidoIntegralmenteFaturadoException("Pedido " + umPedido.getId() + " integralmente Faturado! impossível faturar!\n");

        double valorTotal = 0;
        int conta_vazio = 0;
        int faturar_novamente = 0;
        ItemFaturado umItemFaturado;

        //para todos os itens de pedido daquele pedido eu gero itens faturados
        List<ItemDePedido> itemDePedidos = umPedido.getItemDePedidos();
        List<ItemFaturado> itensfaturados_aux = new ArrayList<>();
        for (ItemDePedido itemDePedido : itemDePedidos) {
            Livro umLivro = itemDePedido.getLivro();

            if(itemDePedido.getQtdRestante() > 0) { //só pega os itens de pedido que não foram faturados ainda
                if (umLivro.getQtdEstoque() > 0) {
                    //se a qtdPedida for maior do que a qtdEstoque daquele livro
                    if (itemDePedido.getQtdRestante() > umLivro.getQtdEstoque()) { //verifico a qtdEstoque daquele livro
                        umItemFaturado = new ItemFaturado(umLivro.getQtdEstoque(), itemDePedido);
                        itemFaturadoService.incluir(umItemFaturado);
                        itensfaturados_aux.add(umItemFaturado);
                        valorTotal += umItemFaturado.getQtdFaturada() * umLivro.getPreco();
                        itemDePedido.getItensFaturados().add((umItemFaturado));
                        itemDePedido.setQtdRestante(itemDePedido.getQtdRestante() - umLivro.getQtdEstoque());
                        umLivro.setQtdEstoque(0);
                    }

                    //a qtdPedida é menor ou igual a qtdEstoque daquele livro
                    else {
                        umItemFaturado = new ItemFaturado(itemDePedido.getQtdRestante(), itemDePedido);
                        itemFaturadoService.incluir(umItemFaturado);
                        itensfaturados_aux.add(umItemFaturado);
                        valorTotal += umItemFaturado.getQtdFaturada() * umLivro.getPreco();
                        itemDePedido.getItensFaturados().add((umItemFaturado));
                        umLivro.setQtdEstoque(umLivro.getQtdEstoque() - itemDePedido.getQtdRestante());
                        itemDePedido.setQtdRestante(0);
                    }
                } else {
                    conta_vazio += 1;
                }
            }
        }

        if(conta_vazio == umPedido.getItemDePedidos().size())
            throw new EstoquesVaziosException("Impossível faturar o pedido " + umPedido.getId() + "! Todos os estoques estão vazios\n");


        for (ItemDePedido itemDePedido : itemDePedidos){
            if(itemDePedido.getQtdRestante() > 0){
                faturar_novamente = 1;
            }
        }

        if(faturar_novamente == 1){
            umPedido.setStatus("Não integralmente faturado");
        }

        else{
            umPedido.setStatus("Integralmente faturado");
        }

        Fatura umaFatura = new Fatura(dataEmissao, umCliente);

        umaFatura.setValortotal(valorTotal);
        umaFatura.setItensFaturados(itensfaturados_aux);

        adicionarFatura(umaFatura);
        System.out.println("Pedido de id " + umPedido.getId() + " faturado com sucesso!\n");

        return umaFatura;

    }

    public void gerarRelatorioLivrosPorMes(List<Fatura> faturas, Livro livroDesejado, int mes, int ano) {
        List<Fatura> faturasFiltradas = filtrarFaturasPorMesEAno(mes, ano, faturas);

        for (Fatura faturasMensal : faturasFiltradas) {
            for (ItemFaturado item : faturasMensal.getItensFaturados()) {
                if (item.getItemDePedido().getLivro().equals(livroDesejado)) {
                    imprimirDetalhesItemFaturado(item, faturasMensal);
                }
            }
        }
    }

    public void gerarRelatorioLivrosNaoFaturados(List<Livro> livros) {
        boolean todosFaturados = true;

        for (Livro livro : livros) {
            if (livro.getItemDePedidos().isEmpty()) {
                todosFaturados = false;
                System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
            } else {
                for (ItemDePedido itemPedido : livro.getItemDePedidos()) {
                    if (itemPedido.getItensFaturados().isEmpty()) {
                        todosFaturados = false;
                        System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                        break;
                    }
                }
            }
        }

        if (todosFaturados) {
            System.out.println("Todos os livros já foram faturados pelo menos uma vez.");
        }
    }

    public void gerarRelatorioFaturamentoMensal(List<Fatura> faturas, List<Livro> livros, int mes, int ano) {
        List<Fatura> faturasFiltradas = filtrarFaturasPorMesEAno(mes, ano, faturas);

        for (Livro livro : livros) {
            int quantidadeFaturada = 0;
            for (Fatura fatura : faturasFiltradas) {
                for (ItemFaturado item : fatura.getItensFaturados()) {
                    if (item.getItemDePedido().getLivro().equals(livro)) {
                        quantidadeFaturada += item.getQtdFaturada();
                    }
                }
            }
            if (quantidadeFaturada > 0) {
                System.out.println("Livro " + livro.getId() + " - Quantidade Faturada: " + quantidadeFaturada);
            }
        }
    }

    private List<Fatura> filtrarFaturasPorMesEAno(int mes, int ano, List<Fatura> faturas) {
        List<Fatura> faturasFiltradas = new ArrayList<>();
        for (Fatura fatura : faturas) {
            if (fatura.getDataEmissao().getMonthValue() == mes && fatura.getDataEmissao().getYear() == ano) {
                faturasFiltradas.add(fatura);
            }
        }
        return faturasFiltradas;
    }

    private void imprimirDetalhesItemFaturado(ItemFaturado item, Fatura fatura) {
        System.out.println("Quantidade Faturada: " + item.getQtdFaturada());
        System.out.println("Livro: " + item.getItemDePedido().getLivro().getTitulo());
        System.out.println("Data da Fatura: " + fatura.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void listarFaturasFormatado(List<Fatura> faturas){
        if(faturas.isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente não tem faturas!");
        }
        int aux = 0;
        for (Fatura fatura : faturas){
            if(fatura.getDataCancelamento() != null){
                aux += 1;
            }
            fatura.imprimeFatura(fatura, aux);
            System.out.println("\n");
        }
    }

    public void imprimeItemFaturado(ItemFaturado itemFaturado, Fatura fatura){
        System.out.println("QtdFaturada : " + itemFaturado.getQtdFaturada());
        System.out.println("Nome do Livro : " + itemFaturado.getItemDePedido().getLivro().getTitulo());
        int dia = fatura.getDataEmissao().getDayOfMonth(); //pega o dia da dataEmissão
        int mes = fatura.getDataEmissao().getMonthValue(); //pega o mês da dataEmissão
        int ano = fatura.getDataEmissao().getYear(); //pega o ano da dataEmissão
        String data;
        if (dia >= 10 && mes >= 10){
            data = " " + dia + " /" + " " + mes + " /" + " " + ano; // ex: ex: 10/10/2000
            System.out.println("Data da Fatura : " + data);
        }

        if (dia < 10 && mes < 10){
            data = "0" + dia + " /" + " 0" + mes + " /" + " " + ano; // ex: 01/01/2000
            System.out.println("Data da Fatura : " + data);
        }

        if (dia < 10 && mes >= 10){
            data = "0" + dia + " /" + " " + mes + " /" + " " + ano; // ex: 01/10/2000
            System.out.println("Data da Fatura : " + data);
        }

        if (dia >= 10 && mes < 10){
            data = " " + dia + " /" + " 0" + mes + " /" + " " + ano; // ex: 10/01/2000
            System.out.println("Data da Fatura : " + data);
        }
    }

    public List<Fatura> pegaFaturasDeAcordoComMesAno(int month, int year, List<Fatura> faturas){
        List<Fatura> faturas_MesAno = new ArrayList<>();
        for(Fatura fatura : faturas){
            int mes = fatura.getDataEmissao().getMonthValue(); //pega o mês da dataEmissão
            int ano = fatura.getDataEmissao().getYear(); //pega o ano da dataEmissao
            if(mes == month && ano == year) {
                faturas_MesAno.add(fatura);
            }
        }
        return faturas_MesAno;
    }

    public void executarRelatorio_1(List<Fatura> faturas, Livro livro1, int mes, int ano){
        List<Fatura> faturas_Janeiro = pegaFaturasDeAcordoComMesAno(mes, ano, faturas); //pego as faturas de janeiro
        for (Fatura fatura : faturas_Janeiro){ //percorro as faturas de janeiro
            for (ItemFaturado itemFaturado : fatura.getItensFaturados()){ //percorro os itens faturados de janeiro
                ItemDePedido itemDePedido = itemFaturado.getItemDePedido(); //recupero o livro do item faturado
                if(itemDePedido.getLivro() == livro1){ //se o item faturado for referente ao livro 1
                    imprimeItemFaturado(itemFaturado, fatura);
                    System.out.println("Referente ao pedido de id " + itemDePedido.getPedido().getId());
                    System.out.println("\n");
                }
            }
        }
    }

    public void executarRelatorio_2(List<Livro> livros){
        int todos_faturados = 1;
        for (Livro livro : livros){
            if(livro.getItemDePedidos().isEmpty()){ //nunca houve um pedido para aquele livro
                todos_faturados = 0;
                System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                System.out.println("\n");
            }

            else{ //o item de pedido daquele livro nunca gerou um item faturado
                for(ItemDePedido itemDePedido : livro.getItemDePedidos()){
                    if(itemDePedido.getItensFaturados().isEmpty()){
                        todos_faturados = 0;
                        System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                        System.out.println("\n");
                    }
                }
            }
        }

        if(todos_faturados == 1) {
            System.out.println("Todos os livros já foram faturados pelo menos uma vez!\n");
        }
    }

    public void executarRelatorio_3(List<Fatura> faturas, List<Livro> livros, int mes, int ano){
        int cont = 0;
        List<Fatura> faturas_fevereiro = pegaFaturasDeAcordoComMesAno(mes,ano, faturas);

        for (Livro livro : livros){
            for (Fatura fatura : faturas_fevereiro){
                for (ItemFaturado itemFaturado : fatura.getItensFaturados()){
                    ItemDePedido itemDePedido = itemFaturado.getItemDePedido();
                    if(itemDePedido.getLivro() == livro){
                        cont += itemFaturado.getQtdFaturada();
                    }
                }
            }
            if(cont != 0)
                System.out.println("Livro " + livro.getId() + " - QtdFaturada = " + cont);
            cont = 0;
        }

        System.out.println("\n");
    }



    public List<Fatura> recuperarFaturas(){
        return daoFatura.recuperarTodos();
    }
}
