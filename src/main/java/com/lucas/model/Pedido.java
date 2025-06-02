package com.lucas.model;

import com.lucas.util.Id;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    @Id
    private int id;
    private ZonedDateTime dataEmissao;
    private ZonedDateTime dataCancelamento;
    private String status;
    private Cliente cliente;
    private List<ItemDePedido> itemDePedidos;
    private String enderecoEntrega;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Pedido(String dataEmissao, Cliente cliente){
        setDataEmissao(dataEmissao);
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
        this.itemDePedidos = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", dataEmissao=" + getDataEmissao() +
                ", status=" + getStatus() + '\'' +
                ", cliente=" + cliente.getNome() +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getEnderecoEntrega() { return enderecoEntrega; }

    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataCancelamento(){
        return DTF.format(dataCancelamento.withZoneSameInstant(ZoneId.of("America/Sao_Paulo")));
    }

    public void setDataCancelamento(String data){

        try{
            int dia = Integer.parseInt(data.substring(0,2));
            int mes = Integer.parseInt(data.substring(3,5));
            int ano = Integer.parseInt(data.substring(6,10));
            int hora = Integer.parseInt(data.substring(11,13));
            int min = Integer.parseInt(data.substring(14,16));
            int seg = Integer.parseInt(data.substring(17,19));
            int mili = 0;

            this.dataCancelamento = ZonedDateTime.of(
                    ano,
                    mes,
                    dia,
                    hora,
                    min,
                    seg,
                    mili,
                    ZoneId.of("America/Sao_Paulo")).withZoneSameInstant(ZoneId.of("UTC"));
        }
        catch (StringIndexOutOfBoundsException |
               NumberFormatException |
               DateTimeException e){
        }
    }

    public String getDataEmissao() {
        return DTF.format(dataEmissao.withZoneSameInstant(ZoneId.of("America/Sao_Paulo")));
    }

    public void setDataEmissao(String data){

        try{
            int dia = Integer.parseInt(data.substring(0,2));
            int mes = Integer.parseInt(data.substring(3,5));
            int ano = Integer.parseInt(data.substring(6,10));
            int hora = Integer.parseInt(data.substring(11,13));
            int min = Integer.parseInt(data.substring(14,16));
            int seg = Integer.parseInt(data.substring(17,19));
            int mili = 0;

            this.dataEmissao = ZonedDateTime.of(
                    ano,
                    mes,
                    dia,
                    hora,
                    min,
                    seg,
                    mili,
                    ZoneId.of("America/Sao_Paulo")).withZoneSameInstant(ZoneId.of("UTC"));
        }
        catch (StringIndexOutOfBoundsException |
               NumberFormatException |
               DateTimeException e){

        }
    }

    public Cliente getCliente(){
        return cliente;
    }

    public List<ItemDePedido> getItemDePedidos(){
        return itemDePedidos;
    }

}





