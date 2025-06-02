package com.lucas.model;

import com.lucas.util.Id;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Fatura implements Serializable {
    @Id

    private int id;
    private ZonedDateTime dataEmissao;
    private ZonedDateTime dataCancelamento;
    private Cliente cliente;
    private List<ItemFaturado> itensFaturados;

    public double valorTotal;
    public double valorTotalDoDesconto;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Fatura(String dataEmissao, Cliente cliente){
        setDataEmissao(dataEmissao);
        this.cliente = cliente;
        this.itensFaturados= new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", dataEmissao=" + dataEmissao +
                '}';
    }


    public int getId() {
        return id;
    }

    /*public void imprimeLivroQtdFaturada(List<ItemFaturado> itensFaturados){
        for (ItemFaturado itemFaturado : itensFaturados){
            Livro umLivro = itemFaturado.getItemDePedido().getLivro();
            System.out.println("Livro " + umLivro.getId() + " - " + "qtdFaturada = " + itemFaturado.getQtdFaturada());
        }
    }*/

    /*public void imprimeItemFaturado(List<ItemFaturado> itensFaturados, Fatura fatura){
        int cont = 0;
        for (ItemFaturado itemFaturado : itensFaturados){
            Livro umLivro = itemFaturado.getItemDePedido().getLivro();

            if(cont == itensFaturados.size()){
                System.out.println(" " + itemFaturado.getQtdFaturada() + " x " + umLivro.getPreco() + " = " + " " + fatura.getValortotal() + "," + "valorTotalDesconto = " + fatura.getValorTotalDoDesconto());
            }

            if (cont >= 4){
                System.out.println(" " + itemFaturado.getQtdFaturada() + " x " + umLivro.getPreco() + " = " + " " + fatura.getValortotal() + "," + "valorTotalDesconto = " + fatura.getValorTotalDoDesconto() + " (5% de desconto para a quarta fatura em diante)");
            }

            else {
                System.out.println(" " + itemFaturado.getQtdFaturada() + " x " + umLivro.getPreco() + " + ");
            }
            cont += 1;
        }
    }*/

    public void imprimeFatura(Fatura umaFatura, int faturasNaoCanceladas){
        if (faturasNaoCanceladas > 3) {
            System.out.println("Fatura " + id + "," + " valorTotalDaFatura = " + valorTotal + " valorTotalDoDesconto = " + valorTotalDoDesconto);
        }
        else{
            System.out.println("Fatura " + id + "," + " valorTotalDaFatura = " + valorTotal + " valorTotalDoDesconto = " + valorTotalDoDesconto);
        }

        for (ItemFaturado itemFaturado : itensFaturados){
            Livro umLivro = itemFaturado.getItemDePedido().getLivro();
            System.out.println("Livro " + umLivro.getId() + " - " + "qtdFaturada = " + itemFaturado.getQtdFaturada());
        }

    }

    public double getValorTotalDoDesconto() {
        return valorTotalDoDesconto;
    }

    public void setValorTotalDoDesconto(double valorTotalDoDesconto) {
        this.valorTotalDoDesconto = valorTotalDoDesconto;
    }

    public double getValortotal() {
        return valorTotal;
    }

    public void setValortotal(double valortotal) {
        this.valorTotal = valortotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonedDateTime getDataCancelamento() {
        return dataCancelamento;
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

    public ZonedDateTime getDataEmissao() {
        return dataEmissao;
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

    public List<ItemFaturado> getItensFaturados(){
        return itensFaturados;
    }

    public void setItensFaturados(List<ItemFaturado> itensFaturados) {
        this.itensFaturados = itensFaturados;
    }

}
