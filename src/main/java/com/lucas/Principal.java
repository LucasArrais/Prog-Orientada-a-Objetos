package com.lucas;

import com.lucas.dao.*;
import com.lucas.model.*;
import com.lucas.util.FabricaDeDaos;
import corejava.Console;

import java.io.*;
import java.util.Map;

public class Principal {
    public static void main(String[] args) {

        PrincipalCliente principalCliente = new PrincipalCliente();
        PrincipalLivro principalLivro = new PrincipalLivro();
        PrincipalPedido principalPedido = new PrincipalPedido();
        PrincipalFatura principalFatura = new PrincipalFatura();
        PrincipalTestarSistema principalTestarSistema = new PrincipalTestarSistema();

        recuperarDados();

        boolean continua = true;
        while (continua) {
            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "Do que você deseja tratar?");
            System.out.println('\n' + "1 - Clientes");
            System.out.println("2 - Faturas");
            System.out.println("3 - Pedidos");
            System.out.println("4 - Livros");
            System.out.println("5 - Testar Sistema");
            System.out.println("6 - Sair");

            int escolha = Console.readInt('\n' + "Digite um número entre 1 e 6:");

            System.out.println();

            switch (escolha) {
                case 1 -> {
                    principalCliente.principal(); //Cliente
                }
                case 2 -> {
                    principalFatura.principal(); //Fatura
                }
                case 3 -> {
                    principalPedido.principal(); //Pedido
                }
                case 4 -> {
                    principalLivro.principal(); //Livro
                }
                case 5 -> {
                    principalTestarSistema.principal();//Testar sistema
                    continua = false; // Sair
                    //salvarDados();
                }
                case 6 -> {
                    continua = false; //Sair
                    salvarDados();
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }

    private static void recuperarDados() {
        try {
            ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);
            LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);
            PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);
            FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

            FileInputStream fis = new FileInputStream(new File("meusObjetos.txt"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            //Clientes
            Map<Integer, Cliente> mapDeClientes = (Map<Integer, Cliente>) ois.readObject();
            clienteDAO.setMap(mapDeClientes);
            Integer contadorDeClientes = (Integer) ois.readObject();
            clienteDAO.setContador(contadorDeClientes);

            //Livro
            Map<Integer, Livro> mapDeLivros = (Map<Integer, Livro>) ois.readObject();
            livroDAO.setMap(mapDeLivros);
            Integer contadorDeLivro = (Integer) ois.readObject();
            livroDAO.setContador(contadorDeLivro);

            //Pedido
            Map<Integer,Pedido> mapDePedidos = (Map<Integer, Pedido>) ois.readObject();
            pedidoDAO.setMap(mapDePedidos);
            Integer contadorPedido = (Integer) ois.readObject();
            pedidoDAO.setContador(contadorPedido);

            //Fatura
            Map<Integer, Fatura> mapDeFaturas = (Map<Integer,Fatura>) ois.readObject();
            faturaDAO.setMap(mapDeFaturas);
            Integer contadorDeFatura = (Integer) ois.readObject();
            faturaDAO.setContador(contadorDeFatura);


        } catch (FileNotFoundException e) {
            System.out.println("Será criado um arquivo meusObjetos.txt na finalização do programa.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private static void salvarDados() {
        ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);
        LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);
        PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);
        FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

        Map<Integer, Cliente> mapDeClientes = clienteDAO.getMap();
        Map<Integer, Livro> mapDeLivros = livroDAO.getMap();
        Map<Integer, Pedido> mapDePedidos = pedidoDAO.getMap();
        Map<Integer, Fatura> mapDeFaturas = faturaDAO.getMap();

        Integer contadorDeCliente = clienteDAO.getContador();
        Integer contadorDeLivro = livroDAO.getContador();
        Integer contadorDePedido = pedidoDAO.getContador();
        Integer contadorDeFatura = faturaDAO.getContador();


        try {
            FileOutputStream fos = new FileOutputStream(new File("meusObjetos.txt"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(mapDeClientes);
            oos.writeObject(contadorDeCliente);

            oos.writeObject(mapDeLivros);
            oos.writeObject(contadorDeLivro);

            oos.writeObject(mapDePedidos);
            oos.writeObject(contadorDePedido);

            oos.writeObject(mapDeFaturas);
            oos.writeObject(contadorDeFatura);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}