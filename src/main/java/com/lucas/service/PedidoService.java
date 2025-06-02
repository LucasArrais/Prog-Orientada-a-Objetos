package com.lucas.service;

import com.lucas.dao.PedidoDAO;
import com.lucas.exception.*;
import com.lucas.model.*;
import com.lucas.util.EstendeThread;
import com.lucas.util.FabricaDeDaos;

import com.lucas.dao.ItemDePedidoDAO;

import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class PedidoService {
    private final PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);

    private final ItemDePedidoService itemDePedidoService = new ItemDePedidoService();


    public Pedido incluir(Pedido pedido){
        EstendeThread thread = new EstendeThread(pedido.getCliente());
        thread.start();
        pedidoDAO.incluir(pedido);
        pedido.getCliente().getPedidos().add(pedido);
        for(ItemDePedido umItemDePedido : pedido.getItemDePedidos()){
            itemDePedidoService.incluir(umItemDePedido);
        }
        return pedido;
    }

    public Pedido recuperarPedidoPorId(int id) {
        Pedido pedido = pedidoDAO.recuperarPorId(id);
        if (pedido == null)
            throw new EntidadeNaoEncontradaException(" \n Pedido inexistente.");
        return pedido;
    }

    public void verificaData(String data) throws DataInvalidaException {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy/HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        TemporalAccessor tA = f.parseUnresolved(data, pos);
        int mes = Integer.parseInt(data.substring(3,5));
        int dia = Integer.parseInt(data.substring(0,2));
        if (tA == null || pos.getIndex() < data.length() || mes < 1 || mes > 12 || dia < 0 || dia > 31)
            throw new DataInvalidaException("Data invalida!\n");
    }

    public Pedido remover(int id){
        Pedido pedido = this.recuperarPedidoPorId(id);

        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido inexistente.");
        }

        List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
        for (ItemDePedido itemDePedido : itemDePedidos) {
            itemDePedidoService.remover(itemDePedido.getId());
        }
        pedidoDAO.remover(pedido.getId());
        return pedido;
    }

    public Pedido cancelarPedido(Cliente umCliente, Pedido umPedido, String dataCancelamento) {
        if (umCliente.getPedidos().isEmpty()) {
            throw new ClienteSemPedidosException("\nO cliente " + umCliente.getId() + " não possui pedidos!\n");
        }

        if (umPedido.getStatus().equals("Cancelado")) {
            throw new PedidoCanceladoException("\nO pedido " + umPedido.getId() + " já está cancelado!\n");
        }

        if (umPedido.getStatus().equals("Integralmente faturado") || umPedido.getStatus().equals("Não integralmente faturado")) {
            throw new PedidoFaturadoException("\nO pedido " + umPedido.getId() + " foi faturado, não é possível cancelar!\n");
        }

        //verificando se alguma fatura foi gerada a partir de um item de pedido desse pedido
        //Só é possível cancelar um pedido se ele não tiver nenhuma fatura
        boolean tem_fatura = false;

        for(Fatura fatura : umCliente.getFaturas()){
            for(ItemFaturado itemFaturado : fatura.getItensFaturados()){
                if(itemFaturado.getItemDePedido().getPedido().getId() == umPedido.getId()){
                    tem_fatura = true;
                    break;
                }
            }
        }

        //verificando se todas as faturas desse pedido estão canceladas
        //só verifica se o pedido tiver faturas
        //Só é possível cancelar um pedido se todas as faturas tiverem sido canceladas
        boolean faturas_canceladas = true;
        if (tem_fatura == true) {
            for(Fatura fatura : umCliente.getFaturas()){
                for(ItemFaturado itemFaturado : fatura.getItensFaturados()){
                    if(itemFaturado.getItemDePedido().getPedido().getId() == umPedido.getId() && fatura.getDataCancelamento() == null){
                        faturas_canceladas = false;
                        break;
                    }
                }
            }
        }

        //só cancela se o pedido não tiver faturas ou se todas as faturas desse pedido estiverem canceladas
        if (tem_fatura == false || faturas_canceladas) {
            umPedido.setDataCancelamento(dataCancelamento);
            umPedido.setStatus("Cancelado");
        } else {
            throw new ImpossivelCancelarPedidoException("\nNão é possível cancelar o pedido " + umPedido.getId() + " !\n");
        }
        System.out.println("Pedido de id " + umPedido.getId() + "cancelado com sucesso!\n");

        return umPedido;
    }

    public void listarPedidosDeUmCliente(Cliente umCliente){
        if(umCliente.getPedidos().isEmpty()){
            throw new ClienteSemPedidosException("\nO cliente " + umCliente.getId() + " não possui pedidos!\n");
        }
        for (Pedido pedido : umCliente.getPedidos()) {
            System.out.println(pedido);
            List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
            for (ItemDePedido itemDePedido : itemDePedidos) {
                System.out.println(itemDePedido);
            }
            System.out.println("\n");
        }
    }

    public void listarTodosOsPedidos(List<Pedido> pedidos){
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
            for (ItemDePedido itemDePedido : itemDePedidos) {
                System.out.println(itemDePedido);
            }
            System.out.println("\n");
        }
    }

    public List<Pedido> recuperarPedidos(){
        return pedidoDAO.recuperarTodos();
    }

}
