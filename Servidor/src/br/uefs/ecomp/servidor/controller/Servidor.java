package br.uefs.ecomp.servidor.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Classe que cria Socket de servidor e redireciona clientes
 * @author Victor Munduruca
 *
 */
public class Servidor {
	public static void main(String[] args) throws IOException {
		
		ServerSocket servidor = new ServerSocket(12346); //Cria socket servidor
		System.out.println("Porta aberta novo");
		while(true) { 
			Socket cliente = servidor.accept();	//Aceita as comunicação do cliente
			TrataClientes trata = new TrataClientes(cliente); // Chama a thread que trata os clientes e a roda
			new Thread(trata).start();
		}
	}
	
	
}
